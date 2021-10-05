/*
 * Copyright 2019 IBM All Rights Reserved.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.sslab.fabric.chaincodeshim.contract.routing;

import org.sslab.fabric.chaincodeshim.contract.ContractInterface;
import org.sslab.fabric.chaincodeshim.contract.annotation.Contract;

import java.lang.reflect.Method;
import java.util.Collection;

/**
 * Definition of the Contract
 *
 * A data structure that represents the contract that will be executed in the
 * chaincode. Primarily has
 *
 * Name - either defined by the Contract annotation or the Class name (can be
 * referred to as Namespace) Default - is the default contract (defined by the
 * Default annotation) TxFunctions in this contract do not need the name prefix
 * when invoked TxFunctions - the transaction functions defined in this contract
 *
 * Will embedded the ContractInterface instance, as well as the annotation
 * itself, and the routing for any tx function that is unknown
 *
 */
public interface ContractDefinition {

    /**
     * @return the fully qualified name of the Contract
     */
    String getName();

    /**
     * @return Complete collection of all the transaction functions in this contract
     */
    Collection<TxFunction> getTxFunctions();

    /**
     * @return Object reference to the instantiated object that is 'the contract'
     */
    Class<? extends ContractInterface> getContractImpl();

    /**
     * @param m The java.lang.reflect object that is the method that is a tx
     *          function
     * @return TxFunction object representing this method
     */
    TxFunction addTxFunction(Method m);

    /**
     *
     * @return if this is contract is the default one or not
     */
    boolean isDefault();

    /**
     *
     * @param method name to be returned
     * @return TxFunction that represents this requested method
     */
    TxFunction getTxFunction(String method);

    /**
     *
     * @param method name to be checked
     * @return true if this txFunction exists or not
     */
    boolean hasTxFunction(String method);

    /**
     * @return The TxFunction to be used for this contract in case of unknown
     *         request
     */
    TxFunction getUnknownRoute();

    /**
     * @return Underlying raw annotation
     */
    Contract getAnnotation();
}
