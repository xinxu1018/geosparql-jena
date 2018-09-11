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
package implementation.function_registration;

import geo.topological.property_functions.simple_features.SfContainsPF;
import geo.topological.property_functions.simple_features.SfCrossesPF;
import geo.topological.property_functions.simple_features.SfDisjointPF;
import geo.topological.property_functions.simple_features.SfEqualsPF;
import geo.topological.property_functions.simple_features.SfIntersectsPF;
import geo.topological.property_functions.simple_features.SfOverlapsPF;
import geo.topological.property_functions.simple_features.SfTouchesPF;
import geo.topological.property_functions.simple_features.SfWithinPF;
import geof.topological.filter_functions.simple_features.SfContainsFF;
import geof.topological.filter_functions.simple_features.SfCrossesFF;
import geof.topological.filter_functions.simple_features.SfDisjointFF;
import geof.topological.filter_functions.simple_features.SfEqualsFF;
import geof.topological.filter_functions.simple_features.SfIntersectsFF;
import geof.topological.filter_functions.simple_features.SfOverlapsFF;
import geof.topological.filter_functions.simple_features.SfTouchesFF;
import geof.topological.filter_functions.simple_features.SfWithinFF;
import implementation.vocabulary.Geo;
import implementation.vocabulary.Geof;
import org.apache.jena.sparql.function.FunctionRegistry;
import org.apache.jena.sparql.pfunction.PropertyFunctionRegistry;

/**
 *
 *
 *
 */
public class SimpleFeatures {

    /**
     * This method loads all the Simple Feature Topological Property Functions
     * as well as the Query Rewrite Property Functions
     *
     * @param registry - the PropertyFunctionRegistry to be used
     */
    public static void loadPropertyFunctions(PropertyFunctionRegistry registry) {

        // Simple Feature Topological Property Functions
        registry.put(Geo.SF_CONTAINS_NAME, SfContainsPF.class);
        registry.put(Geo.SF_CROSSES_NAME, SfCrossesPF.class);
        registry.put(Geo.SF_DISJOINT_NAME, SfDisjointPF.class);
        registry.put(Geo.SF_EQUALS_NAME, SfEqualsPF.class);
        registry.put(Geo.SF_INTERSECTS_NAME, SfIntersectsPF.class);
        registry.put(Geo.SF_OVERLAPS_NAME, SfOverlapsPF.class);
        registry.put(Geo.SF_TOUCHES_NAME, SfTouchesPF.class);
        registry.put(Geo.SF_WITHIN_NAME, SfWithinPF.class);
    }

    /**
     * This method loads all the Simple Feature Topological Expression Functions
     *
     * @param functionRegistry
     */
    public static void loadFilterFunctions(FunctionRegistry functionRegistry) {

        functionRegistry.put(Geof.SF_CONTAINS, SfContainsFF.class);
        functionRegistry.put(Geof.SF_CROSSES, SfCrossesFF.class);
        functionRegistry.put(Geof.SF_DISJOINT, SfDisjointFF.class);
        functionRegistry.put(Geof.SF_EQUALS, SfEqualsFF.class);
        functionRegistry.put(Geof.SF_INTERSECTS, SfIntersectsFF.class);
        functionRegistry.put(Geof.SF_OVERLAPS, SfOverlapsFF.class);
        functionRegistry.put(Geof.SF_TOUCHES, SfTouchesFF.class);
        functionRegistry.put(Geof.SF_WITHIN, SfWithinFF.class);

    }
}
