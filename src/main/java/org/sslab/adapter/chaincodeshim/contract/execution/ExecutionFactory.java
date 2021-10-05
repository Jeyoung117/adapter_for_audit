/*
 * Copyright 2019 IBM All Rights Reserved.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.sslab.adapter.chaincodeshim.contract.execution;

import org.sslab.adapter.chaincodeshim.contract.execution.impl.ContractExecutionService;
import org.sslab.adapter.chaincodeshim.contract.execution.impl.ContractInvocationRequest;
import org.sslab.adapter.chaincodeshim.shim.ChaincodeStub;

public class ExecutionFactory {
    private static ExecutionFactory rf;
    private static ExecutionService es;

    /**
     * @return ExecutionFactory
     */
    public static ExecutionFactory getInstance() {
        if (rf == null) {
            rf = new ExecutionFactory();
        }
        return rf;
    }

    /**
     * @param context Chaincode Context
     * @return Invocation request
     */
    public InvocationRequest createRequest(final ChaincodeStub context) {
        return new ContractInvocationRequest(context);
    }

    /**
     * @param serializers Instance of the serializer
     * @return Execution Service
     */
    public ExecutionService createExecutionService(final SerializerInterface serializers) {
        if (es == null) {
            es = new ContractExecutionService(serializers);
        }
        return es;
    }
}
