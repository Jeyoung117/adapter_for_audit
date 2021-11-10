package org.sslab.fabric.adapter;

import bsp_transaction.BspTransactionOuterClass;
import bsp_transaction.CorfuConnectBSPGrpc;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.owlike.genson.Genson;
import io.grpc.Context;
import io.grpc.stub.ServerCallStreamObserver;
import io.grpc.stub.StreamObserver;
import lombok.SneakyThrows;
import org.corfudb.protocols.wireprotocol.Token;
import org.corfudb.runtime.CorfuRuntime;
import org.corfudb.runtime.view.stream.IStreamView;
import org.hyperledger.fabric.protos.common.Common;

import org.hyperledger.fabric.protos.peer.*;
import org.sslab.fabric.chaincode_support.ChaincodeSupport;
import org.sslab.fabric.chaincodeshim.contract.ContractRouter;
import org.sslab.fabric.chaincodeshim.shim.ChaincodeStub;
import org.sslab.fabric.chaincodeshim.shim.impl.ChaincodeMessageFactory;
import org.sslab.fabric.chaincodeshim.shim.impl.InvocationStubImpl;
import org.sslab.fabric.corfu.CorfuAccess;
import org.sslab.fabric.corfu.Rwset_builder;
import org.sslab.fabric.dtype.dtype;
import org.sslab.fabric.protoutil.TxUtils;

//import org.sslab.adapter.chaincode.fabcar.FabCar;
//import org.hyperledger.fabric.sdk.ProposalResponse;


import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.Logger;

import static io.netty.util.internal.SocketUtils.accept;
import static org.hyperledger.fabric.protos.common.Common.HeaderType.ENDORSER_TRANSACTION;
import static org.hyperledger.fabric.protos.peer.ChaincodeShim.ChaincodeMessage.Type.COMPLETED;
import static org.hyperledger.fabric.protos.peer.ChaincodeShim.ChaincodeMessage.Type.ERROR;
import static org.sslab.fabric.protoutil.Proputils.toProtoResponse;
//

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
    CorfuRuntime runtime;
    CorfuAccess corfu_access;
    private final Genson genson = new Genson();
    ContractRouter cfc;
//    private ProposalPackage.Proposal proposal;


    public AdapterModuleService(CorfuAccess corfu_access, CorfuRuntime runtime, ContractRouter cfc) {
        streamViews = new HashMap<UUID, IStreamView>();
        runtimes = new HashMap<UUID, CorfuRuntime>();
        this.runtime = runtime;
//        runtime = new CorfuRuntime(runtimeAddr[0]).connect();
        lastReadAddrs = new HashMap<String, Long>();
        System.out.println("Init AdapterModuleService");
        tokenMap = new HashMap<String, Token>();
        this.corfu_access = corfu_access;
        this.cfc = cfc;
    }

    private final Logger logger = Logger.getLogger(AdapterModuleService.class.getName());

    @SneakyThrows
    @Override
    public void processProposal(BspTransactionOuterClass.Proposal proposal, StreamObserver<BspTransactionOuterClass.SubmitResponse> responseObserver) {
//        UnpackedProposal up =  unpackProposal(proposal);
        BspTransactionOuterClass.ProposalPayload propPayload = BspTransactionOuterClass.ProposalPayload.parseFrom(proposal.getPayload());


        BspTransactionOuterClass.SubmitResponse res = processProposalSuccessfullyOrError(proposal, propPayload);

        responseObserver.onNext(res);
        responseObserver.onCompleted();
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
//        BspTransactionOuterClass.SubmitResponse res = executeProposal(txParams, proposal.getChaincodeId());

//        while(!Context.current().isCancelled()){ // THIS LINE CHANGED
//            if(res!=null && !res.isInitialized()){
//                responseObserver.onNext(res);
//                responseObserver.onCompleted();
//            }else{
//                break;
//            }
//        }

        corfu_access.issueSnapshotToken();
        System.out.println(txParams.chaincodeArgs.get(1));
        String submitTest = "submitTest";
        System.out.println("while의 밖에서 벌어지는 일");
        corfu_access.putStringState(txParams.chaincodeArgs.get(1), "mychannel", txParams.chaincodeID, submitTest.getBytes(StandardCharsets.UTF_8));
        corfu_access.commitTransaction();

        BspTransactionOuterClass.SubmitResponse res = BspTransactionOuterClass.SubmitResponse.newBuilder()
                .setStatus(200)
                .build();
        Context.CancellableContext withCancellation = Context.current().withCancellation();
//        while(!Context.current().isCancelled()){ // THIS LINE CHANGED
//            if(res!=null && !res.getDefaultInstanceForType().isInitialized()){
//                responseObserver.onNext(res);
//
//                corfu_access.issueSnapshotToken();
//                System.out.println(txParams.chaincodeArgs.get(1));
//                submitTest = "submitTest";
//                System.out.println("while의 if 안에서 벌어지는 일");
//                corfu_access.putStringState(txParams.chaincodeArgs.get(1), "mychannel", txParams.chaincodeID, submitTest.getBytes(StandardCharsets.UTF_8));
//                corfu_access.commitTransaction();
//            }else{
//                System.out.println("while의 else 안에서 벌어지는 일");
//                withCancellation.cancel(null);
//                break;
//            }
//        }
        clientCallStreamObserver.cancel()
        responseObserver.onNext(res);
        responseObserver.onCompleted();
        withCancellation.cancel(null);
    }

