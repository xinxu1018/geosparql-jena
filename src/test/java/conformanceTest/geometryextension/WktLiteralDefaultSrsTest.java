/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformanceTest.geometryextension;

import static conformanceTest.ConformanceTestSuite.literalQuery;
import conformanceTest.RDFDataLocationTest;
import static implementation.functionregistry.RegistryLoader.load;
import java.util.Arrays;
import java.util.List;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerRegistry;
import org.apache.jena.util.FileManager;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author haozhechen
 *
 * A.3.2.2 /conf/geometry-extension/wkt-literal-default-srs
 *
 * Requirement: /req/geometry-extension/wkt-literal-default-srs The URI
 * <http://www.opengis.net/def/crs/OGC/1.3/CRS84> shall be assumed as the
 * spatial reference system for geo:wktLiterals that do not specify an explicit
 * spatial reference system URI.
 *
 * a.) Test purpose: check conformance with this requirement
 *
 * b.) Test method: verify that queries involving geo:wktLiteral values without
 * an explicit encoded spatial reference system URI return the correct result
 * for a test dataset.
 *
 * c.) Reference: Clause 8.5.1 Req 11
 *
 * d.) Test Type: Capabilities
 */
public class WktLiteralDefaultSrsTest {

    /**
     * Default WKT model - with no inference support.
     */
    public static Model DEFAULT_WKT_MODEL;

    /**
     * This negative model facilitates the test for empty geometries and the WKT
     * literal without a specified SRID.
     */
    public static InfModel TEST_WKT_MODEL;

    @BeforeClass
    public static void setUpClass() {
        /**
         * Initialize all the topology functions.
         */
        load();
        /**
         * Setup inference model.
         */
        DEFAULT_WKT_MODEL = ModelFactory.createDefaultModel();
        Model schema = FileManager.get().loadModel("http://schemas.opengis.net/geosparql/1.0/geosparql_vocab_all.rdf");
        /**
         * The use of OWL reasoner can bind schema with existing test data.
         */
        Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
        reasoner = reasoner.bindSchema(schema);
        TEST_WKT_MODEL = ModelFactory.createInfModel(reasoner, DEFAULT_WKT_MODEL);
        TEST_WKT_MODEL.read(RDFDataLocationTest.SAMPLE_WKT_EMPTY);
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {

    }

    @Test
    public void positiveTest() {

        List<String> expectedList = Arrays.asList("http://www.opengis.net/def/crs/OGC/1.3/CRS84");

        String Q1 = "SELECT ((geof:getSRID( ?aWKT )) AS ?srid) WHERE{"
                + " ex:B ex:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + "}";
        List<String> actualList = literalQuery(Q1, TEST_WKT_MODEL);
        assertEquals("failure - result arrays list not same", expectedList, actualList);
    }

}
