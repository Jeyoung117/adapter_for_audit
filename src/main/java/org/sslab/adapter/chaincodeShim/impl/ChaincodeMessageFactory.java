package org.sslab.adapter.chaincodeShim.impl;

import static org.hyperledger.fabric.protos.corfu.CorfuChaincodeShim.CorfuChaincodeMessage.Type.COMPLETED;
import static org.hyperledger.fabric.protos.corfu.CorfuChaincodeShim.CorfuChaincodeMessage.Type.DEL_STATE;
import static org.hyperledger.fabric.protos.corfu.CorfuChaincodeShim.CorfuChaincodeMessage.Type.ERROR;
import static org.hyperledger.fabric.protos.corfu.CorfuChaincodeShim.CorfuChaincodeMessage.Type.GET_PRIVATE_DATA_HASH;
import static org.hyperledger.fabric.protos.corfu.CorfuChaincodeShim.CorfuChaincodeMessage.Type.GET_STATE;
import static org.hyperledger.fabric.protos.corfu.CorfuChaincodeShim.CorfuChaincodeMessage.Type.GET_STATE_METADATA;
import static org.hyperledger.fabric.protos.corfu.CorfuChaincodeShim.CorfuChaincodeMessage.Type.INVOKE_CHAINCODE;
import static org.hyperledger.fabric.protos.corfu.CorfuChaincodeShim.CorfuChaincodeMessage.Type.PUT_STATE;
import static org.hyperledger.fabric.protos.corfu.CorfuChaincodeShim.CorfuChaincodeMessage.Type.PUT_STATE_METADATA;
import static org.hyperledger.fabric.protos.corfu.CorfuChaincodeShim.CorfuChaincodeMessage.Type.REGISTER;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.hyperledger.fabric.protos.corfu.CorfuChaincodeShim;
import org.hyperledger.fabric.protos.peer.Chaincode.ChaincodeID;
import org.hyperledger.fabric.protos.peer.ChaincodeEventPackage.ChaincodeEvent;
//import org.hyperledger.fabric.protos.peer.ChaincodeShim.ChaincodeMessage;
//import org.hyperledger.fabric.protos.peer.ChaincodeShim.ChaincodeMessage.Type;
import org.hyperledger.fabric.protos.corfu.CorfuChaincodeShim.CorfuChaincodeMessage.Type;
import org.hyperledger.fabric.protos.corfu.CorfuChaincodeShim.CorfuChaincodeMessage;
import org.hyperledger.fabric.protos.corfu.CorfuChaincodeShim;
import org.hyperledger.fabric.protos.peer.ChaincodeShim.DelState;
import org.hyperledger.fabric.protos.peer.ChaincodeShim.GetState;
import org.hyperledger.fabric.protos.peer.ChaincodeShim.GetStateMetadata;
import org.hyperledger.fabric.protos.peer.ChaincodeShim.PutState;
import org.hyperledger.fabric.protos.peer.ChaincodeShim.PutStateMetadata;
import org.hyperledger.fabric.protos.peer.ChaincodeShim.StateMetadata;
import org.hyperledger.fabric.protos.peer.ProposalResponsePackage.Response;
import org.hyperledger.fabric.protos.peer.ProposalResponsePackage.Response.Builder;
import org.hyperledger.fabric.shim.Chaincode;

import com.google.protobuf.ByteString;

public final class ChaincodeMessageFactory {

    private ChaincodeMessageFactory() {
    }

    protected static CorfuChaincodeShim.CorfuChaincodeMessage newGetPrivateDataHashEventMessage(final String channelId, final String txId, final String collection, final String key) {
        return newEventMessage(GET_PRIVATE_DATA_HASH, channelId, txId, CorfuChaincodeShim.CorfuGetState.newBuilder().setCollection(collection).setKey(key).build().toByteString());
    }

    public static CorfuChaincodeShim.CorfuChaincodeMessage newGetStateEventMessage(final String channelId, final String txId, final String collection, final String key) {
        return newEventMessage(GET_STATE, channelId, txId, CorfuChaincodeShim.CorfuGetState.newBuilder().setCollection(collection).setKey(key).build().toByteString());
    }

