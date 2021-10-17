package org.sslab.fabric.adapter;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.owlike.genson.Genson;
import io.grpc.stub.StreamObserver;
import lombok.SneakyThrows;
import org.corfudb.protocols.wireprotocol.Token;
import org.corfudb.runtime.CorfuRuntime;
import org.corfudb.runtime.view.stream.IStreamView;
import org.hyperledger.fabric.protos.common.Common;


import org.hyperledger.fabric.protos.corfu.CorfuConnectGrpc;
import org.hyperledger.fabric.protos.peer.*;
import org.sslab.fabric.chaincode.fabcar.FabCar;
import org.sslab.fabric.corfu.CorfuAccess;
import org.sslab.fabric.protoutil.Proputils;

//import org.sslab.adapter.chaincode.fabcar.FabCar;
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
    CorfuRuntime runtime;
    CorfuAccess corfu_access;
    private final Genson genson = new Genson();
//    private ProposalPackage.Proposal proposal;


    public AdapterModuleService(CorfuAccess corfu_access, CorfuRuntime runtime) {
        streamViews = new HashMap<UUID, IStreamView>();
        runtimes = new HashMap<UUID, CorfuRuntime>();
        this.runtime = runtime;
//        runtime = new CorfuRuntime(runtimeAddr[0]).connect();
        lastReadAddrs = new HashMap<String, Long>();
        System.out.println("Init AdapterModuleService");
        tokenMap = new HashMap<String, Token>();
        this.corfu_access = corfu_access;
    }

    private static CorfuRuntime getRuntimeAndConnect(String configurationString) {
            CorfuRuntime corfuRuntime = new CorfuRuntime(configurationString).connect();
            return corfuRuntime;
        }

    private final Logger logger = Logger.getLogger(AdapterModuleService.class.getName());


    @Override
    public void processProposal(ProposalPackage.SignedProposal signedProposal, StreamObserver<ProposalResponsePackage.ProposalResponse> responseObserver) {
        UnpackedProposal up =  unpackProposal(signedProposal);
        try {
            ProposalResponsePackage.ProposalResponse pResp = processProposalSuccessfullyOrError(up);
            System.out.println("[interface] {processProposal} Corfu runtime is finished");

            responseObserver.onNext(pResp);
            responseObserver.onCompleted();

            UUID streamID = CorfuRuntime.getStreamID(up.channelHeader.getChannelId());
            IStreamView sv = streamViews.get(streamID);

            long logicalAddr = sv.append(pResp.toByteArray());
            System.out.println("[orderer-stub] {append} proposal response size: " + pResp.getSerializedSize());
            System.out.println("[orderer-stub] {append} logical addr: " + logicalAddr);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
//        ProposalResponsePackage.ProposalResponse pResp = ProposalResponsePackage.ProposalResponse
//                .newBuilder()
//                .setVersion(1)
//                .build();
//
//        responseObserver.onNext(pResp);
//        responseObserver.onCompleted();
//        UUID streamID = runtime.getStreamID(up.channelHeader.getChannelId());
//        IStreamView sv = runtime.getStreamsView().get(streamID);
//        System.out.println(up.channelHeader.getChannelId());
//        long logicalAddr = sv.append(pResp.toByteArray());
//        sv.seek(27);
//        System.out.println(sv.getId());
//        System.out.println("[orderer-stub] {append} proposal response size: " + pResp.getSerializedSize());
//        System.out.println("[orderer-stub] {append} logical addr: " + logicalAddr);
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


    public ProposalResponsePackage.ProposalResponse processProposalSuccessfullyOrError(UnpackedProposal up) throws InvalidProtocolBufferException {
        TransactionParams txParams =  new TransactionParams(
                up.channelHeader.getTxId(),
                up.channelHeader.getChannelId(),
                up.chaincodeName,
                up.signedProp,
                up.proposal
                );
        ChaincodeShim.ChaincodeMessage ccMsg = executeProposal(txParams, up.chaincodeName, up.input);
        ByteString cceventBytes = createCCEventBytes(ccMsg.getChaincodeEvent());
        Proputils proputils = new Proputils();

        Chaincode.ChaincodeID ccID = Chaincode.ChaincodeID.newBuilder().setName(up.chaincodeName).setVersion(up.chaincodeName).build();
        ByteString prpBytes = proputils.getBytesProposalResponsePayload(up.proposalHash, null, ccMsg.getPayload(), cceventBytes, ccID);

        return ProposalResponsePackage.ProposalResponse
                .newBuilder()
                .setVersion(1)
                .setPayload(prpBytes)
//                .setResponse(null)
                .build();
    }

    public ChaincodeShim.ChaincodeMessage executeProposal(TransactionParams txParams, String chaincodeName, Chaincode.ChaincodeInput chaincodeInput) throws InvalidProtocolBufferException {

//        CorfuChaincodeShim.CorfuChaincodeMessage message= new CorfuChaincodeShim.CorfuChaincodeMessage();
        FabCar fabcar = new FabCar();
        System.out.println(chaincodeInput);
        System.out.println(chaincodeInput.toString());


//        InvocationStubImpl invocationStub = new InvocationStubImpl(ChaincodeMessageFactory.newGetStateEventMessage(txParams.channelID, txParams.txID, "", "CAR9", );
//        Context context = new Context(invocationStub);
////        Car car = fabcar.queryCar(context, chaincodeInput.getArgs(1).toString());
//        Car car1 = fabcar.queryCar(context, "CAR9");
//        System.out.println(car1);
//        fabcar.changeCarOwner(context,"CAR9", "newowner1");
//        Car car2 = fabcar.queryCar(context, "CAR9");
//        System.out.println(car2);

        return null;
    }
//    public void executeProposal(String channelID, String chaincodeName, Chaincode.ChaincodeInput chaincodeInput) {
//        FabCar fabcar = new FabCar();
//        fabcar.queryCar(chaincodeInput.toString());

//        CorfuChaincodeShim.CorfuChaincodeMessage message= new CorfuChaincodeShim.CorfuChaincodeMessage();

    public void callChaincode(TransactionParams txParams, String chaincodeName, Chaincode.ChaincodeInput chaincodeInput) throws InvalidProtocolBufferException {
        FabCar fabcar = new FabCar();
        System.out.println(chaincodeInput);
        System.out.println(chaincodeInput.toString());
    }

    public ByteString createCCEventBytes(ChaincodeEventPackage.ChaincodeEvent ccevent) {
        if(ccevent == null) {
            return null;
        }
        return ccevent.toByteString();
    }
}





