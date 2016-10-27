/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datatype;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.gml2.GMLReader;
import com.vividsolutions.jts.io.gml2.GMLWriter;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.jena.datatypes.BaseDatatype;
import org.apache.jena.datatypes.DatatypeFormatException;
import org.apache.jena.datatypes.RDFDatatype;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author haozhechen
 */
public class GmlDatatype extends BaseDatatype {

    private static final Logger LOGGER = LoggerFactory.getLogger(GmlDatatype.class);

    /**
     * The default WKT type URI.
     */
    public static final String theTypeURI = "http://www.opengis.net/ont/geosparql#gmlLiteral";

    /**
     * A static instance of WktDatatype.
     */
    public static final RDFDatatype theGmlDatatype = new GmlDatatype();

    /**
     * XML element tag "gml" is defined for the convenience of GML generation.
     */
    public static final String GMLPrefix = "gml";

    /**
     * The spatial reference system "urn:ogc:def:crs:OGC::CRS84" is returned
     * for all generated GML literal.
     */
    public static final String GMLSRSNAme = "urn:ogc:def:crs:OGC::CRS84";

    /**
     * private constructor - single global instance.
     */
    public GmlDatatype() {
        super(theTypeURI);
    }

    /**
     * This method Un-parses the JTS Geometry to the GML literal
     *
     * @param geometry - the JTS Geometry to be un-parsed
     * @return GML - the returned GML Literal.
     * <br> Notice that the Spatial Reference System
     * "urn:ogc:def:crs:OGC::CRS84" is predefined in the returned GML literal.
     */
    @Override
    public String unparse(Object geometry) {
        Geometry geom = (Geometry) geometry;
        GMLWriter gmlWriter = new GMLWriter();
        gmlWriter.setNamespace(true);
        gmlWriter.setSrsName(GMLSRSNAme);
        gmlWriter.setPrefix(GMLPrefix);
        String gml = gmlWriter.write(geom);
        return gml;
    }

    /**
     * This method Parses the GML literal to the JTS Geometry
     *
     * @param lexicalForm - the GML literal to be parsed
     * @return geometry - if the GML literal is valid.
     * <br> empty geometry - if the GML literal is empty.
     * <br> null - if the GML literal is invalid.
     */
    @Override
    public Geometry parse(String lexicalForm) throws DatatypeFormatException {
        GMLReader gmlReader = new GMLReader();
        try {
            Geometry geometry = gmlReader.read(lexicalForm, null);
            geometry.setUserData(GMLSRSNAme);
            return geometry;
        } catch (IOException | ParserConfigurationException | org.xml.sax.SAXException ex) {
            LOGGER.error("Illegal GML literal: {}", lexicalForm);
            return null;
        }
    }
}