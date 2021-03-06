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
package io.github.galbiston.geosparql_jena.implementation.function_registration;

import io.github.galbiston.geosparql_jena.geo.topological.property_functions.egenhofer.EhContainsPF;
import io.github.galbiston.geosparql_jena.geo.topological.property_functions.egenhofer.EhCoveredByPF;
import io.github.galbiston.geosparql_jena.geo.topological.property_functions.egenhofer.EhCoversPF;
import io.github.galbiston.geosparql_jena.geo.topological.property_functions.egenhofer.EhDisjointPF;
import io.github.galbiston.geosparql_jena.geo.topological.property_functions.egenhofer.EhEqualsPF;
import io.github.galbiston.geosparql_jena.geo.topological.property_functions.egenhofer.EhInsidePF;
import io.github.galbiston.geosparql_jena.geo.topological.property_functions.egenhofer.EhMeetPF;
import io.github.galbiston.geosparql_jena.geo.topological.property_functions.egenhofer.EhOverlapPF;
import io.github.galbiston.geosparql_jena.geof.topological.filter_functions.egenhofer.EhContainsFF;
import io.github.galbiston.geosparql_jena.geof.topological.filter_functions.egenhofer.EhCoveredByFF;
import io.github.galbiston.geosparql_jena.geof.topological.filter_functions.egenhofer.EhCoversFF;
import io.github.galbiston.geosparql_jena.geof.topological.filter_functions.egenhofer.EhDisjointFF;
import io.github.galbiston.geosparql_jena.geof.topological.filter_functions.egenhofer.EhEqualsFF;
import io.github.galbiston.geosparql_jena.geof.topological.filter_functions.egenhofer.EhInsideFF;
import io.github.galbiston.geosparql_jena.geof.topological.filter_functions.egenhofer.EhMeetFF;
import io.github.galbiston.geosparql_jena.geof.topological.filter_functions.egenhofer.EhOverlapFF;
import io.github.galbiston.geosparql_jena.implementation.vocabulary.Geo;
import io.github.galbiston.geosparql_jena.implementation.vocabulary.Geof;
import org.apache.jena.sparql.function.FunctionRegistry;
import org.apache.jena.sparql.pfunction.PropertyFunctionRegistry;

/**
 *
 *
 *
 */
public class Egenhofer {

    /**
     * This method loads all the Egenhofer Topological Property Functions as
     * well as the Query Rewrite Property Functions
     *
     * @param registry - the PropertyFunctionRegistry to be used
     */
    public static void loadPropertyFunctions(PropertyFunctionRegistry registry) {

        // Simple Feature Topological Property Functions
        registry.put(Geo.EH_CONTAINS_NAME, EhContainsPF.class);
        registry.put(Geo.EH_COVERED_BY_NAME, EhCoveredByPF.class);
        registry.put(Geo.EH_COVERS_NAME, EhCoversPF.class);
        registry.put(Geo.EH_DISJOINT_NAME, EhDisjointPF.class);
        registry.put(Geo.EH_EQUALS_NAME, EhEqualsPF.class);
        registry.put(Geo.EH_INSIDE_NAME, EhInsidePF.class);
        registry.put(Geo.EH_MEET_NAME, EhMeetPF.class);
        registry.put(Geo.EH_OVERLAP_NAME, EhOverlapPF.class);
    }

    /**
     * This method loads all the Egenhofer Topological Expression Functions
     *
     * @param functionRegistry
     */
    public static void loadFilterFunctions(FunctionRegistry functionRegistry) {

        functionRegistry.put(Geof.EH_CONTAINS, EhContainsFF.class);
        functionRegistry.put(Geof.EH_COVERED_BY, EhCoveredByFF.class);
        functionRegistry.put(Geof.EH_COVERS, EhCoversFF.class);
        functionRegistry.put(Geof.EH_DISJOINT, EhDisjointFF.class);
        functionRegistry.put(Geof.EH_EQUALS, EhEqualsFF.class);
        functionRegistry.put(Geof.EH_INSIDE, EhInsideFF.class);
        functionRegistry.put(Geof.EH_MEET, EhMeetFF.class);
        functionRegistry.put(Geof.EH_OVERLAP, EhOverlapFF.class);

    }
}
