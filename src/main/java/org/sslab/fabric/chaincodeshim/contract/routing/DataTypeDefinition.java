/*
 * Copyright 2019 IBM All Rights Reserved.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.sslab.fabric.chaincodeshim.contract.routing;

import org.sslab.fabric.chaincodeshim.contract.metadata.TypeSchema;

import java.util.Map;

public interface DataTypeDefinition {

    /**
     * @return String
     */
    String getName();

    /**
     * @return Map of String to PropertyDefinitions
     */
    Map<String, PropertyDefinition> getProperties();

    /**
     * @return String
     */
    String getSimpleName();

    /**
     * @return Class object of the type
     */
    Class<?> getTypeClass();

    /**
     * @return TypeSchema
     */
    TypeSchema getSchema();
}
