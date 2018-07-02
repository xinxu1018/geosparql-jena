/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.index.expiring;

import implementation.GeoSPARQLSupport;
import implementation.GeometryWrapper;
import implementation.datatype.WKTDatatype;
import implementation.index.GeometryLiteralIndex;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opengis.referencing.FactoryException;

/**
 *
 *
 */
public class ExpiringIndexTest {

    public ExpiringIndexTest() {
    }

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

    /**
     * Test of put method, of class ExpiringIndex.
     *
     * @throws java.lang.InterruptedException
     */
    @Test
    public void testExpiry() throws InterruptedException {
        System.out.println("expiry");

        long expiryInterval = 5000l;
        long halfExpiryInterval = expiryInterval / 2;

        ExpiringIndex<String, String> instance = new ExpiringIndex<>(5, expiryInterval, halfExpiryInterval, "Test");
        instance.startExpiry();
        instance.put("key1", "value1");
        instance.put("key2", "value2");
        instance.put("key3", "value3");
        instance.put("key4", "value4");
        Thread.sleep(halfExpiryInterval);
        instance.put("key5", "value5");
        instance.put("key6", "value6");
        //System.out.println("Size Before: " + instance.size());
        Thread.sleep(expiryInterval);
        //System.out.println("Size After: " + instance.size());
        int result = instance.size();
        int expResult = 1;
        instance.stopExpiry();

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of put method, of class ExpiringIndex.
     *
     * @throws java.lang.InterruptedException
     */
    @Test
    public void testRefresh() throws InterruptedException {
        System.out.println("refresh");

        long expiryInterval = 2000l;
        long halfExpiryInterval = expiryInterval / 2;
        long quarterExpiryInterval = expiryInterval / 3 * 4;

        ExpiringIndex<String, String> instance = new ExpiringIndex<>(5, expiryInterval, halfExpiryInterval, "Test");
        instance.startExpiry();
        instance.put("key1", "value1");
        instance.put("key2", "value2");
        instance.put("key3", "value3");
        instance.put("key4", "value4");
        Thread.sleep(halfExpiryInterval + quarterExpiryInterval);
        instance.put("key1", "value1");
        instance.put("key2", "value2");
        //System.out.println("Size Before: " + instance.size());
        Thread.sleep(halfExpiryInterval);
        //System.out.println("Size After: " + instance.size());
        int result = instance.size();
        int expResult = 2;
        instance.stopExpiry();

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of put method, of class ExpiringIndex.
     *
     * @throws java.lang.InterruptedException
     */
    @Test
    public void testEmpty() throws InterruptedException {
        System.out.println("empty");

        long expiryInterval = 2000l;
        long halfExpiryInterval = expiryInterval / 2;

        ExpiringIndex<String, String> instance = new ExpiringIndex<>(5, expiryInterval, halfExpiryInterval, "Test");
        instance.startExpiry();
        instance.put("key1", "value1");
        instance.put("key2", "value2");
        instance.put("key3", "value3");
        instance.put("key4", "value4");
        Thread.sleep(halfExpiryInterval);
        instance.put("key1", "value1");
        instance.put("key2", "value2");
        //System.out.println("Size Before: " + instance.size());
        Thread.sleep(expiryInterval * 2);
        //System.out.println("Size After: " + instance.size());
        int result = instance.size();
        int expResult = 0;
        instance.stopExpiry();

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Index Retrieval of class GeometryLiteral.
     *
     * @throws org.opengis.referencing.FactoryException
     */
    @Test
    public void testIndexRetrieval() throws FactoryException {
        System.out.println("Index Retrieval");
        GeoSPARQLSupport.loadFunctionsMemoryIndex();
        String testItem = "POINT EMPTY";
        GeometryWrapper result = GeometryLiteralIndex.retrieve(testItem, WKTDatatype.INSTANCE, GeometryLiteralIndex.GeometryIndex.PRIMARY);

        GeometryWrapper expResult = GeometryLiteralIndex.retrieve(testItem, WKTDatatype.INSTANCE, GeometryLiteralIndex.GeometryIndex.PRIMARY);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);

    }

}
