/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.functionregistry;

import org.apache.jena.sparql.function.FunctionRegistry;
import org.apache.jena.sparql.pfunction.PropertyFunctionRegistry;
import implementation.support.Prefixes;
import implementation.support.Vocabulary;

/**
 *
 * @author Gregory Albiston
 */
public class FunctionLoader {

    public static void addPropertyFunction(PropertyFunctionRegistry registry, String functionName, Class<?> funcClass) {

        String uri = Vocabulary.getFunctionURI(Prefixes.GEO_URI, functionName);
        registry.put(uri, funcClass);
    }

    public static void addFilterFunction(FunctionRegistry registry, String functionName, Class<?> funcClass) {

        String uri = Vocabulary.getFunctionURI(Prefixes.GEOF_URI, functionName);
        registry.put(uri, funcClass);
    }

}
