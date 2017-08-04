/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformanceTest.geometryextension.wkt;

import static conformanceTest.ConformanceTestSuite.initWktEmptyModel;
import static conformanceTest.ConformanceTestSuite.literalQuery;
import implementation.functionregistry.RegistryLoader;
import java.util.Arrays;
import java.util.List;
import org.apache.jena.rdf.model.InfModel;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

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

    @BeforeClass
    public static void setUpClass() {
        /**
         * Initialize all the topology functions.
         */
        RegistryLoader.load();
        infModel = initWktEmptyModel();
    }

    private static InfModel infModel;

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
        List<String> actualList = literalQuery(Q1, infModel);
        assertEquals(expectedList, actualList);
    }

}