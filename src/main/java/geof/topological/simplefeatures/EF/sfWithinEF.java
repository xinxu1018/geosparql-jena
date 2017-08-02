/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geof.topological.simplefeatures.EF;

import implementation.GeometryWrapper;
import geof.topological.GenericExpressionFunction;
import org.apache.jena.sparql.expr.Expr;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;
import implementation.vocabulary.Geof;

/**
 *
 * @author haozhechen
 * @author Gregory Albiston
 */
public class sfWithinEF extends GenericExpressionFunction {

    public sfWithinEF(Expr expr1, Expr expr2) {
        super(expr1, expr2, Geof.SF_WITHIN);
    }

    @Override
    public Expr copy(Expr arg1, Expr arg2) {
        return new sfWithinEF(arg1, arg2);
    }

    @Override
    protected boolean relate(GeometryWrapper sourceGeometry, GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {
        return sourceGeometry.within(targetGeometry);
    }
}