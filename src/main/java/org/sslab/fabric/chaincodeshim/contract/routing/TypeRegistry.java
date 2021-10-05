/*
 * Copyright 2019 IBM All Rights Reserved.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.sslab.fabric.chaincodeshim.contract.routing;

import org.sslab.fabric.chaincodeshim.contract.metadata.TypeSchema;
import org.sslab.fabric.chaincodeshim.contract.routing.impl.TypeRegistryImpl;

import java.util.Collection;

public interface TypeRegistry {

    /**
     * @return TypeRegistry
     */
    static TypeRegistry getRegistry() {
        return TypeRegistryImpl.getInstance();
    }

    /**
     * @param dtd
     */
    void addDataType(DataTypeDefinition dtd);

    /**
     * @param cl
     */
    void addDataType(Class<?> cl);

    /**
     * @param name
     * @return DataTypeDefinition
     */
    DataTypeDefinition getDataType(String name);

    /**
     * @param schema
     * @return DataTypeDefinition
     */
    DataTypeDefinition getDataType(TypeSchema schema);

    /**
     * @return All datatypes
     */
    Collection<DataTypeDefinition> getAllDataTypes();

}
