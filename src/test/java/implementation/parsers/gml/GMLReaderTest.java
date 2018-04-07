/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.parsers.gml;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;
import implementation.CustomGeometryFactory;
import implementation.DimensionInfo;
import implementation.GeometryWrapper;
import implementation.jts.CustomCoordinateSequence;
import implementation.support.GeoSerialisationEnum;
import java.io.IOException;
import org.jdom2.JDOMException;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 *
 */
public class GMLReaderTest {

    public final String URN_SRS_NAMESPACE = "urn:ogc:def:crs:EPSG::27700";
    public final String URL_SRS_NAMESPACE = "http://www.opengis.net/def/crs/EPSG/0/27700";

    public GMLReaderTest() {
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

    private static final GeometryFactory GEOMETRY_FACTORY = CustomGeometryFactory.theInstance();

    @Test
    public void testPointURL() throws JDOMException, IOException {
        GeometryWrapper geo = GMLReader.read("<gml:Point srsName=\"http://www.opengis.net/def/crs/EPSG/0/27700\" xmlns:gml=\"http://www.opengis.net/ont/gml\"><gml:pos>-83.4 34.4</gml:pos></gml:Point>");
        Geometry test = GEOMETRY_FACTORY.createPoint(new CustomCoordinateSequence(CustomCoordinateSequence.CoordinateSequenceDimensions.XY, "-83.4 34.4"));
        GeometryWrapper expResult = new GeometryWrapper(test, URL_SRS_NAMESPACE, GeoSerialisationEnum.GML, new DimensionInfo(2, 2, 0));

        //System.out.println("Expected: " + expResult);
        //System.out.println("Result: " + geo);
        assertEquals(geo, expResult);
    }

    @Test
    public void testPointURN() throws JDOMException, IOException {
        GeometryWrapper geo = GMLReader.read("<gml:Point srsName=\"urn:ogc:def:crs:EPSG::27700\" xmlns:gml=\"http://www.opengis.net/ont/gml\"><gml:pos>-83.4 34.4</gml:pos></gml:Point>");
        Geometry test = GEOMETRY_FACTORY.createPoint(new CustomCoordinateSequence(CustomCoordinateSequence.CoordinateSequenceDimensions.XY, "-83.4 34.4"));
        GeometryWrapper expResult = new GeometryWrapper(test, URN_SRS_NAMESPACE, GeoSerialisationEnum.GML, new DimensionInfo(2, 2, 0));

        //System.out.println("Expected: " + expResult);
        //System.out.println("Result: " + geo);
        assertEquals(geo, expResult);
    }

    @Test
    public void testLineString() throws JDOMException, IOException {
        GeometryWrapper geo = GMLReader.read("<gml:LineString srsName=\"urn:ogc:def:crs:EPSG::27700\" xmlns:gml=\"http://www.opengis.net/ont/gml\"><gml:posList srsDimension=\"2\">-83.4 34.0 -83.3 34.3</gml:posList></gml:LineString>");
        Geometry test = GEOMETRY_FACTORY.createLineString(new CustomCoordinateSequence(CustomCoordinateSequence.CoordinateSequenceDimensions.XY, "-83.4 34.0, -83.3 34.3"));
        GeometryWrapper expResult = new GeometryWrapper(test, URN_SRS_NAMESPACE, GeoSerialisationEnum.GML, new DimensionInfo(2, 2, 1));

        //System.out.println("Expected: " + expResult);
        //System.out.println("Result: " + geo);
        assertEquals(geo, expResult);
    }

    @Test
    public void testPolygon() throws JDOMException, IOException {
        GeometryWrapper geo = GMLReader.read("<gml:Polygon xmlns:gml=\"http://www.opengis.net/ont/gml\" srsName=\"urn:ogc:def:crs:EPSG::27700\"><gml:exterior><gml:posList srsDimension=\"2\">30 10 40 40 20 40 10 20 30 10</gml:posList></gml:exterior></gml:Polygon>");
        Geometry test = GEOMETRY_FACTORY.createPolygon(new CustomCoordinateSequence(CustomCoordinateSequence.CoordinateSequenceDimensions.XY, "30 10, 40 40, 20 40, 10 20, 30 10"));
        GeometryWrapper expResult = new GeometryWrapper(test, URN_SRS_NAMESPACE, GeoSerialisationEnum.GML, new DimensionInfo(2, 2, 2));

        //System.out.println("Expected: " + expResult);
        //System.out.println("Result: " + geo);
        assertEquals(geo, expResult);
    }

    @Test
    public void testPolygon2() throws JDOMException, IOException {
        GeometryWrapper geo = GMLReader.read("<gml:Polygon xmlns:gml=\"http://www.opengis.net/ont/gml\" srsName=\"urn:ogc:def:crs:EPSG::27700\"><gml:exterior><gml:posList srsDimension=\"2\">30 10 40 40 20 40 10 20 30 10</gml:posList></gml:exterior><gml:interior><gml:posList srsDimension=\"2\">20 30 35 35 30 20 20 30</gml:posList></gml:interior></gml:Polygon>");
        LinearRing shell = GEOMETRY_FACTORY.createLinearRing(new CustomCoordinateSequence(CustomCoordinateSequence.CoordinateSequenceDimensions.XY, "30 10, 40 40, 20 40, 10 20, 30 10"));
        LinearRing[] holes = new LinearRing[]{GEOMETRY_FACTORY.createLinearRing(new CustomCoordinateSequence(CustomCoordinateSequence.CoordinateSequenceDimensions.XY, "20 30, 35 35, 30 20, 20 30"))};
        Geometry test = GEOMETRY_FACTORY.createPolygon(shell, holes);
        GeometryWrapper expResult = new GeometryWrapper(test, URN_SRS_NAMESPACE, GeoSerialisationEnum.GML, new DimensionInfo(2, 2, 2));

        //System.out.println("Expected: " + expResult);
        //System.out.println("Result: " + geo);
        assertEquals(geo, expResult);
    }

    @Test
    public void testMultiPoint() throws JDOMException, IOException {
        GeometryWrapper geo = GMLReader.read("<gml:MultiPoint xmlns:gml=\"http://www.opengis.net/ont/gml\" srsName=\"urn:ogc:def:crs:EPSG::27700\"><gml:PointMember><gml:Point><gml:pos>10 40</gml:pos></gml:Point></gml:PointMember><gml:PointMember><gml:Point><gml:pos>40 30</gml:pos></gml:Point></gml:PointMember><gml:PointMember><gml:Point><gml:pos>20 20</gml:pos></gml:Point></gml:PointMember><gml:PointMember><gml:Point><gml:pos>30 10</gml:pos></gml:Point></gml:PointMember></gml:MultiPoint>");
        Geometry test = GEOMETRY_FACTORY.createMultiPoint(new CustomCoordinateSequence(CustomCoordinateSequence.CoordinateSequenceDimensions.XY, "10 40, 40 30, 20 20, 30 10"));
        GeometryWrapper expResult = new GeometryWrapper(test, "urn:ogc:def:crs:EPSG::27700", GeoSerialisationEnum.GML, new DimensionInfo(2, 2, 0));

        //System.out.println("Expected: " + expResult);
        //System.out.println("Result: " + geo);
        assertEquals(geo, expResult);
    }

    @Test
    public void testMultiLineString() throws JDOMException, IOException {
        GeometryWrapper geo = GMLReader.read("<gml:MultiLineString xmlns:gml=\"http://www.opengis.net/ont/gml\" srsName=\"urn:ogc:def:crs:EPSG::27700\"><gml:LineStringMember><gml:LineString><gml:posList srsDimension=\"2\">10 10 20 20 10 40</gml:posList></gml:LineString></gml:LineStringMember><gml:LineStringMember><gml:LineString><gml:posList srsDimension=\"2\">40 40 30 30 40 20 30 10</gml:posList></gml:LineString></gml:LineStringMember></gml:MultiLineString>");
        LineString[] lineStrings = new LineString[2];
        lineStrings[0] = GEOMETRY_FACTORY.createLineString(new CustomCoordinateSequence(CustomCoordinateSequence.CoordinateSequenceDimensions.XY, "10 10, 20 20, 10 40"));
        lineStrings[1] = GEOMETRY_FACTORY.createLineString(new CustomCoordinateSequence(CustomCoordinateSequence.CoordinateSequenceDimensions.XY, "40 40, 30 30, 40 20, 30 10"));
        Geometry test = GEOMETRY_FACTORY.createMultiLineString(lineStrings);
        GeometryWrapper expResult = new GeometryWrapper(test, URN_SRS_NAMESPACE, GeoSerialisationEnum.GML, new DimensionInfo(2, 2, 1));

        //System.out.println("Expected: " + expResult);
        //System.out.println("Result: " + geo);
        assertEquals(geo, expResult);
    }

    @Test
    public void testMultiPolygon() throws JDOMException, IOException {
        GeometryWrapper geo = GMLReader.read("<gml:MultiPolygon xmlns:gml=\"http://www.opengis.net/ont/gml\" srsName=\"urn:ogc:def:crs:EPSG::27700\"><gml:PolygonMember><gml:Polygon srsName=\"urn:ogc:def:crs:EPSG::27700\"><gml:exterior><gml:posList srsDimension=\"2\">40 40 20 45 45 30 40 40</gml:posList></gml:exterior></gml:Polygon></gml:PolygonMember><gml:PolygonMember><gml:Polygon srsName=\"urn:ogc:def:crs:EPSG::27700\"><gml:exterior><gml:posList srsDimension=\"2\">20 35 10 30 10 10 30 5 45 20 20 35</gml:posList></gml:exterior><gml:interior><gml:posList srsDimension=\"2\">30 20 20 15 20 25 30 20</gml:posList></gml:interior></gml:Polygon></gml:PolygonMember></gml:MultiPolygon>");
        Polygon[] polygons = new Polygon[2];
        polygons[0] = GEOMETRY_FACTORY.createPolygon(new CustomCoordinateSequence(CustomCoordinateSequence.CoordinateSequenceDimensions.XY, "40 40, 20 45, 45 30, 40 40"));
        LinearRing shell = GEOMETRY_FACTORY.createLinearRing(new CustomCoordinateSequence(CustomCoordinateSequence.CoordinateSequenceDimensions.XY, "20 35, 10 30, 10 10, 30 5, 45 20, 20 35"));
        LinearRing[] holes = new LinearRing[]{GEOMETRY_FACTORY.createLinearRing(new CustomCoordinateSequence(CustomCoordinateSequence.CoordinateSequenceDimensions.XY, "30 20, 20 15, 20 25, 30 20"))};
        polygons[1] = GEOMETRY_FACTORY.createPolygon(shell, holes);
        Geometry test = GEOMETRY_FACTORY.createMultiPolygon(polygons);
        GeometryWrapper expResult = new GeometryWrapper(test, URN_SRS_NAMESPACE, GeoSerialisationEnum.GML, new DimensionInfo(2, 2, 2));

        //System.out.println("Expected: " + expResult);
        //System.out.println("Result: " + geo);
        assertEquals(geo, expResult);
    }

    @Test
    public void testGeometryCollection() throws JDOMException, IOException {
        GeometryWrapper geo = GMLReader.read("<gml:GeometryCollection xmlns:gml=\"http://www.opengis.net/ont/gml\" srsName=\"urn:ogc:def:crs:EPSG::27700\"><gml:GeometryMember><gml:Point srsName=\"urn:ogc:def:crs:EPSG::27700\"><gml:pos>4 6</gml:pos></gml:Point></gml:GeometryMember><gml:GeometryMember><gml:LineString srsName=\"urn:ogc:def:crs:EPSG::27700\"><gml:posList srsDimension=\"2\">4 6 7 10</gml:posList></gml:LineString></gml:GeometryMember></gml:GeometryCollection>");
        Geometry[] geometries = new Geometry[2];
        geometries[0] = GEOMETRY_FACTORY.createPoint(new CustomCoordinateSequence(CustomCoordinateSequence.CoordinateSequenceDimensions.XY, "4 6"));
        geometries[1] = GEOMETRY_FACTORY.createLineString(new CustomCoordinateSequence(CustomCoordinateSequence.CoordinateSequenceDimensions.XY, "4 6,7 10"));
        Geometry test = GEOMETRY_FACTORY.createGeometryCollection(geometries);
        GeometryWrapper expResult = new GeometryWrapper(test, URN_SRS_NAMESPACE, GeoSerialisationEnum.GML, new DimensionInfo(2, 2, 1));

        //System.out.println("Expected: " + expResult);
        //System.out.println("Result: " + geo);
        assertEquals(geo, expResult);
    }
}