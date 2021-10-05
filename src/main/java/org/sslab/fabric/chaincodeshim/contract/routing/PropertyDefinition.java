/*
 * Copyright 2019 IBM All Rights Reserved.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.sslab.fabric.chaincodeshim.contract.routing;

import org.sslab.fabric.chaincodeshim.contract.metadata.TypeSchema;

import java.lang.reflect.Field;

public interface PropertyDefinition {

    /**
     * @return Class of the Property
     */
    Class<?> getTypeClass();

    /**
     * @return TypeSchema
     */
    TypeSchema getSchema();

    /**
     * @return Field
     */
    Field getField();

    /**
     * @return Name of the property
     */
    String getName();

}
