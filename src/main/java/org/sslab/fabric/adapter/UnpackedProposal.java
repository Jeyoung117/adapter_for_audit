package org.sslab.fabric.adapter;

import com.google.protobuf.ByteString;
import lombok.SneakyThrows;
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
        public ByteString proposalHash;

        public UnpackedProposal(String chaincodeName, Common.ChannelHeader channelHeader, Chaincode.ChaincodeInput input, ProposalPackage.Proposal proposal, Common.SignatureHeader signatureHeader,
                ProposalPackage.SignedProposal signedProp, ByteString proposalHash) {
                this.chaincodeName = chaincodeName;
                this.channelHeader = channelHeader;
                this.input = input;
                this.proposal = proposal;
                this.signatureHeader = signatureHeader;
                this.signedProp = signedProp;
                this.proposalHash = proposalHash;
        }

        @SneakyThrows
        public UnpackedProposal unpackProposal(ProposalPackage.SignedProposal signedProposal) {
                ProposalPackage.Proposal proposal;
                Common.Header header;
                Common.ChannelHeader channelHeader;
                Common.SignatureHeader signatureHeader;
                ProposalPackage.ChaincodeHeaderExtension chaincodeHdrExt;
                ProposalPackage.ChaincodeProposalPayload payload;
                Chaincode.ChaincodeInvocationSpec chaincodeInvocationSpec;

//        byte[] sp = signedProposal.getProposalBytes().toByteArray();
                proposal = ProposalPackage.Proposal.parseFrom(signedProposal.getProposalBytes());
                header = Common.Header.parseFrom(proposal.getHeader());
                channelHeader = Common.ChannelHeader.parseFrom(header.getChannelHeader());
                signatureHeader = Common.SignatureHeader.parseFrom(header.getSignatureHeader());
                chaincodeHdrExt = ProposalPackage.ChaincodeHeaderExtension.parseFrom(channelHeader.getExtension());
                payload = ProposalPackage.ChaincodeProposalPayload.parseFrom(proposal.getPayload());
                chaincodeInvocationSpec = Chaincode.ChaincodeInvocationSpec.parseFrom(payload.getInput());

                UnpackedProposal unpackedProposal = new UnpackedProposal(chaincodeHdrExt.getChaincodeId().getName().toString(), channelHeader, chaincodeInvocationSpec.getChaincodeSpec().getInput(),
                        proposal, signatureHeader, signedProposal, signedProposal.getProposalBytes());
                return unpackedProposal;
        }
}
