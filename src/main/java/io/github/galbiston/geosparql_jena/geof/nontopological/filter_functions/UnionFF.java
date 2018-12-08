/*
 * Copyright 2018 the original author or authors.
 * See the notice.md file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.galbiston.geosparql_jena.geof.nontopological.filter_functions;

import io.github.galbiston.geosparql_jena.implementation.GeometryWrapper;
import io.github.galbiston.geosparql_jena.implementation.index.GeometryLiteralIndex.GeometryIndex;
import java.lang.invoke.MethodHandles;
import org.apache.jena.sparql.expr.ExprEvalException;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.function.FunctionBase2;
import org.apache.jena.sparql.util.FmtUtils;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.operation.TransformException;
import org.opengis.util.FactoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 *
 */
public class UnionFF extends FunctionBase2 {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public NodeValue exec(NodeValue v1, NodeValue v2) {

        try {
            GeometryWrapper geometry1 = GeometryWrapper.extract(v1, GeometryIndex.PRIMARY);
            if (geometry1 == null) {
                throw new ExprEvalException("Not a GeometryLiteral: " + FmtUtils.stringForNode(v1.asNode()));
            }

            GeometryWrapper geometry2 = GeometryWrapper.extract(v2, GeometryIndex.SECONDARY);
            if (geometry2 == null) {
                throw new ExprEvalException("Not a GeometryLiteral: " + FmtUtils.stringForNode(v2.asNode()));
            }

            GeometryWrapper union = geometry1.union(geometry2);
            return union.asNode();

        } catch (FactoryException | MismatchedDimensionException | TransformException ex) {
            LOGGER.error("Exception: {}, {}, {}", v1, v2, ex.getMessage());
            throw new ExprEvalException(ex.getMessage() + ": " + FmtUtils.stringForNode(v1.asNode()) + ", " + FmtUtils.stringForNode(v2.asNode()));
        }

    }

}
