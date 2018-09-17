/*
 * Copyright 2018 the original author or authors.
 * See the notice.md file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package geosparql_jena.geo.topological.property_functions.simple_features;

import geosparql_jena.implementation.datatype.WKTDatatype;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.ResourceFactory;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Overlap compares two geometries of the same dimension and returns t (TRUE) if
 * their intersection set results in a geometry different from both but of the
 * same dimension.
 */
public class SfOverlapsPFTest {

    public SfOverlapsPFTest() {
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

    //Point-Point returns false. Have to compare two sets of points.
    @Test
    public void testFilterFunction_multipoint_multipoint() {
        System.out.println("filterFunction_multipoint_multipoint");

        Literal subjectGeometryLiteral = ResourceFactory.createTypedLiteral("<http://www.opengis.net/def/crs/EPSG/0/27700> MULTIPOINT(60 60, 20 20)", WKTDatatype.INSTANCE);
        Literal objectGeometryLiteral = ResourceFactory.createTypedLiteral("<http://www.opengis.net/def/crs/EPSG/0/27700> MULTIPOINT(60 60, 10 10)", WKTDatatype.INSTANCE);

        SfOverlapsPF instance = new SfOverlapsPF();

        Boolean expResult = true;
        Boolean result = instance.testFilterFunction(subjectGeometryLiteral, objectGeometryLiteral);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    @Test
    public void testFilterFunction_linestring_linestring() {
        System.out.println("filterFunction_linestring_linestring");

        Literal subjectGeometryLiteral = ResourceFactory.createTypedLiteral("<http://www.opengis.net/def/crs/EPSG/0/27700> LINESTRING(80 20, 80 50)", WKTDatatype.INSTANCE);
        Literal objectGeometryLiteral = ResourceFactory.createTypedLiteral("<http://www.opengis.net/def/crs/EPSG/0/27700> LINESTRING(80 85, 80 30)", WKTDatatype.INSTANCE);

        SfOverlapsPF instance = new SfOverlapsPF();

        Boolean expResult = true;
        Boolean result = instance.testFilterFunction(subjectGeometryLiteral, objectGeometryLiteral);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    @Test
    public void testFilterFunction_polygon_polygon() {
        System.out.println("filterFunction_polygon_polygon");

        Literal subjectGeometryLiteral = ResourceFactory.createTypedLiteral("<http://www.opengis.net/def/crs/EPSG/0/27700> POLYGON((80 15, 80 45, 140 45, 140 15, 80 15))", WKTDatatype.INSTANCE);
        Literal objectGeometryLiteral = ResourceFactory.createTypedLiteral("<http://www.opengis.net/def/crs/EPSG/0/27700> POLYGON((30 40, 30 70, 90 70, 90 40, 30 40))", WKTDatatype.INSTANCE);

        SfOverlapsPF instance = new SfOverlapsPF();

        Boolean expResult = true;
        Boolean result = instance.testFilterFunction(subjectGeometryLiteral, objectGeometryLiteral);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    @Test
    public void testFilterFunction_point_point_false() {
        System.out.println("filterFunction_point_point_false");

        Literal subjectGeometryLiteral = ResourceFactory.createTypedLiteral("<http://www.opengis.net/def/crs/EPSG/0/27700> MULTIPOINT(60 60, 20 20)", WKTDatatype.INSTANCE);
        Literal objectGeometryLiteral = ResourceFactory.createTypedLiteral("<http://www.opengis.net/def/crs/EPSG/0/27700> MULTIPOINT(60 60, 20 20)", WKTDatatype.INSTANCE);

        SfOverlapsPF instance = new SfOverlapsPF();

        Boolean expResult = false;
        Boolean result = instance.testFilterFunction(subjectGeometryLiteral, objectGeometryLiteral);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    @Test
    public void testFilterFunction_linestring_linestring_false() {
        System.out.println("filterFunction_linestring_linestring_false");

        Literal subjectGeometryLiteral = ResourceFactory.createTypedLiteral("<http://www.opengis.net/def/crs/EPSG/0/27700> LINESTRING(80 20, 80 50)", WKTDatatype.INSTANCE);
        Literal objectGeometryLiteral = ResourceFactory.createTypedLiteral("<http://www.opengis.net/def/crs/EPSG/0/27700> LINESTRING(75 60, 145 60)", WKTDatatype.INSTANCE);

        SfOverlapsPF instance = new SfOverlapsPF();

        Boolean expResult = false;
        Boolean result = instance.testFilterFunction(subjectGeometryLiteral, objectGeometryLiteral);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    @Test
    public void testFilterFunction_polygon_polygon_false() {
        System.out.println("filterFunction_polygon_polygon_false");

        Literal subjectGeometryLiteral = ResourceFactory.createTypedLiteral("<http://www.opengis.net/def/crs/EPSG/0/27700> POLYGON((30 40, 30 70, 90 70, 90 40, 30 40))", WKTDatatype.INSTANCE);
        Literal objectGeometryLiteral = ResourceFactory.createTypedLiteral("<http://www.opengis.net/def/crs/EPSG/0/27700> POLYGON((140 15, 140 45, 200 45, 200 15, 140 15))", WKTDatatype.INSTANCE);

        SfOverlapsPF instance = new SfOverlapsPF();

        Boolean expResult = false;
        Boolean result = instance.testFilterFunction(subjectGeometryLiteral, objectGeometryLiteral);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

}