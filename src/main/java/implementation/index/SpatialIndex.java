/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.index;

import org.locationtech.jts.geom.Envelope;
import implementation.GeometryWrapper;
import implementation.datatype.GMLDatatype;
import implementation.datatype.WKTDatatype;
import static implementation.index.CollisionResult.*;
import implementation.vocabulary.Geo;
import implementation.vocabulary.SRS_URI;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import org.apache.jena.graph.Node;
import org.apache.jena.query.Dataset;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.tdb.base.file.Location;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.operation.TransformException;
import org.opengis.util.FactoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 */
public class SpatialIndex implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final HashMap<String, Envelope> SPATIAL_INDEX = new HashMap<>();
    private static final String SPATIAL_INDEX_FILE = "spatial.index";
    private static Boolean IS_ACTIVE = true;
    private static long WARNING_ISSUED_TIME = System.currentTimeMillis();
    private static final long WARNING_DURATION = 60000;
    private static Long RETRIEVAL_COUNT = 0L;

    public static final void setActive(boolean isActive) {
        IS_ACTIVE = isActive;
    }

    public static final boolean isActive() {
        return IS_ACTIVE;
    }

    public static final CollisionResult checkCollision(NodeValue nodeValue1, NodeValue nodeValue2, Boolean isDisjoint) throws FactoryException, MismatchedDimensionException, TransformException {

        if (!IS_ACTIVE) {
            return CHECK_RELATION;
        }

        if (!nodeValue1.isLiteral()) {
            return FALSE_RELATION;
        }

        Node node1 = nodeValue1.asNode();
        String sourceDatatypeURI = node1.getLiteralDatatypeURI();
        String sourceLexicalForm = node1.getLiteralLexicalForm();

        Boolean isAdded = addIfMissing(sourceLexicalForm, sourceDatatypeURI);
        if (isAdded == null) {
            return FALSE_RELATION;
        }

        if (!nodeValue2.isLiteral()) {
            return FALSE_RELATION;
        }

        Node node2 = nodeValue1.asNode();
        String targetDatatypeURI = node2.getLiteralDatatypeURI();
        String targetLexicalForm = node2.getLiteralLexicalForm();
        isAdded = addIfMissing(targetLexicalForm, targetDatatypeURI);
        if (isAdded == null) {
            return FALSE_RELATION;
        }

        return SpatialIndex.checkCollision(sourceLexicalForm, targetLexicalForm, isDisjoint);
    }

    public static final CollisionResult checkCollision(Literal literal1, Literal literal2, Boolean isDisjoint) throws FactoryException, MismatchedDimensionException, TransformException {

        if (!IS_ACTIVE) {
            return CHECK_RELATION;
        }

        String sourceDatatypeURI = literal1.getDatatypeURI();
        String sourceLexicalForm = literal1.getLexicalForm();

        Boolean isAdded = addIfMissing(sourceLexicalForm, sourceDatatypeURI);
        if (isAdded == null) {
            return FALSE_RELATION;
        }

        String targetDatatypeURI = literal2.getDatatypeURI();
        String targetLexicalForm = literal2.getLexicalForm();
        isAdded = addIfMissing(targetLexicalForm, targetDatatypeURI);
        if (isAdded == null) {
            return FALSE_RELATION;
        }

        return SpatialIndex.checkCollision(sourceLexicalForm, targetLexicalForm, isDisjoint);
    }

    public static final CollisionResult checkCollision(String sourceGeometryLiteral, String targetGeometryLiteral, Boolean isDisjoint) throws TransformException {
        if (!IS_ACTIVE) {
            return CHECK_RELATION;
        }

        RETRIEVAL_COUNT++;

        Envelope sourceEnvelope = SPATIAL_INDEX.get(sourceGeometryLiteral);
        Envelope targetEnvelope = SPATIAL_INDEX.get(targetGeometryLiteral);
        boolean isIntersect = sourceEnvelope.intersects(targetEnvelope);

        if (!isIntersect) {
            if (isDisjoint) {
                //It doesn't intersect and is a disjoint relation so definitely true.
                return TRUE_RELATION;
            } else {
                //It doesn't intersect and is not a disjoint relation so definitely false.
                return FALSE_RELATION;
            }
        }

        //It does intersect so further checking required.
        return CHECK_RELATION;
    }

    /**
     * Adds the lexical part of a literal to the index. Checks the datatype to
     * ensure a GeometryLiteral. Returns whether added to index and null if not
     * a GeometryLiteral.
     *
     * @param lexicalForm
     * @param datatypeURI
     * @return
     */
    public static Boolean addIfMissing(String lexicalForm, String datatypeURI) {
        if (!IS_ACTIVE) {
            return false;
        }

        if (datatypeURI.equals(WKTDatatype.URI) || datatypeURI.equals(GMLDatatype.URI)) {
            if (SPATIAL_INDEX.containsKey(lexicalForm)) {
                return false;
            } else {
                insert(lexicalForm, datatypeURI);
                return true;
            }
        }

        return null;
    }

    public static final Boolean contains(String geometryLiteral) {
        return SPATIAL_INDEX.containsKey(geometryLiteral);
    }

    public static final Envelope remove(String geometryLiteral) {
        return SPATIAL_INDEX.remove(geometryLiteral);
    }

    public static final void insert(String lexicalForm, String datatypeURI) {
        try {
            if (!IS_ACTIVE) {
                long timeNow = System.currentTimeMillis();
                if (timeNow - WARNING_ISSUED_TIME > WARNING_DURATION) {
                    LOGGER.warn("Spatial Index is inactive and attempted to insert GeometryLiteral. Warning will be suppresed for {} milliseconds", WARNING_DURATION);
                    WARNING_ISSUED_TIME = timeNow;
                }
                return;
            }

            GeometryWrapper geometryWrapper = GeometryWrapper.extract(lexicalForm, datatypeURI);
            Envelope envelope = extractEnvelope(geometryWrapper);
            SPATIAL_INDEX.put(lexicalForm, envelope);
        } catch (FactoryException | MismatchedDimensionException | TransformException ex) {
            LOGGER.error("Expection Inserting: {} {} - {}", lexicalForm, datatypeURI, ex.getMessage());
        }
    }

    private static Envelope extractEnvelope(GeometryWrapper sourceGeometryWrapper) throws FactoryException, MismatchedDimensionException, TransformException {
        GeometryWrapper transformedGeometryWrapper = sourceGeometryWrapper.transform(SRS_URI.GEOCENTRIC_CARTESIAN);
        Envelope envelope = transformedGeometryWrapper.getEnvelope();
        return envelope;
    }

    public static final void clear() {
        SPATIAL_INDEX.clear();
        RETRIEVAL_COUNT = 0L;
    }

    public static final void build(Dataset dataset) {
        if (IS_ACTIVE) {
            LOGGER.info("Building Spatial Index for Dataset: Started");
            Model defaultModel = dataset.getDefaultModel();
            build(defaultModel, "Default Model");

            Model unionModel = dataset.getUnionModel();
            build(unionModel, "Union Model");
            LOGGER.info("Building Spatial Index for Dataset: Completed");
        }
    }

    public static final void build(Model model, String graphName) {
        if (IS_ACTIVE) {
            LOGGER.info("Building Spatial Index for {}: Started", graphName);

            NodeIterator nodeIt = model.listObjectsOfProperty(Geo.HAS_SERIALIZATION_PROP);
            while (nodeIt.hasNext()) {
                RDFNode node = nodeIt.nextNode();
                if (node.isLiteral()) {
                    Literal literal = node.asLiteral();
                    String lexicalForm = literal.getLexicalForm();
                    String datatypeURI = literal.getDatatypeURI();
                    addIfMissing(lexicalForm, datatypeURI);
                }
            }

            LOGGER.info("Building Spatial Index for {}: Completed- Index size: {}", graphName, SPATIAL_INDEX.size());
        }
    }

    public static final void write(File indexFolder) {
        if (IS_ACTIVE) {
            LOGGER.info("Writing Spatial Index: Started");
            indexFolder.mkdir();
            if (!indexFolder.exists()) {
                LOGGER.error("Writing Spatial Index: Failed - {} does not exist.", indexFolder.getAbsolutePath());
                return;
            }

            File indexFile = createIndexFile(indexFolder);
            writeObject(indexFile, SPATIAL_INDEX);
            LOGGER.info("Writing Spatial Index: Completed - Index size: {}", SPATIAL_INDEX.size());
        }
    }

    /**
     * Reads existing index files if TDB backed or creates new ones if not
     * present. Also, builds index if memory dataset.
     *
     * @param dataset
     */
    public static final void prepare(Dataset dataset) {

        if (TDBFactory.isBackedByTDB(dataset)) {
            Location location = TDBFactory.location(dataset);
            File datasetFolder = new File(location.getDirectoryPath());
            if (containsIndex(datasetFolder)) {
                read(datasetFolder);
            } else {
                build(dataset);
                write(datasetFolder);
            }

        } else {
            build(dataset);
        }

    }

    public static final void read(File indexFolder) {
        LOGGER.info("Reading Spatial Index: Started");
        if (!containsIndex(indexFolder)) {
            LOGGER.error("Read Spatial Index: Failed - No index file {} in {}.", SPATIAL_INDEX_FILE, indexFolder.getAbsolutePath());
            return;
        }

        if (!IS_ACTIVE) {
            LOGGER.warn("Spatial Index is inactive. Reading index file but will not be accessible unless made active.");
        }
        File indexFile = createIndexFile(indexFolder);
        Object spatialIndexObject = readObject(indexFile);
        if (spatialIndexObject instanceof HashMap<?, ?>) {
            @SuppressWarnings("unchecked")
            HashMap<String, Envelope> spatialIndex = (HashMap<String, Envelope>) spatialIndexObject;
            SPATIAL_INDEX.putAll(spatialIndex);
        }
        LOGGER.info("Reading Spatial Index: Completed - Index size: {}", SPATIAL_INDEX.size());
    }

    public static boolean containsIndex(File indexFolder) {
        File indexFile = createIndexFile(indexFolder);
        return indexFile.exists();
    }

    private static File createIndexFile(File indexFolder) {
        return new File(indexFolder, SPATIAL_INDEX_FILE);
    }

    private static void writeObject(File indexFile, Object index) {

        LOGGER.info("Writing Index - {}: Started", indexFile.getName());
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(indexFile))) {
            objectOutputStream.writeObject(index);
        } catch (IOException ex) {
            LOGGER.error("Store Index exception: {}", ex.getMessage());
        }
        LOGGER.info("Writing Index - {}: Completed", indexFile.getName());
    }

    @SuppressWarnings("unchecked")
    private static Object readObject(File indexFile) {

        if (!indexFile.exists()) {
            LOGGER.error("Index does not exist: {}", indexFile.getAbsolutePath());
            return null;
        }

        LOGGER.info("Reading Index - {}: Started", indexFile.getName());
        Object result;
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(indexFile))) {
            @SuppressWarnings("unchecked")
            Object object = objectInputStream.readObject();
            result = object;
        } catch (IOException | ClassNotFoundException ex) {
            LOGGER.error("Read Index exception: {}", ex.getMessage());
            result = null;
        }
        LOGGER.info("Reading Index - {}: Completed", indexFile.getName());
        return result;
    }

    public static final void deleteIndexFile(File indexFolder) {
        File indexFile = createIndexFile(indexFolder);
        LOGGER.info("Deleting Index - {}: Started", indexFile.getName());
        if (indexFile.exists()) {
            indexFile.delete();
            LOGGER.info("Deleting Index - {}: Completed", indexFile.getName());
        } else {
            LOGGER.warn("Deleting Index - {}: Index file does not exist.", indexFile.getName());
        }
    }

    public static final Integer getSpatialIndexSize() {
        return SPATIAL_INDEX.size();
    }

    public static final Long getRetrievalCount() {
        return RETRIEVAL_COUNT;
    }
}
