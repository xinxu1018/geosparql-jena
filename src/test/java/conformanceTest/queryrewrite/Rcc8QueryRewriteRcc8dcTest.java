/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformanceTest.queryrewrite;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 *
 * @author haozhechen
 *
 * A.6.3.1 /conf/query-rewrite-extension/rcc8-query-rewrite
 *
 * Requirement: /req/query-rewrite-extension/rcc8-query-rewrite
 * Basic graph pattern matching shall use the semantics defined by the
 * RIF Core Entailment Regime [W3C SPARQL Entailment] for the RIF rules
 * [W3C RIF Core] geor:rcc8eq, geor:rcc8dc, geor:rcc8ec, geor:rcc8po,
 * geor:rcc8tppi, geor:rcc8tpp, geor:rcc8ntpp, geor:rcc8ntppi.
 *
 * a.) Test purpose: check conformance with this requirement
 *
 * b.) Test method: Verify that queries involving the following query
 * transformation rules return the correct result for a test dataset
 * when using the specified serialization and version: geor:rcc8eq,
 * geor:rcc8dc, geor:rcc8ec, geor:rcc8po, geor:rcc8tppi, geor:rcc8tpp,
 * geor:rcc8ntpp, geor:rcc8ntppi.
 *
 * c.) Reference: Clause 11.4 Req 30
 *
 * d.) Test Type: Capabilities
 */
public class Rcc8QueryRewriteRcc8dcTest {

    @BeforeClass
    public static void setUpClass() {
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

}