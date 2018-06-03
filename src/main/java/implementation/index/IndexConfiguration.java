/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.index;

import implementation.registry.CRSRegistry;
import implementation.registry.MathTransformRegistry;
import java.io.File;
import java.util.UUID;
import org.apache.commons.io.FileUtils;

/**
 *
 *
 */
public class IndexConfiguration {

    /*
     * Index Configuration Parameters
     */
    private static File indexStorageFolder = null;
    private static Thread shutdownStorageThread = null;
    private static IndexOption indexOptionEnum = IndexOption.MEMORY;

    public static final void setConfig(IndexOption indexOption, File indexFolder) {
        indexOptionEnum = indexOption;

        switch (indexOptionEnum) {
            case MEMORY:
                IndexConfiguration.setupMemoryIndex(indexFolder);
                break;
            default:
                IndexConfiguration.setupNoIndex();
        }
    }

    public static void setupNoIndex() {
        removeMemoryIndexStorageThread();
        zeroMemoryIndexMaxSize();
        clearStoredMemIndexes();
    }

    public static void setupMemoryIndex(File indexFolder) {
        if (indexFolder != null) {
            //Only load and setup storage once per filename.
            if (indexStorageFolder == null | !indexFolder.equals(indexStorageFolder)) {
                loadMemoryIndexes(indexFolder);
                storeMemoryIndexesAtShutdown(indexFolder);
                indexStorageFolder = indexFolder;
            }
        }
    }

    public static final void clearAllIndexesAndRegistries() {
        GeometryLiteralIndex.clear();
        QueryRewriteIndex.clear();
        CRSRegistry.clearAll();
        MathTransformRegistry.clear();
    }

    private static void clearStoredMemIndexes() {
        if (indexStorageFolder != null) {
            for (String filename : IndexDefaultValues.INDEX_REGISTRY_FILENAMES) {
                File indexFile = new File(indexStorageFolder, filename);
                FileUtils.deleteQuietly(indexFile);
            }
            indexStorageFolder = null;
        }
    }

    public static final void setIndexMaxSize(Integer geometryLiteralIndexMaxSize, Integer geometryTransformIndexMaxSize, Integer queryRewriteIndexMaxSize) {
        GeometryLiteralIndex.setMaxSize(geometryLiteralIndexMaxSize);
        GeometryTransformIndex.setMaxSize(geometryTransformIndexMaxSize);
        QueryRewriteIndex.setMaxSize(queryRewriteIndexMaxSize);
    }

    private static void loadMemoryIndexes(File indexFolder) {
        //CRS Registry
        File crsRegistryFile = new File(indexFolder, IndexDefaultValues.CRS_REGISTRY_FILENAME);
        if (crsRegistryFile.exists()) {
            CRSRegistry.readCRSRegistry(crsRegistryFile);
        }

        //Math Transform Registry
        File mathTransformRegistryFile = new File(indexFolder, IndexDefaultValues.MATH_TRANSFORM_REGISTRY_FILENAME);
        if (mathTransformRegistryFile.exists()) {
            MathTransformRegistry.read(mathTransformRegistryFile);
        }
        //Geometry Literal Index
        File geometryLiteralIndexFile = new File(indexFolder, IndexDefaultValues.GEOMETRY_LITERAL_INDEX_FILENAME);
        if (geometryLiteralIndexFile.exists()) {
            GeometryLiteralIndex.read(geometryLiteralIndexFile);
        }
        //Geometry Transform Index
        File geometryTransformIndexFile = new File(indexFolder, IndexDefaultValues.GEOMETRY_TRANSFORM_INDEX_FILENAME);
        if (geometryTransformIndexFile.exists()) {
            GeometryTransformIndex.read(geometryTransformIndexFile);
        }
        //Query Rewrite Index
        File queryRewriteIndexFile = new File(indexFolder, IndexDefaultValues.QUERY_REWRITE_INDEX_FILENAME);
        if (queryRewriteIndexFile.exists()) {
            QueryRewriteIndex.read(queryRewriteIndexFile);
        }
    }

    public static void defaultMemoryIndexMaxSize() {
        GeometryLiteralIndex.setMaxSize(IndexDefaultValues.GEOMETRY_LITERAL_INDEX_MAX_SIZE_DEFAULT);
        GeometryTransformIndex.setMaxSize(IndexDefaultValues.GEOMETRY_TRANSFORM_INDEX_MAX_SIZE_DEFAULT);
        QueryRewriteIndex.setMaxSize(IndexDefaultValues.QUERY_REWRITE_INDEX_MAX_SIZE_DEFAULT);
    }

    private static void storeMemoryIndexesAtShutdown(File indexFolder) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                writeIndexRegistryToFile(indexFolder);
            }
        };
        Runtime.getRuntime().addShutdownHook(thread);
        //Remove any previous shutdown storage thread.
        removeMemoryIndexStorageThread();
        //Retain the thread in case called again.
        shutdownStorageThread = thread;
    }

    public static void writeIndexRegistryToFile(File indexFolder) {
        //CRS Registry
        File crsRegistryFile = new File(indexFolder, IndexDefaultValues.CRS_REGISTRY_FILENAME);
        CRSRegistry.writeCRSRegistry(crsRegistryFile);
        //Math Transform Registry
        File mathTransformRegistryFile = new File(indexFolder, IndexDefaultValues.MATH_TRANSFORM_REGISTRY_FILENAME);
        MathTransformRegistry.write(mathTransformRegistryFile);
        //Geometry Literal Index
        File geometryLiteralIndexFile = new File(indexFolder, IndexDefaultValues.GEOMETRY_LITERAL_INDEX_FILENAME);
        GeometryLiteralIndex.write(geometryLiteralIndexFile);
        //Geometry Transform Index
        File geometryTransformIndexFile = new File(indexFolder, IndexDefaultValues.GEOMETRY_TRANSFORM_INDEX_FILENAME);
        GeometryTransformIndex.write(geometryTransformIndexFile);
        //Query Rewrite Index
        File queryRewriteIndex = new File(indexFolder, IndexDefaultValues.QUERY_REWRITE_INDEX_FILENAME);
        QueryRewriteIndex.write(queryRewriteIndex);
    }

    private static void removeMemoryIndexStorageThread() {
        if (shutdownStorageThread != null) {
            Runtime.getRuntime().removeShutdownHook(shutdownStorageThread);
        }
    }

    public static void zeroMemoryIndexMaxSize() {
        GeometryLiteralIndex.setMaxSize(0);
        GeometryTransformIndex.setMaxSize(0);
        QueryRewriteIndex.setMaxSize(0);
    }

    public static final IndexOption getIndexOption() {
        return indexOptionEnum;
    }

    public static final String createURI(String namespaceURI, String prefix) {
        return namespaceURI + prefix + "-" + UUID.randomUUID().toString();
    }

}