//    @Override
//    public void query(BspTransactionOuterClass.QueryRequest proposal, StreamObserver<BspTransactionOuterClass.QueryResponse> responseObserver) {
//
//        List<String> args = new ArrayList<String>();
//        args.add(proposal.getKey());
//        TransactionParams txParams =  new TransactionParams(
//                proposal.getChannelID(),
//                proposal.getChaincodeID(),
//                args
//        );
//
//
//        Token snapshotTimestamp = corfu_access.issueSnapshotToken();
//        ChaincodeSupport chaincodeSupport = new ChaincodeSupport();
//        long seq = snapshotTimestamp.getSequence();
//        ChaincodeStub stub = new InvocationStubImpl(txParams, corfu_access, seq);
//        org.sslab.fabric.chaincodeshim.shim.Chaincode.Response ccresp =  chaincodeSupport.Execute(txParams, proposal.getChaincodeID(), stub, cfc);
//        //debugging용 출력, propPayload contents 확인용
//        logger.info(String.format("Receive Query Tx chaincode %s key %s", proposal.getChaincodeID(), proposal.getKey()));
//
//
//        BspTransactionOuterClass.SubmitResponse res = processProposalSuccessfullyOrError(proposal, propPayload);
//
//        responseObserver.onNext(res);
//        responseObserver.onCompleted();
//    }


    public BspTransactionOuterClass.SubmitResponse processProposalSuccessfullyOrError(BspTransactionOuterClass.Proposal signedProposal, BspTransactionOuterClass.ProposalPayload propPayload) throws InvalidProtocolBufferException {

        TransactionParams txParams =  new TransactionParams(
                propPayload.getTxId(),
                propPayload.getChaincodeId(),
                signedProposal,
                propPayload,
                propPayload.getChaincodeArgsList(),
                "mychannel"
                );

        BspTransactionOuterClass.SubmitResponse res = executeProposal(txParams, propPayload.getChaincodeId());

        return res;
    }

    public BspTransactionOuterClass.SubmitResponse executeProposal(TransactionParams txParams, String chaincodeName) throws InvalidProtocolBufferException {

        //fabric chaincode_support.go 의 execute 에서의 선언 copy
//        ChaincodeShim.ChaincodeMessage ccMsg = ChaincodeShim.ChaincodeMessage.newBuilder()
//                .setType(ChaincodeShim.ChaincodeMessage.Type.TRANSACTION)
//                .setChaincodeId(txParams.chaincodeID)
//                .setTxid(txParams.txID)
//                .setPayload(txParams.signedProp.toByteString())
//                .build();

        ChaincodeShim.ChaincodeMessage ccMsg = ChaincodeShim.ChaincodeMessage.newBuilder()
                .setType(ChaincodeShim.ChaincodeMessage.Type.TRANSACTION)
                .setChaincodeId(txParams.chaincodeID)
                .setTxid(txParams.txID)
//                .setPayload(txParams.signedProp.toByteString())
                .build();

        BspTransactionOuterClass.SubmitResponse res = callChaincode(txParams, chaincodeName, ccMsg);

            return res;
    }

    public BspTransactionOuterClass.SubmitResponse callChaincode(TransactionParams txParams, String chaincodeName, ChaincodeShim.ChaincodeMessage ccMsg) throws InvalidProtocolBufferException {
        Token snapshotTimestamp = corfu_access.issueSnapshotToken();
        ChaincodeSupport chaincodeSupport = new ChaincodeSupport();
        long seq = snapshotTimestamp.getSequence();
//        ChaincodeStub stub = new InvocationStubImpl(ccMsg, corfu_access, seq);
                ChaincodeStub stub = new InvocationStubImpl(txParams, corfu_access, seq);

        org.sslab.fabric.chaincodeshim.shim.Chaincode.Response ccresp =  chaincodeSupport.Execute(txParams, chaincodeName, stub, cfc);

        Rwset_builder rwset = ccresp.getRwset();
        //writeset이 null 즉, query 일 시 바로 return
        if(ccresp.getRwset() == null) {
            BspTransactionOuterClass.SubmitResponse res = toProtoResponse(ccresp);
            corfu_access.commitTransaction();
            return res;
        }
//        BspTransactionOuterClass.BspTransactionType txLocalityType = BspTransactionOuterClass.BspTransactionType.IntraTx;
//            BspTransactionOuterClass.Version version = BspTransactionOuterClass.Version.newBuilder()
//                    .setBlockNumber(snapshotTimestamp.getSequence())  //문제되면 couter로 변경
//                    .setTxOffset(0)
//                    .build();
//
//        String regionID = "edgechain0";
//        ByteString bspTxBytes = buildBspTX(txLocalityType, txParams.signedProp, txParams.propPayload, seq, regionID, "mychannel", rwset); //chainID 추후 채널 config 통해 수정 필요
//
////        Common.Envelope env = createEnvFromBSPType(55555, seq, txParams.txID, "mychannel", bspTxBytes);
//
//        ByteString txEventBytes = buildTxEvent(txParams.propPayload, seq);

        //response에서 받은 값 확인
//        byte[] tesmp = ccresp.getPayload();
//        String tesmp1 = new String(tesmp);
//        Object result = genson.deserialize(tesmp1, Object.class);
//        System.out.println("return value: " + result);

//        ChaincodeShim.ChaincodeMessage ccMessage = createCCMSG(ccresp, ccMsg, stub);
//        ByteString cceventBytes = createCCEventBytes(ccMessage.getChaincodeEvent());

        /*
        체인코드 simulation result 만들어 주는 코딩
        */

        BspTransactionOuterClass.SubmitResponse res = toProtoResponse(ccresp);
        corfu_access.commitTransaction(); //

        return res;
    }

    public ByteString buildBspTX(BspTransactionOuterClass.BspTransactionType txLocalityType, BspTransactionOuterClass.Proposal prop, BspTransactionOuterClass.ProposalPayload propPayload, long seq, String regionID, String chainID, Rwset_builder rwset) {
        ByteString clientProposal = prop.toByteString();
        BspTransactionOuterClass.TxInput input = BspTransactionOuterClass.TxInput.newBuilder()
                .setChaincodeId(propPayload.getChaincodeId())
                .setChaincodeVersion("v1.0")
                .addAllChaincodeArgs(propPayload.getChaincodeArgsList())
                .addAllReads(rwset.getReadSet())
                .build();

        BspTransactionOuterClass.TxOutput output = BspTransactionOuterClass.TxOutput.newBuilder()
                .addAllWrites(rwset.getWriteSet())
                .build();

        BspTransactionOuterClass.BspTransaction bspTx = BspTransactionOuterClass.BspTransaction.newBuilder()
                .setType(txLocalityType)
                .setClientId(propPayload.getClientId())
                .setTxId(propPayload.getTxId())
                .setSeq(seq)
                .setChainID(chainID)
                .setInput(input)
                .setOutput(output)
                .setProposal(clientProposal)
                .setRegion(regionID)
                .build();

        return bspTx.toByteString();
    }

    public ByteString buildTxEvent(BspTransactionOuterClass.ProposalPayload propPayload, long seq) {
        BspTransactionOuterClass.ConsensusState consensusState = BspTransactionOuterClass.ConsensusState.newBuilder()
                .setLevel(BspTransactionOuterClass.ConsensusState.Level.EC_Spec)
                .setResult(BspTransactionOuterClass.ConsensusState.Result.Commit)
                .build();

        BspTransactionOuterClass.TxEventPayload txEventPayload = BspTransactionOuterClass.TxEventPayload.newBuilder()
                .setClientID(propPayload.getClientId())
                .setTxId(propPayload.getTxId())
                .setNumber(seq) //current? 혹은 발급받은 token으로 변경
                .setOffset(0)
                .setState(consensusState)
                .setActiveBSPAddress("")
                .build();

        ByteString txEventPayloadBytes = txEventPayload.toByteString();
        BspTransactionOuterClass.TxEvent txEvent = BspTransactionOuterClass.TxEvent.newBuilder()
                .setPayload(txEventPayloadBytes)
                .build();
        ByteString txEventBytes = txEvent.toByteString();
        return txEventBytes;
    }

