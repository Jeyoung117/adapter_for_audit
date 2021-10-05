/*
 * Copyright 2019 IBM All Rights Reserved.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.sslab.fabric.chaincodeshim.contract.routing.impl;

import org.sslab.fabric.chaincodeshim.contract.metadata.TypeSchema;
import org.sslab.fabric.chaincodeshim.contract.routing.PropertyDefinition;

import java.lang.reflect.Field;

public final class PropertyDefinitionImpl implements PropertyDefinition {

    private final Class<?> typeClass;
    private final TypeSchema schema;
    private final Field field;
    private final String name;

    /**
     *
     * @param name
     * @param typeClass
     * @param schema
     * @param f
     */
    public PropertyDefinitionImpl(final String name, final Class<?> typeClass, final TypeSchema schema, final Field f) {
        this.typeClass = typeClass;
        this.schema = schema;
        this.field = f;
        this.name = name;
    }

    @Override
    public Class<?> getTypeClass() {
        return this.typeClass;
    }

    @Override
    public TypeSchema getSchema() {
        return this.schema;
    }

    @Override
    public Field getField() {
        return this.field;
    }

    @Override
    public String getName() {
        return this.name;
    }

}
