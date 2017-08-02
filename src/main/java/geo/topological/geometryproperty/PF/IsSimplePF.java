/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geo.topological.geometryproperty.PF;

import geo.topological.GenericGeometryPropertyFunction;
import geof.topological.geometryproperty.EF.IsSimpleEF;
import org.apache.jena.sparql.expr.Expr;

/**
 *
 * @author haozhechen
 */
public class IsSimplePF extends GenericGeometryPropertyFunction {

    @Override
    protected Expr propFunc(Expr expr) {
        return new IsSimpleEF(expr);
    }

}
