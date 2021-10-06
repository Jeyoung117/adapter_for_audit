package org.sslab.fabric.adapter;

import org.hyperledger.fabric.protos.common.Common;
import org.hyperledger.fabric.protos.peer.*;
import org.hyperledger.fabric.protos.peer.ProposalPackage;

public class UnpackedProposal {
        public String chaincodeName;
        public Common.ChannelHeader channelHeader;
        public Chaincode.ChaincodeInput input;
        public ProposalPackage.Proposal proposal;
        public Common.SignatureHeader signatureHeader;
        public ProposalPackage.SignedProposal signedProp;
        public byte[] proposalHash;

        public UnpackedProposal(String chaincodeName, Common.ChannelHeader channelHeader, Chaincode.ChaincodeInput input, ProposalPackage.Proposal proposal, Common.SignatureHeader signatureHeader) {
                this.chaincodeName = chaincodeName;
                this.channelHeader = channelHeader;
                this.input = input;
                this.proposal = proposal;
                this.signatureHeader = signatureHeader;
//                this.signedProp = signedProp;
//                this.proposalHash = proposalHash;
        }
}
