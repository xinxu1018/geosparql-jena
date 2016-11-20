/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geof.topological;

import implementation.GeometryWrapper;
import org.apache.jena.datatypes.DatatypeFormatException;
import org.apache.jena.sparql.expr.Expr;
import org.apache.jena.sparql.expr.ExprFunction1;
import org.apache.jena.sparql.expr.NodeValue;
import org.opengis.geometry.MismatchedDimensionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author haozhechen
 */
public abstract class GenericIntegerExpressionFunction extends ExprFunction1 {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenericIntegerExpressionFunction.class);

    public GenericIntegerExpressionFunction(Expr expr, String symbol) {
        super(expr, symbol);
    }

    @Override
    public NodeValue eval(NodeValue v) {
        try {
            GeometryWrapper geometry = GeometryWrapper.extract(v);

            int result = integerProperty(geometry);

            return NodeValue.makeInteger(result);
        } catch (DatatypeFormatException | MismatchedDimensionException ex) {
            LOGGER.error("Expression Function Exception: {}", ex.getMessage());
            return NodeValue.nvNothing;
        }

    }

    protected abstract int integerProperty(GeometryWrapper geometry);
}
