/*
 * Copyright 2019 IBM All Rights Reserved.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.sslab.fabric.chaincodeshim.shim.impl;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import org.hyperledger.fabric.protos.peer.ChaincodeShim;
import org.hyperledger.fabric.protos.peer.ChaincodeShim.QueryResponse;
import org.hyperledger.fabric.protos.peer.ChaincodeShim.QueryResultBytes;
import org.sslab.fabric.chaincodeshim.shim.ledger.QueryResultsIteratorWithMetadata;

import java.util.function.Function;
import java.util.logging.Logger;

/**
 * QueryResult Iterator.
 *
 * Implementation of {@link QueryResultsIteratorWithMetadata}, by extending
 * {@link QueryResultsIterator}
 * implementations, {@link QueryResultsIteratorImpl}
 *
 * @param <T>
 */
public final class QueryResultsIteratorWithMetadataImpl<T> extends QueryResultsIteratorImpl<T> implements QueryResultsIteratorWithMetadata<T> {
    private static Logger logger = Logger.getLogger(QueryResultsIteratorWithMetadataImpl.class.getName());

    private ChaincodeShim.QueryResponseMetadata metadata;

    /**
     *
     * @param handler
     * @param channelId
     * @param txId
     * @param responseBuffer
     * @param mapper
     */
    public QueryResultsIteratorWithMetadataImpl(final ChaincodeInvocationTask handler, final String channelId, final String txId,
            final ByteString responseBuffer, final Function<QueryResultBytes, T> mapper) {
        super(handler, channelId, txId, responseBuffer, mapper);
        try {
            final QueryResponse queryResponse = QueryResponse.parseFrom(responseBuffer);
            metadata = ChaincodeShim.QueryResponseMetadata.parseFrom(queryResponse.getMetadata());
        } catch (final InvalidProtocolBufferException e) {
            logger.warning("can't parse response metadata");
            throw new RuntimeException(e);
        }
    }

    @Override
    public ChaincodeShim.QueryResponseMetadata getMetadata() {
        return metadata;
    }
}
