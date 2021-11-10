package org.sslab.fabric.adapter;

import bsp_transaction.BspTransactionOuterClass;
import bsp_transaction.CorfuConnectBSPGrpc;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import io.grpc.stub.StreamObserver;
import lombok.SneakyThrows;
import org.corfudb.protocols.wireprotocol.Token;
import org.corfudb.runtime.CorfuRuntime;
import org.corfudb.runtime.view.AddressSpaceView;
import org.corfudb.runtime.view.stream.IStreamView;
//import org.hyperledger.fabric.protos.common.Common;

import org.hyperledger.fabric.protos.common.Common;
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
public class AdapterModuleService extends CorfuConnectBSPGrpc.CorfuConnectBSPImplBase{
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
    public void processProposalforBench(BspTransactionOuterClass.ProposalforBench proposal, StreamObserver<BspTransactionOuterClass.SubmitResponse> responseObserver) {

        TransactionParams txParams =  new TransactionParams(
                proposal.getTxId(),
                "mychannel",
                proposal.getChaincodeId(),
                proposal.getChaincodeArgsList()
        );
        BspTransactionOuterClass.SubmitResponse response = executeProposal(txParams.channelID, txParams.chaincodeID, txParams.chaincodeArgs);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

//    @SneakyThrows
//    @Override
//    public void processProposalWrite(Sharedlog.SubmitCheck request, StreamObserver<Sharedlog.ResCheck> responseObserver) {
////        UnpackedProposal up =  unpackProposal(proposal);
////        String result = processProposalSuccessfullyOrError(up);
//
////        System.out.println("received channel name: " + request.getChannelID());
////        System.out.println("received chaincode name: " + request.getChaincodeID());
////        System.out.println("received key : " + request.getKey());
//
//        executeProposal(request.getChannelID(), request.getChaincodeID(), request.getKey());
////        corfu_access.getStringState(request.getKey(), request.getChannelID(),request.getChaincodeID());
////        corfu_access.putStringState(request.getKey(), request.getChannelID(),request.getChaincodeID(),request.getChaincodeID().getBytes());
//        Sharedlog.ResCheck response = Sharedlog.ResCheck
//                .newBuilder()
//                .setCheckresult(1)
//                .build();
//
//        responseObserver.onNext(response);
//        responseObserver.onCompleted();
//    }



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

    public BspTransactionOuterClass.SubmitResponse executeProposal(String channelID, String chaincodeId, List<String> chaincodeArgs) {
        try {
            BspTransactionOuterClass.SubmitResponse res =  callChaincode(channelID, chaincodeId, chaincodeArgs);
            return res;
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        BspTransactionOuterClass.SubmitResponse resError = BspTransactionOuterClass.SubmitResponse.newBuilder()
                .setStatus(500)
                .build();
        return resError;
    }


    public BspTransactionOuterClass.SubmitResponse callChaincode(String channelID, String chaincodeId, List<String> chaincodeArgs) throws InvalidProtocolBufferException {
        Token snToken = corfu_access.issueSnapshotToken();
        FabCar fabcar = new FabCar();
        InvocationStubImpl invocationStub = null;
        try {
            invocationStub = new InvocationStubImpl(channelID, "123456", chaincodeId, corfu_access);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        Context context = new Context(invocationStub);
        Car car = fabcar.createCar(context, chaincodeArgs.get(1), chaincodeArgs.get(2), chaincodeArgs.get(3), chaincodeArgs.get(4), chaincodeArgs.get(5));
        corfu_access.commitTransaction();
        if (car.toString() != null) {
            BspTransactionOuterClass.SubmitResponse res = BspTransactionOuterClass.SubmitResponse.newBuilder()
                    .setStatus(200)
                    .setPayload(ByteString.copyFrom(car.toString().getBytes(StandardCharsets.UTF_8)))
                    .build();
            return res;
        } else {
            BspTransactionOuterClass.SubmitResponse resError = BspTransactionOuterClass.SubmitResponse.newBuilder()
                    .setStatus(500)
                    .build();
            return resError;
        }
    }

//    public void callChaincode(TransactionParams txParams, String chaincodeName, Chaincode.ChaincodeInput chaincodeInput) throws InvalidProtocolBufferException {
//        FabCar fabcar = new FabCar();
//        System.out.println(chaincodeInput);
//        System.out.println(chaincodeInput.toString());
//    }

    //    @SneakyThrows
//    public UnpackedProposal unpackProposal(ProposalPackage.SignedProposal signedProposal) {
//        ProposalPackage.Proposal proposal;
//        Common.Header header;
//        Common.ChannelHeader channelHeader;
//        Common.SignatureHeader signatureHeader;
//        ProposalPackage.ChaincodeHeaderExtension chaincodeHdrExt;
//        ProposalPackage.ChaincodeProposalPayload payload;
//        Chaincode.ChaincodeInvocationSpec chaincodeInvocationSpec;
//
////        byte[] sp = signedProposal.getProposalBytes().toByteArray();
////        pr = ProposalPackage.Proposal.parseFrom(proposal);
//        proposal = ProposalPackage.SignedProposal.parseFrom(signedProposal);
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
//    }

}





