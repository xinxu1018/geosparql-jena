/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geo.topological.simplefeatures.PF;

import geo.topological.GenericPropertyFunction;
import geof.topological.simplefeatures.EF.sfTouchesEF;
import org.apache.jena.sparql.expr.Expr;

/**
 *
 * @author haozhechen
 * <<<<<<< HEAD
 * @a
 * uthor Gregory Albiston
 */
public class sfTouchesPF extends GenericPropertyFunction {

    @Override
    protected Expr expressionFunction(Expr expr1, Expr expr2) {
        return new sfTouchesEF(expr1, expr2);
    }

}