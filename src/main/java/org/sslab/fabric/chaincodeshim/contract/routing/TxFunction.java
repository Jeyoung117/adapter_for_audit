/*
 * Copyright 2019 IBM All Rights Reserved.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.sslab.fabric.chaincodeshim.contract.routing;

import org.sslab.fabric.chaincodeshim.contract.ContractInterface;
import org.sslab.fabric.chaincodeshim.contract.metadata.TypeSchema;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public interface TxFunction {

    interface Routing {

        Method getMethod();

        Class<? extends ContractInterface> getContractClass();

        ContractInterface getContractInstance() throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException;

        String getSerializerName();
    }

    /**
     * @return is this tx to be called when request fn is unknown
     */
    boolean isUnknownTx();

    /**
     * @param unknown
     */
    void setUnknownTx(boolean unknown);

    /**
     * @return Name
     */
    String getName();

    /**
     * @return Routing object
     */
    Routing getRouting();

    /**
     * @return Class of the return type
     */
    Class<?> getReturnType();

    /**
     * @return Parameter array
     */
    java.lang.reflect.Parameter[] getParameters();

    /**
     * @return Submit or Evaluate
     */
    TransactionType getType();

    /**
     * @param returnSchema
     */
    void setReturnSchema(TypeSchema returnSchema);

    /**
     * @return TypeSchema of the return type
     */
    TypeSchema getReturnSchema();

    /**
     * @param list
     */
    void setParameterDefinitions(List<ParameterDefinition> list);

    /**
     * @return List of parameters
     */
    List<ParameterDefinition> getParamsList();

}
