/*
 * Copyright 2019 IBM DTCC All Rights Reserved.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.sslab.adapter.chaincodeshim.shim.impl;

import com.google.protobuf.ByteString;
import org.hyperledger.fabric.protos.peer.Chaincode.ChaincodeID;
import org.hyperledger.fabric.protos.peer.ChaincodeEventPackage.ChaincodeEvent;
import org.hyperledger.fabric.protos.peer.ChaincodeShim.*;
import org.hyperledger.fabric.protos.peer.ChaincodeShim.ChaincodeMessage.Type;
import org.hyperledger.fabric.protos.peer.ProposalResponsePackage.Response;
import org.hyperledger.fabric.protos.peer.ProposalResponsePackage.Response.Builder;
import org.sslab.adapter.chaincodeshim.shim.Chaincode;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.hyperledger.fabric.protos.peer.ChaincodeShim.ChaincodeMessage.Type.*;

public final class ChaincodeMessageFactory {

    private ChaincodeMessageFactory() {
    }

    protected static ChaincodeMessage newGetPrivateDataHashEventMessage(final String channelId, final String txId, final String collection, final String key) {
        return newEventMessage(GET_PRIVATE_DATA_HASH, channelId, txId, GetState.newBuilder().setCollection(collection).setKey(key).build().toByteString());
    }

    protected static ChaincodeMessage newGetStateEventMessage(final String channelId, final String txId, final String collection, final String key) {
        return newEventMessage(GET_STATE, channelId, txId, GetState.newBuilder().setCollection(collection).setKey(key).build().toByteString());
    }

    protected static ChaincodeMessage newGetStateMetadataEventMessage(final String channelId, final String txId, final String collection, final String key) {
        return newEventMessage(GET_STATE_METADATA, channelId, txId, GetStateMetadata.newBuilder().setCollection(collection).setKey(key).build().toByteString());
    }

    protected static ChaincodeMessage newPutStateEventMessage(final String channelId, final String txId, final String collection, final String key,
            final ByteString value) {
        return newEventMessage(PUT_STATE, channelId, txId, PutState.newBuilder().setCollection(collection).setKey(key).setValue(value).build().toByteString());
    }

    protected static ChaincodeMessage newPutStateMetadataEventMessage(final String channelId, final String txId, final String collection, final String key,
            final String metakey, final ByteString value) {
        return newEventMessage(PUT_STATE_METADATA, channelId, txId, PutStateMetadata.newBuilder().setCollection(collection).setKey(key)
                .setMetadata(StateMetadata.newBuilder().setMetakey(metakey).setValue(value).build()).build().toByteString());
    }

    protected static ChaincodeMessage newDeleteStateEventMessage(final String channelId, final String txId, final String collection, final String key) {
        return newEventMessage(DEL_STATE, channelId, txId, DelState.newBuilder().setCollection(collection).setKey(key).build().toByteString());
    }

    protected static ChaincodeMessage newErrorEventMessage(final String channelId, final String txId, final Throwable throwable) {
        return newErrorEventMessage(channelId, txId, printStackTrace(throwable));
    }

    protected static ChaincodeMessage newErrorEventMessage(final String channelId, final String txId, final String message) {
        return newErrorEventMessage(channelId, txId, message, null);
    }

    protected static ChaincodeMessage newErrorEventMessage(final String channelId, final String txId, final String message, final ChaincodeEvent event) {
        return newEventMessage(ERROR, channelId, txId, ByteString.copyFromUtf8(message), event);
    }

    protected static ChaincodeMessage newCompletedEventMessage(final String channelId, final String txId, final Chaincode.Response response,
            final ChaincodeEvent event) {
        final ChaincodeMessage message = newEventMessage(COMPLETED, channelId, txId, toProtoResponse(response).toByteString(), event);
        return message;
    }

    protected static ChaincodeMessage newInvokeChaincodeMessage(final String channelId, final String txId, final ByteString payload) {
        return newEventMessage(INVOKE_CHAINCODE, channelId, txId, payload, null);
    }

    protected static ChaincodeMessage newRegisterChaincodeMessage(final ChaincodeID chaincodeId) {
        return ChaincodeMessage.newBuilder().setType(REGISTER).setPayload(chaincodeId.toByteString()).build();
    }

    protected static ChaincodeMessage newEventMessage(final Type type, final String channelId, final String txId, final ByteString payload) {
        return newEventMessage(type, channelId, txId, payload, null);
    }

    protected static ChaincodeMessage newEventMessage(final Type type, final String channelId, final String txId, final ByteString payload,
            final ChaincodeEvent event) {
        final ChaincodeMessage.Builder builder = ChaincodeMessage.newBuilder().setType(type).setChannelId(channelId).setTxid(txId).setPayload(payload);
        if (event != null) {
            builder.setChaincodeEvent(event);
        }
        return builder.build();
    }

    private static Response toProtoResponse(final Chaincode.Response response) {
        final Builder builder = Response.newBuilder();
        builder.setStatus(response.getStatus().getCode());
        if (response.getMessage() != null) {
            builder.setMessage(response.getMessage());
        }
        if (response.getPayload() != null) {
            builder.setPayload(ByteString.copyFrom(response.getPayload()));
        }
        return builder.build();
    }

    private static String printStackTrace(final Throwable throwable) {
        if (throwable == null) {
            return null;
        }
        final StringWriter buffer = new StringWriter();
        throwable.printStackTrace(new PrintWriter(buffer));
        return buffer.toString();
    }
}
