package org.sslab.fabric.protoutil;

import bsp_transaction.BspTransactionOuterClass;
import com.google.protobuf.ByteString;
import org.hyperledger.fabric.protos.peer.Chaincode;
import org.hyperledger.fabric.protos.peer.ProposalPackage;
import org.hyperledger.fabric.protos.peer.ProposalResponsePackage;
import org.hyperledger.fabric.sdk.User;
import org.hyperledger.fabric.sdk.identity.SigningIdentity;

public class Proputils {
    public ByteString getBytesProposalResponsePayload(ByteString hash, ProposalResponsePackage.Response response , ByteString result , ByteString event, Chaincode.ChaincodeID ccid) {
        ProposalPackage.ChaincodeAction cAct = ProposalPackage.ChaincodeAction
                .newBuilder()
                .setEvents(event)
                .setResults(result)
                .setResponse(response)
                .setChaincodeId(ccid)
                .build();

        ByteString cActBytes = cAct.toByteString();
        ProposalResponsePackage.ProposalResponsePayload prp = ProposalResponsePackage.ProposalResponsePayload
                .newBuilder()
                .setExtension(cActBytes)
                .setProposalHash(hash)
                .build();

        ByteString prpBytes = prp.toByteString();
        return prpBytes;
    }

    public static BspTransactionOuterClass.SubmitResponse toProtoResponse(final org.sslab.fabric.chaincodeshim.shim.Chaincode.Response response) {
        final BspTransactionOuterClass.SubmitResponse.Builder builder = BspTransactionOuterClass.SubmitResponse.newBuilder();
        builder.setStatus(response.getStatus().getCode());
        if (response.getMessage() != null) {
            builder.setMessage(response.getMessage());
        }
        if (response.getPayload() != null) {
            builder.setPayload(ByteString.copyFrom(response.getPayload()));
        }
        return builder.build();
    }

    public static void SignPayloadWithSigner(BspTransactionOuterClass.Proposal signedProposal, ByteString payload) {

    }
}
