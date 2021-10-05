/*
 * Copyright 2019 IBM All Rights Reserved.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.sslab.adapter.chaincodeshim.contract.execution.impl;

import org.sslab.adapter.chaincodeshim.contract.Context;
import org.sslab.adapter.chaincodeshim.contract.ContractInterface;
import org.sslab.adapter.chaincodeshim.contract.ContractRuntimeException;
import org.sslab.adapter.chaincodeshim.contract.execution.ExecutionService;
import org.sslab.adapter.chaincodeshim.contract.execution.InvocationRequest;
import org.sslab.adapter.chaincodeshim.contract.execution.SerializerInterface;
import org.sslab.adapter.chaincodeshim.contract.metadata.TypeSchema;
import org.sslab.adapter.chaincodeshim.contract.routing.ParameterDefinition;
import org.sslab.adapter.chaincodeshim.contract.routing.TxFunction;
import org.sslab.adapter.chaincodeshim.shim.Chaincode;
import org.sslab.adapter.chaincodeshim.shim.ChaincodeException;
import org.sslab.adapter.chaincodeshim.shim.ChaincodeStub;
import org.sslab.adapter.chaincodeshim.shim.ResponseUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        logger.fine(() -> "Routing Request" + txFn);
        final TxFunction.Routing rd = txFn.getRouting();
        Chaincode.Response response;

        try {
            final ContractInterface contractObject = rd.getContractInstance();
            final Context context = contractObject.createContext(stub);

            final List<Object> args = convertArgs(req.getArgs(), txFn);
            args.add(0, context); // force context into 1st position, other elements move up

            contractObject.beforeTransaction(context);
            final Object value = rd.getMethod().invoke(contractObject, args.toArray());
            contractObject.afterTransaction(context, value);

            if (value == null) {
                response = ResponseUtils.newSuccessResponse();
            } else {
                response = ResponseUtils.newSuccessResponse(convertReturn(value, txFn));
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

    private byte[] convertReturn(final Object obj, final TxFunction txFn) {
        byte[] buffer;
        final TypeSchema ts = txFn.getReturnSchema();
        buffer = serializer.toBuffer(obj, ts);

        return buffer;
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