    protected static CorfuChaincodeShim.CorfuChaincodeMessage newGetStateMetadataEventMessage(final String channelId, final String txId, final String collection, final String key) {
        return newEventMessage(GET_STATE_METADATA, channelId, txId, CorfuChaincodeShim.CorfuGetStateMetadata.newBuilder().setCollection(collection).setKey(key).build().toByteString());
    }

    protected static CorfuChaincodeShim.CorfuChaincodeMessage newPutStateEventMessage(final String channelId, final String txId, final String collection, final String key,
                                                              final ByteString value) {
        return newEventMessage(PUT_STATE, channelId, txId, CorfuChaincodeShim.CorfuPutState.newBuilder().setCollection(collection).setKey(key).setValue(value).build().toByteString());
    }

    protected static CorfuChaincodeShim.CorfuChaincodeMessage newPutStateMetadataEventMessage(final String channelId, final String txId, final String collection, final String key,
                                                                      final String metakey, final ByteString value) {
        return newEventMessage(PUT_STATE_METADATA, channelId, txId, CorfuChaincodeShim.CorfuPutStateMetadata.newBuilder().setCollection(collection).setKey(key)
                .setMetadata(CorfuChaincodeShim.CorfuStateMetadata.newBuilder().setMetakey(metakey).setValue(value).build()).build().toByteString());
    }

    protected static CorfuChaincodeShim.CorfuChaincodeMessage newDeleteStateEventMessage(final String channelId, final String txId, final String collection, final String key) {
        return newEventMessage(DEL_STATE, channelId, txId, CorfuChaincodeShim.CorfuDelState.newBuilder().setCollection(collection).setKey(key).build().toByteString());
    }

    protected static CorfuChaincodeShim.CorfuChaincodeMessage newErrorEventMessage(final String channelId, final String txId, final Throwable throwable) {
        return newErrorEventMessage(channelId, txId, printStackTrace(throwable));
    }

    protected static CorfuChaincodeShim.CorfuChaincodeMessage newErrorEventMessage(final String channelId, final String txId, final String message) {
        return newErrorEventMessage(channelId, txId, message, null);
    }

    protected static CorfuChaincodeShim.CorfuChaincodeMessage newErrorEventMessage(final String channelId, final String txId, final String message, final ChaincodeEvent event) {
        return newEventMessage(ERROR, channelId, txId, ByteString.copyFromUtf8(message), event);
    }

    protected static CorfuChaincodeShim.CorfuChaincodeMessage newCompletedEventMessage(final String channelId, final String txId, final Chaincode.Response response,
                                                               final ChaincodeEvent event) {
        final CorfuChaincodeShim.CorfuChaincodeMessage message = newEventMessage(COMPLETED, channelId, txId, toProtoResponse(response).toByteString(), event);
        return message;
    }

    protected static CorfuChaincodeShim.CorfuChaincodeMessage newInvokeChaincodeMessage(final String channelId, final String txId, final ByteString payload) {
        return newEventMessage(INVOKE_CHAINCODE, channelId, txId, payload, null);
    }

    protected static CorfuChaincodeShim.CorfuChaincodeMessage newRegisterChaincodeMessage(final ChaincodeID chaincodeId) {
        return CorfuChaincodeShim.CorfuChaincodeMessage.newBuilder().setType(REGISTER).setPayload(chaincodeId.toByteString()).build();
    }

    protected static CorfuChaincodeShim.CorfuChaincodeMessage newEventMessage(final Type type, final String channelId, final String txId, final ByteString payload) {
        return newEventMessage(type, channelId, txId, payload, null);
    }

    protected static CorfuChaincodeShim.CorfuChaincodeMessage newEventMessage(final Type type, final String channelId, final String txId, final ByteString payload,
                                                      final ChaincodeEvent event) {
        final CorfuChaincodeShim.CorfuChaincodeMessage.Builder builder = CorfuChaincodeShim.CorfuChaincodeMessage.newBuilder().setType(type).setChannelId(channelId).setTxid(txId).setPayload(payload);
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