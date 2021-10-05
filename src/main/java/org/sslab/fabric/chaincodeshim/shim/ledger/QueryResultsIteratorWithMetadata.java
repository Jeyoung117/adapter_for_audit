/*
 * Copyright 2019 IBM All Rights Reserved.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.sslab.fabric.chaincodeshim.shim.ledger;

import org.hyperledger.fabric.protos.peer.ChaincodeShim;

/**
 * QueryResultsIteratorWithMetadata allows a chaincode to iterate over a set of
 * key/value pairs returned by range, execute and history queries. In addition,
 * it store
 * {@link ChaincodeShim.QueryResponseMetadata},
 * returned by pagination range queries
 *
 * @param <T> the type of elements returned by the iterator
 */
public interface QueryResultsIteratorWithMetadata<T> extends Iterable<T>, AutoCloseable {
    /**
     *
     * @return Query Metadata
     */
    ChaincodeShim.QueryResponseMetadata getMetadata();
}
