/*
 * Copyright 2019 IBM DTCC All Rights Reserved.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.sslab.fabric.chaincodeshim.contract.execution;

import org.sslab.fabric.chaincodeshim.contract.routing.TxFunction;
import org.sslab.fabric.chaincodeshim.shim.Chaincode;
import org.sslab.fabric.chaincodeshim.shim.ChaincodeStub;

/**
 * ExecutionService.
 *
 * Service that executes {@link InvocationRequest} (wrapped Init/Invoke + extra
 * data) using routing information
 */
public interface ExecutionService {

    /**
     *
     * @param txFn
     * @param req
     * @param stub
     * @return Chaincode response
     */
    Chaincode.Response executeRequest(TxFunction txFn, InvocationRequest req, ChaincodeStub stub);
}
