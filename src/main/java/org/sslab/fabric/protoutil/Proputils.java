package org.sslab.fabric.protoutil;

import com.google.protobuf.ByteString;
import org.hyperledger.fabric.protos.peer.Chaincode;
import org.hyperledger.fabric.protos.peer.ProposalPackage;
import org.hyperledger.fabric.protos.peer.ProposalResponsePackage;

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
}
