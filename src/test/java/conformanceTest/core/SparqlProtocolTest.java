/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformanceTest.core;

import static conformanceTest.ConformanceTestSuite.INF_WKT_MODEL;
import java.util.ArrayList;
import static main.Main.init;
import main.TopologyRegistryLevel;
import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.QuerySolutionMap;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import vocabulary.Prefixes;

/**
 *
 * @author haozhechen
 *
 * A.1.1 /conf/core/sparql-protocol
 * Requirement: /req/core/sparql-protocol
 *
 * Implementations shall support the SPARQL Query Language for RDF
 * [W3C SPARQL], the SPARQL Protocol for RDF [W3CSPARQL Protocol] and
 * the SPARQL Query Results XML Format [W3C SPARQL Result Format].
 *
 * a.) Test purpose: check conformance with this requirement
 *
 * b.) Test method: verify that the implementation accepts SPARQL
 * queries and returns the correct results in the correct format,
 * according to the SPARQL Query Language for RDF, the SPARQL Protocol
 * for RDF and SPARQL Query Results XML Format W3C specifications.
 *
 * c.) Reference: Clause 6.1 Req 1 d.) Test Type: Capabilities.
 */
public class SparqlProtocolTest {

    @BeforeClass
    public static void setUpClass() {
        /**
         * Initialize all the topology functions.
         */
        init(TopologyRegistryLevel.DEFAULT);
    }

    @AfterClass
    public static void tearDownClass() {
    }

    private ArrayList expectedList;
    private ArrayList actualList;

    @Before
    public void setUp() {
        this.expectedList = new ArrayList<>();
        this.actualList = new ArrayList<>();
    }

    @After
    public void tearDown() {
        this.actualList.clear();
        this.expectedList.clear();
    }

    @Test
    public void positiveTest() {

        this.expectedList.add("<http://www.opengis.net/def/crs/OGC/1.3/CRS84> Point(-83.4 34.4)^^http://www.opengis.net/ont/geosparql#wktLiteral");

        String Q1 = "SELECT ?aWKT WHERE{"
                + " ntu:A ntu:hasExactGeometry ?aGeom ."
                + " ?aGeom geo:asWKT ?aWKT ."
                + "}";
        QuerySolutionMap bindings = new QuerySolutionMap();
        ParameterizedSparqlString query = new ParameterizedSparqlString(Q1, bindings);
        query.setNsPrefixes(Prefixes.get());

        try (QueryExecution qexec = QueryExecutionFactory.create(query.asQuery(), INF_WKT_MODEL)) {
            ResultSet results = qexec.execSelect();
            while (results.hasNext()) {
                QuerySolution solution = results.nextSolution();
                Literal literal = solution.getLiteral("?aWKT");
                this.actualList.add(literal.toString());
            }
        }
        assertEquals("failure - result arrays list not same", this.expectedList, this.actualList);
    }
}
