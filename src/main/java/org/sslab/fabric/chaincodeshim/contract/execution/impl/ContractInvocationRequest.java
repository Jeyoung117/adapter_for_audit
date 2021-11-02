/*
 * Copyright 2019 IBM DTCC All Rights Reserved.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.sslab.fabric.chaincodeshim.contract.execution.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sslab.fabric.chaincodeshim.contract.execution.InvocationRequest;
import org.sslab.fabric.chaincodeshim.shim.ChaincodeStub;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ContractInvocationRequest implements InvocationRequest {
    private String namespace;
    private String method;
    private List<byte[]> args = Collections.emptyList();

    private static Log logger = LogFactory.getLog(ContractInvocationRequest.class);

    /**
     * @param context
     */
    public ContractInvocationRequest(final ChaincodeStub context) {
        final String func = context.getStringArgs().size() > 0 ? context.getStringArgs().get(0) : null;
        logger.info(new StringBuilder().append("in ContractInvocationRequest func is ").append(func).toString());
//        logger.fine(() -> "Namespace is " + request.getNamespace());
        final String[] funcParts = func.split(":");
        logger.info("in ContractInvocationRequest funcParts is " + funcParts);
        logger.debug(func);
        if (funcParts.length == 2) {
            namespace = funcParts[0];
            method = funcParts[1];
        } else {
            namespace = context.getChaincodeId();
            logger.info("in ContractInvocationRequest namespace is " + namespace);
            method = funcParts[0];
        }

        args = context.getArgs().stream().skip(1).collect(Collectors.toList());
        logger.info(namespace + " " + method + " " + args);
    }

    /**
     *
     */
    @Override
    public String getNamespace() {
        return namespace;
    }

    /**
     *
     */
    @Override
    public String getMethod() {
        return method;
    }

    /**
     *
     */
    @Override
    public List<byte[]> getArgs() {
        return args;
    }

    /**
     *
     */
    @Override
    public String getRequestName() {
        return namespace + ":" + method;
    }

    /**
     *
     */
    @Override
    public String toString() {
        return namespace + ":" + method + " @" + Integer.toHexString(System.identityHashCode(this));
    }

}
