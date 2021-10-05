/*
 * Copyright 2019 IBM All Rights Reserved.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.sslab.adapter.chaincodeshim.contract.routing;

import org.sslab.adapter.chaincodeshim.contract.metadata.TypeSchema;

import java.lang.reflect.Parameter;

public interface ParameterDefinition {

    /**
     * @return Class type of the parameter
     */
    Class<?> getTypeClass();

    /**
     * @return TypeSchema of the parameter
     */
    TypeSchema getSchema();

    /**
     * @return Parameter
     */
    Parameter getParameter();

    /**
     * @return name of the parameter
     */
    String getName();

}
