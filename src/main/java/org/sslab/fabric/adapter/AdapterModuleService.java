package org.sslab.fabric.adapter;

import bsp_transaction.AggregatorGrpc;
import bsp_transaction.BspTransactionOuterClass;
import com.google.protobuf.InvalidProtocolBufferException;
import io.grpc.stub.StreamObserver;
import lombok.SneakyThrows;
import org.corfudb.protocols.wireprotocol.Token;
import org.corfudb.runtime.CorfuRuntime;
import org.corfudb.runtime.view.AddressSpaceView;
import org.corfudb.runtime.view.stream.IStreamView;
//import org.hyperledger.fabric.protos.common.Common;

import org.hyperledger.fabric.protos.peer.Chaincode;
import org.hyperledger.fabric.protos.peer.ChaincodeShim;
import org.hyperledger.fabric.protos.peer.ProposalPackage;
import org.sslab.fabric.chaincode.fabcar.Car;
import org.sslab.fabric.chaincode.fabcar.FabCar;
import org.sslab.fabric.chaincodeshim.contract.Context;
import org.sslab.fabric.chaincodeshim.shim.impl.ChaincodeInvocationTask;
import org.sslab.fabric.chaincodeshim.shim.impl.ChaincodeMessageFactory;
import org.sslab.fabric.chaincodeshim.shim.impl.InvocationStubImpl;
import org.sslab.fabric.corfu.Corfu_access;
import protos.CorfuConnectGrpc;
import protos.Sharedlog;
//import org.sslab.adapter.chaincode.fabcar.FabCar;
//import org.hyperledger.fabric.sdk.ProposalResponse;


import java.nio.charset.StandardCharsets;
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
    Corfu_access corfu_access;
    private final Logger logger = Logger.getLogger(AdapterModuleService.class.getName());
    int i=0;


    public AdapterModuleService(Corfu_access corfu_access) {
        streamViews = new HashMap<UUID, IStreamView>();
        runtimes = new HashMap<UUID, CorfuRuntime>();
//        runtime = new CorfuRuntime(runtimeAddr[0]).connect();
        lastReadAddrs = new HashMap<String, Long>();
        System.out.println("Init AdapterModuleService");
        tokenMap = new HashMap<String, Token>();
        this.corfu_access = corfu_access;
//        this.runtime = runtime;
    }

    @SneakyThrows
    @Override
    public void processProposal(Sharedlog.ReqCheck request, StreamObserver<Sharedlog.ResCheck> responseObserver) {
//        UnpackedProposal up =  unpackProposal(proposal);
//        String result = processProposalSuccessfullyOrError(up);

//        System.out.println("received channel name: " + request.getChannelID());
//        System.out.println("received chaincode name: " + request.getChaincodeID());
//        System.out.println("received key : " + request.getKey());

        executeProposal(request.getChannelID(), request.getChaincodeID(), request.getKey());
//        corfu_access.getStringState(request.getKey(), request.getChannelID(),request.getChaincodeID());
//        corfu_access.putStringState(request.getKey(), request.getChannelID(),request.getChaincodeID(),request.getChaincodeID().getBytes());
        Sharedlog.ResCheck response = Sharedlog.ResCheck
                .newBuilder()
                .setCheckresult(1)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @SneakyThrows
    public void unpackProposal(BspTransactionOuterClass.Proposal proposal) {
//        Proposal pr;
//        Common.Header header;
//        Common.ChannelHeader channelHeader;
//        Common.SignatureHeader signatureHeader;
//        ProposalPackage.ChaincodeHeaderExtension chaincodeHdrExt;
//        ProposalPayload payload;
//        Chaincode.ChaincodeInvocationSpec chaincodeInvocationSpec;
//
////        byte[] sp = signedProposal.getProposalBytes().toByteArray();
//        pr = ProposalPackage.Proposal.parseFrom(proposal);
//
//        header = Common.Header.parseFrom(proposal.getHeader());
//
//        channelHeader = Common.ChannelHeader.parseFrom(header.getChannelHeader());
//
//        signatureHeader = Common.SignatureHeader.parseFrom(header.getSignatureHeader());
//
//        chaincodeHdrExt = ProposalPackage.ChaincodeHeaderExtension.parseFrom(channelHeader.getExtension());
//
//        payload = ProposalPackage.ChaincodeProposalPayload.parseFrom(proposal.getPayload());
//
//        chaincodeInvocationSpec = Chaincode.ChaincodeInvocationSpec.parseFrom(payload.getInput());
//
//        UnpackedProposal unpackedProposal = new UnpackedProposal(chaincodeHdrExt.getChaincodeId().getName().toString(), channelHeader, chaincodeInvocationSpec.getChaincodeSpec().getInput(),
//                proposal, signatureHeader);
//        return unpackedProposal;
    }

//    public String processProposalSuccessfullyOrError(UnpackedProposal up) throws InvalidProtocolBufferException {
//        TransactionParams txParams =  new TransactionParams(
//                up.channelHeader.getTxId(),
//                up.channelHeader.getChannelId(),
//                up.chaincodeName,
//                up.signedProp,
//                up.proposal
//                );
//        executeProposal(txParams, up.chaincodeName, up.input);
//    return null;
//
//    }

//    public void executeProposal(TransactionParams txParams, String chaincodeName, Chaincode.ChaincodeInput chaincodeInput) throws InvalidProtocolBufferException {
//
//        ChaincodeShim.ChaincodeMessage message= new ChaincodeShim.ChaincodeMessage();
//        FabCar fabcar = new FabCar();
//        System.out.println(chaincodeInput);
//        System.out.println(chaincodeInput.toString());


//        InvocationStubImpl invocationStub = new InvocationStubImpl(ChaincodeMessageFactory.newGetStateEventMessage(txParams.channelID, txParams.txID, "", "CAR9", );
//        Context context = new Context(invocationStub);
////        Car car = fabcar.queryCar(context, chaincodeInput.getArgs(1).toString());
//        Car car1 = fabcar.queryCar(context, "CAR9");
//        System.out.println(car1);
//        fabcar.changeCarOwner(context,"CAR9", "newowner1");
//        Car car2 = fabcar.queryCar(context, "CAR9");
//        System.out.println(car2);
//
//    }

    public void executeProposal(String channelID, String chaincodeId, String key) {
        try {
            callChaincode(channelID, chaincodeId, key);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }

//        System.out.println(car);
    }


    public void callChaincode(String channelID, String chaincodeId, String key) throws InvalidProtocolBufferException {
        corfu_access.issueSnapshotToken();
        FabCar fabcar = new FabCar();
        InvocationStubImpl invocationStub = null;
        try {
            invocationStub = new InvocationStubImpl(channelID, "123456", chaincodeId, corfu_access);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        Context context = new Context(invocationStub);
        fabcar.queryCar(context, key);
        corfu_access.commitTransaction();
    }

//    public void callChaincode(TransactionParams txParams, String chaincodeName, Chaincode.ChaincodeInput chaincodeInput) throws InvalidProtocolBufferException {
//        FabCar fabcar = new FabCar();
//        System.out.println(chaincodeInput);
//        System.out.println(chaincodeInput.toString());
//    }

}





