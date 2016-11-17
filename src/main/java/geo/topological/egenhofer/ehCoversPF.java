/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geo.topological.egenhofer;

import geo.topological.GenericPropertyFunction;
import geof.topological.egenhofer.ehCoversEF;
import org.apache.jena.sparql.expr.Expr;

/**
 *
 * @author haozhechen
 * @author Gregory Albiston
 */
public class ehCoversPF extends GenericPropertyFunction {

    @Override
    protected Expr expressionFunction(Expr expr1, Expr expr2) {
        return new ehCoversEF(expr1, expr2);
    }

}