//    public Common.Envelope createEnvFromBSPType(int msgType, long seq, String TxID, String ChainID, ByteString payload) {
//        TxUtils txUtils = new TxUtils();
//        TransactionPackage.Transaction dataMsg = getPayloadInsideTransaction(payload);
//        Common.Envelope env = txUtils.CreateSignedEnvelopeWithTLSBindingWithTxID(ENDORSER_TRANSACTION, ChainID, null, dataMsg, msgType, seq, null, TxID);
//
//        return env;
//    }

    public TransactionPackage.Transaction getPayloadInsideTransaction(ByteString bspTxBytes) {
        TransactionPackage.TransactionAction act = TransactionPackage.TransactionAction.newBuilder()
                .setPayload(bspTxBytes)
                .build();

        TransactionPackage.Transaction transaction = TransactionPackage.Transaction.newBuilder()
                .setActions(0, act)
                .build();
        return transaction;

    }

    public ChaincodeShim.ChaincodeMessage createCCMSG(org.sslab.fabric.chaincodeshim.shim.Chaincode.Response result, ChaincodeShim.ChaincodeMessage message, ChaincodeStub stub) {
        ChaincodeShim.ChaincodeMessage finalResponseMessage;
        try {
        if (result.getStatus().getCode() >= org.sslab.fabric.chaincodeshim.shim.Chaincode.Response.Status.INTERNAL_SERVER_ERROR.getCode()) {
            // Send ERROR with entire result.Message as payload
            logger.severe(
                    () -> String.format("[%-8.8s] Invoke failed with error code %d. Sending %s", message.getTxid(), result.getStatus().getCode(), ERROR));
            finalResponseMessage = ChaincodeMessageFactory.newErrorEventMessage(message.getChannelId(), message.getTxid(), result.getMessage(),
                    stub.getEvent());
        } else {
            // Send COMPLETED with entire result as payload
            logger.fine(() -> String.format("[%-8.8s] Invoke succeeded. Sending %s", message.getTxid(), COMPLETED));
            finalResponseMessage = ChaincodeMessageFactory.newCompletedEventMessage(message.getChannelId(), message.getTxid(), result, stub.getEvent());
        }

    } catch (RuntimeException e) {
        logger.severe(() -> String.format("[%-8.8s] Invoke failed. Sending %s: %s", message.getTxid(), ERROR, e));
        finalResponseMessage = ChaincodeMessageFactory.newErrorEventMessage(message.getChannelId(), message.getTxid(), e);
    }

    // also return for reference
        return finalResponseMessage;
    }



    public ByteString createCCEventBytes(ChaincodeEventPackage.ChaincodeEvent ccevent) {
        if(ccevent == null) {
            return null;
        }
        return ccevent.toByteString();
    }

    public ByteString getBytesProposalResponsePayload(byte[] ProposalHash, ProposalResponsePackage.Response response, ByteString result, ByteString event, Chaincode.ChaincodeID ccid) {
        ProposalPackage.ChaincodeAction cAct = ProposalPackage.ChaincodeAction.newBuilder()
                .setEvents(event)
                .setResults(result)
                .setResponse(response)
                .setChaincodeId(ccid)
                .build();

        ByteString cActBytes = cAct.toByteString();

        ProposalResponsePackage.ProposalResponsePayload prp = ProposalResponsePackage.ProposalResponsePayload.newBuilder()
                .setExtension(cActBytes)
                .setProposalHash(ByteString.copyFrom(ProposalHash))
                .build();

        ByteString prpBytes = prp.toByteString();
        return prpBytes;
    }
}





