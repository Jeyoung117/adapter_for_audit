/*
 * Copyright 2019 IBM All Rights Reserved.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.sslab.fabric.chaincodeshim.contract.routing.impl;

import org.sslab.fabric.chaincodeshim.Logger;
import org.sslab.fabric.chaincodeshim.contract.Context;
import org.sslab.fabric.chaincodeshim.contract.ContractInterface;
import org.sslab.fabric.chaincodeshim.contract.ContractRuntimeException;
import org.sslab.fabric.chaincodeshim.contract.annotation.Property;
import org.sslab.fabric.chaincodeshim.contract.annotation.Transaction;
import org.sslab.fabric.chaincodeshim.contract.metadata.TypeSchema;
import org.sslab.fabric.chaincodeshim.contract.routing.ContractDefinition;
import org.sslab.fabric.chaincodeshim.contract.routing.ParameterDefinition;
import org.sslab.fabric.chaincodeshim.contract.routing.TransactionType;
import org.sslab.fabric.chaincodeshim.contract.routing.TxFunction;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class TxFunctionImpl implements TxFunction {
    private static Logger logger = Logger.getLogger(TxFunctionImpl.class);

    private final Method method;
    private String name;
    private TransactionType type;
    private final Routing routing;
    private TypeSchema returnSchema;
    private List<ParameterDefinition> paramsList = new ArrayList<>();
    private boolean isUnknownTx;

    public static final class RoutingImpl implements Routing {

        private final Method method;
        private final Class<? extends ContractInterface> clazz;
        private static String serializerName;

        /**
         *
         * @param method
         * @param contract
         */
        public RoutingImpl(final Method method, final ContractDefinition contract) {
            this.method = method;
            this.clazz = contract.getContractImpl();
            this.serializerName = contract.getAnnotation().transactionSerializer();
        }

        @Override
        public Method getMethod() {
            return method;
        }

        @Override
        public Class<? extends ContractInterface> getContractClass() {
            return clazz;
        }

        @Override
        public ContractInterface getContractInstance() throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
            return clazz.getDeclaredConstructor().newInstance();
        }

        @Override
        public String toString() {
            return method.getName() + ":" + clazz.getCanonicalName();
        }

        @Override
        public String getSerializerName() {
            return serializerName;
        }

    }

    /**
     * New TxFunction Definition Impl.
     *
     * @param m        Reflect method object
     * @param contract ContractDefinition this is part of
     */
    public TxFunctionImpl(final Method m, final ContractDefinition contract) {

        this.method = m;
        if (m.getAnnotation(Transaction.class) != null) {
            logger.debug("Found Transaction method: " + m.getName());
            if (m.getAnnotation(Transaction.class).submit()) {
                this.type = TransactionType.INVOKE;
            } else {
                this.type = TransactionType.QUERY;
            }

            final String txnName = m.getAnnotation(Transaction.class).name();
            if (!txnName.isEmpty()) {
                this.name = txnName;
            }
        }

        if (name == null) {
            this.name = m.getName();
        }

        // create the routing object that defines how to get the data to the transaction
        // function.
        this.routing = new RoutingImpl(m, contract);

        // set the return schema
        this.returnSchema = TypeSchema.typeConvert(m.getReturnType());

        // parameter processing
        final List<java.lang.reflect.Parameter> params = new ArrayList<java.lang.reflect.Parameter>(
                Arrays.asList(method.getParameters()));

        // validate the first one is a context object
        if (!Context.class.isAssignableFrom(params.get(0).getType())) {
            throw new ContractRuntimeException(
                    "First argument should be of type Context " + method.getName() + " " + params.get(0).getType());
        } else {

            params.remove(0);
        }

        // FUTURE: if ever the method of creating the instance where to change,
        // the routing could be changed here, a different implementation could be made
        // here encapsulating the change. eg use an annotation to define where the
        // context goes

        for (final java.lang.reflect.Parameter parameter : params) {
            final TypeSchema paramMap = new TypeSchema();
            final TypeSchema schema = TypeSchema.typeConvert(parameter.getType());

            final Property annotation = parameter.getAnnotation(Property.class);
            if (annotation != null) {
                final String[] userSupplied = annotation.schema();
                for (int i = 0; i < userSupplied.length; i += 2) {
                    schema.put(userSupplied[i], userSupplied[i + 1]);
                }
            }

            paramMap.put("name", parameter.getName());
            paramMap.put("schema", schema);
            final ParameterDefinition pd = new ParameterDefinitionImpl(parameter.getName(), parameter.getClass(), paramMap,
                    parameter);
            paramsList.add(pd);
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Routing getRouting() {
        return this.routing;
    }

    @Override
    public Class<?> getReturnType() {
        return method.getReturnType();
    }

    @Override
    public java.lang.reflect.Parameter[] getParameters() {
        return method.getParameters();
    }

    @Override
    public TransactionType getType() {
        return this.type;
    }

    @Override
    public String toString() {
        return name + " @" + Integer.toHexString(System.identityHashCode(this));
    }

    @Override
    public void setReturnSchema(final TypeSchema returnSchema) {
        this.returnSchema = returnSchema;
    }

    @Override
    public List<ParameterDefinition> getParamsList() {
        return paramsList;
    }

    /**
     *
     * @param paramsList
     */
    public void setParamsList(final ArrayList<ParameterDefinition> paramsList) {
        this.paramsList = paramsList;
    }

    @Override
    public TypeSchema getReturnSchema() {
        return returnSchema;
    }

    @Override
    public void setParameterDefinitions(final List<ParameterDefinition> list) {
        this.paramsList = list;

    }

    @Override
    public boolean isUnknownTx() {
        return isUnknownTx;
    }

    @Override
    public void setUnknownTx(final boolean unknown) {
        this.isUnknownTx = unknown;
    }

}
