/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geof.topological.simplefeatures;

import geof.topological.GenericExpressionFunction;
import implementation.GeometryWrapper;
import implementation.vocabulary.Geof;
import org.apache.jena.sparql.expr.Expr;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;

/**
 *
 * @author haozhechen
 * @author Gregory Albiston
 */
public class sfCrossesEF extends GenericExpressionFunction {

    public sfCrossesEF(Expr expr1, Expr expr2) {
        super(expr1, expr2, Geof.SF_CROSSES);
    }

    @Override
    public Expr copy(Expr arg1, Expr arg2) {
        return new sfCrossesEF(arg1, arg2);
    }

    @Override
    protected boolean relate(GeometryWrapper sourceGeometry, GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {
        return sourceGeometry.crosses(targetGeometry);
    }
}