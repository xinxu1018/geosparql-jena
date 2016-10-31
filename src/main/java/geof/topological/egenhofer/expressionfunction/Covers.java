/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geof.topological.egenhofer.expressionfunction;

import implementation.GeometryWrapper;
import geof.topological.GenericExpressionFunction;
import org.apache.jena.sparql.expr.Expr;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;
import implementation.support.Vocabulary;

/**
 *
 * @author haozhechen
 * @author Gregory Albiston
 */
public class Covers extends GenericExpressionFunction {

    public Covers(Expr expr1, Expr expr2) {
        super(expr1, expr2, Vocabulary.EH_COVERS_SYMBOL);
    }

    @Override
    public Expr copy(Expr arg1, Expr arg2) {
        return new Covers(arg1, arg2);
    }

    @Override
    protected boolean relate(GeometryWrapper sourceGeometry, GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {
        return sourceGeometry.relate(targetGeometry, "T*TFT*FF*");
    }

}
