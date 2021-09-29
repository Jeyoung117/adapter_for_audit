package org.sslab.adapter;

import com.google.protobuf.ByteString;
import io.grpc.stub.StreamObserver;
import lombok.SneakyThrows;
import org.corfudb.protocols.wireprotocol.Token;
import org.corfudb.runtime.CorfuRuntime;
import org.corfudb.runtime.view.AddressSpaceView;
import org.corfudb.runtime.view.stream.IStreamView;
import org.hyperledger.fabric.protos.common.Common;
import org.hyperledger.fabric.protos.corfu.CorfuConnectGrpc;
import org.hyperledger.fabric.protos.peer.*;
//import org.hyperledger.fabric.sdk.ProposalResponse;


import java.util.*;
import java.util.logging.Logger;


/**
 * @author Jeyoung Hwang.capricorn116@postech.ac.kr
 *         created on 2021. 4. 10.
 */
public class AdapterModuleService extends CorfuConnectGrpc.CorfuConnectImplBase{
    Map<UUID, CorfuRuntime> runtimes;
    Map<UUID, IStreamView> streamViews;
    Map<String, Long> lastReadAddrs;
    //tokenMap key: fabric txID, value: access token
    Map<String, Token> tokenMap;

//    private ProposalPackage.Proposal proposal;


    public AdapterModuleService() {
        streamViews = new HashMap<UUID, IStreamView>();
        runtimes = new HashMap<UUID, CorfuRuntime>();
//        runtime = new CorfuRuntime(runtimeAddr[0]).connect();
        lastReadAddrs = new HashMap<String, Long>();
        System.out.println("Init AdapterModuleService");
        tokenMap = new HashMap<String, Token>();
    }


    private static CorfuRuntime getRuntimeAndConnect(String configurationString) {
            CorfuRuntime corfuRuntime = new CorfuRuntime(configurationString).connect();
            return corfuRuntime;
        }
    CorfuRuntime runtime =  getRuntimeAndConnect("141.223.121.251:12011");
    AddressSpaceView addressSpaceView = runtime.getAddressSpaceView();

        private final Logger logger = Logger.getLogger(AdapterModuleService.class.getName());


    //commitTransaction version 3
    //Receive proposal response from peer

    @Override
    public void processProposal(ProposalPackage.SignedProposal signedProposal, StreamObserver<ProposalResponsePackage.ProposalResponse> responseObserver) {
        unpackProposal(signedProposal);


        System.out.println("[peer-interface] {commitTransaction} Corfu runtime is finished");
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
                proposal, signatureHeader, signedProposal);
        return unpackedProposal;
    }
}





