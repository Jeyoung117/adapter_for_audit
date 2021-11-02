/*
 * Copyright 2019 IBM All Rights Reserved.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.sslab.fabric.chaincodeshim.contract.execution.impl;

import org.sslab.fabric.chaincodeshim.contract.Context;
import org.sslab.fabric.chaincodeshim.contract.ContractInterface;
import org.sslab.fabric.chaincodeshim.contract.ContractRuntimeException;
import org.sslab.fabric.chaincodeshim.contract.execution.ExecutionService;
import org.sslab.fabric.chaincodeshim.contract.execution.InvocationRequest;
import org.sslab.fabric.chaincodeshim.contract.execution.SerializerInterface;
import org.sslab.fabric.chaincodeshim.contract.metadata.TypeSchema;
import org.sslab.fabric.chaincodeshim.contract.routing.ParameterDefinition;
import org.sslab.fabric.chaincodeshim.contract.routing.TxFunction;
import org.sslab.fabric.chaincodeshim.shim.Chaincode;
import org.sslab.fabric.chaincodeshim.shim.ChaincodeException;
import org.sslab.fabric.chaincodeshim.shim.ChaincodeStub;
import org.sslab.fabric.chaincodeshim.shim.ResponseUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Logger;

public class ContractExecutionService implements ExecutionService {

    private static Logger logger = Logger.getLogger(ContractExecutionService.class.getName());

    private final SerializerInterface serializer;
    private Map<String, Object> proxies = new HashMap<>();

    /**
     * @param serializer
     */
    public ContractExecutionService(final SerializerInterface serializer) {
        this.serializer = serializer;
    }

    /**
     *
     */
    @Override
    public Chaincode.Response executeRequest(final TxFunction txFn, final InvocationRequest req, final ChaincodeStub stub) {
        logger.info(() -> "Routing Request " + txFn);
        final TxFunction.Routing rd = txFn.getRouting();
        Chaincode.Response response;

        try {
            final ContractInterface contractObject = rd.getContractInstance();
            final Context context = contractObject.createContext(stub);
            final Object[] stubArgs = stub.getParameters().toArray();
            logger.info(() -> "여기일세" + stubArgs[0]);
            final List<Object> args = new ArrayList<>(stubArgs.length + 1); // allow for context as the first argument
            for (int i = 0; i < stubArgs.length; i++) {
                args.add(i, stubArgs[0]);
            }
            args.add(0, context); // force context into 1st position, other elements move up

            contractObject.beforeTransaction(context);
            final Object value = rd.getMethod().invoke(contractObject, args.toArray());
            contractObject.afterTransaction(context, value);

            if (value == null) {
                response = ResponseUtils.newSuccessResponse();
            } else {
                response = ResponseUtils.newSuccessResponse(value.toString().getBytes());
            }

        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException e) {
            final String message = String.format("Could not execute contract method: %s", rd.toString());
            throw new ContractRuntimeException(message, e);
        } catch (final InvocationTargetException e) {
            final Throwable cause = e.getCause();

            if (cause instanceof ChaincodeException) {
                throw (ChaincodeException) cause;
            } else {
                throw new ContractRuntimeException("Error during contract method execution", cause);
            }
        }

        return response;
    }

    private byte[] convertReturn(final Object obj) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(bos);
        out.writeObject(obj);
        out.flush();
        out.close();
        bos.close();
        byte[] yourBytes = bos.toByteArray();

        return yourBytes;
    }

    private List<Object> convertArgs(final List<byte[]> stubArgs, final TxFunction txFn) {

        final List<ParameterDefinition> schemaParams = txFn.getParamsList();
        final List<Object> args = new ArrayList<>(stubArgs.size() + 1); // allow for context as the first argument
        for (int i = 0; i < schemaParams.size(); i++) {
            args.add(i, serializer.fromBuffer(stubArgs.get(i), schemaParams.get(i).getSchema()));
        }
        return args;
    }

}
