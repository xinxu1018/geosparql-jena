/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformance_test.geometry_extension.wkt;

import static conformance_test.ConformanceTestSuite.*;
import implementation.GeoSPARQLModel;
import implementation.datatype.WKTDatatype;

import java.util.ArrayList;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.ResourceFactory;
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
 * A.3.2.5 /conf/geometry-extension/geometry-as-wkt-literal
 *
 * Requirement: /req/geometry-extension/geometry-as-wkt-literal Implementations
 * shall allow the RDF property geo:asWKT to be used in SPARQL graph patterns.
 *
 * a.) Test purpose: check conformance with this requirement
 *
 * b.) Test method: verify that queries involving the geo:asWKT property return
 * the correct result for a test dataset.
 *
 * c.) Reference: Clause 8.5.2 Req 14
 *
 * d.) Test Type: Capabilities
 */
public class GeometryAsWktLiteralTest {

    @BeforeClass
    public static void setUpClass() {
        /**
         * Initialize all the topology functions.
         */
        
        infModel = initWktModel();
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

        ArrayList<Literal> expResult = new ArrayList<>();
        expResult.add(ResourceFactory.createTypedLiteral("<http://www.opengis.net/def/crs/OGC/1.3/CRS84> Point(-83.4 34.4)", WKTDatatype.THE_WKT_DATATYPE));

        String queryString = "SELECT ?aWKT WHERE{"
                + " ex:A ex:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + "}";
        ArrayList<Literal> result = literalQuery(queryString, infModel);
        assertEquals(expResult, result);
    }

}
