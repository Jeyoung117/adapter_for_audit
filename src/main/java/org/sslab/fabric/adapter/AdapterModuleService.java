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
import org.sslab.fabric.chaincode.fabcar.fabcar;
import org.sslab.fabric.chaincodeshim.contract.Context;
import org.sslab.fabric.chaincodeshim.contract.ContractRouter;
import org.sslab.fabric.chaincodeshim.shim.ChaincodeStub;
import org.sslab.fabric.chaincodeshim.shim.impl.ChaincodeInvocationTask;
import org.sslab.fabric.chaincodeshim.shim.impl.ChaincodeMessageFactory;
import org.sslab.fabric.chaincodeshim.shim.impl.InvocationStubImpl;
import org.sslab.fabric.corfu.CorfuAccess;
import org.sslab.fabric.protoutil.Proputils;

//import org.sslab.adapter.chaincode.fabcar.FabCar;
//import org.hyperledger.fabric.sdk.ProposalResponse;


import java.security.MessageDigest;
import java.util.*;
import java.util.logging.Logger;

import static org.hyperledger.fabric.protos.peer.ChaincodeShim.ChaincodeMessage.Type.COMPLETED;
import static org.hyperledger.fabric.protos.peer.ChaincodeShim.ChaincodeMessage.Type.ERROR;
import static org.sslab.fabric.protoutil.Proputils.toProtoResponse;


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

    @Override
    public void processProposal(ProposalPackage.SignedProposal signedProposal, StreamObserver<ProposalResponsePackage.ProposalResponse> responseObserver) {
        UnpackedProposal up =  unpackProposal(signedProposal);

        try {
            ProposalResponsePackage.ProposalResponse pResp = processProposalSuccessfullyOrError(up);

            responseObserver.onNext(pResp);
            responseObserver.onCompleted();

//            System.out.println(up.channelHeader.getChannelId());
//            UUID streamID = runtime.getStreamID(up.channelHeader.getChannelId());
//            IStreamView iStreamView = runtime.getStreamsView().get(streamID);
//            StreamsView streamsView = new StreamsView(runtime);
//
//            long logicalAddr = iStreamView.append(pResp.toByteArray());
//            System.out.println("[orderer-stub] {append} proposal response size: " + pResp.getSerializedSize());
//            System.out.println("[orderer-stub] {append} logical addr: " + logicalAddr);
//            System.out.println("[interface] {processProposal} Corfu runtime is finished");
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

    public ProposalResponsePackage.ProposalResponse processProposalSuccessfullyOrError(UnpackedProposal up) throws InvalidProtocolBufferException {
        TransactionParams txParams =  new TransactionParams(
                up.channelHeader.getTxId(),
                up.channelHeader.getChannelId(),
                up.chaincodeName,
                up.signedProp,
                up.proposal,
                up.proposalHash
                );
        Proputils proputils = new Proputils();

//        ChaincodeShim.ChaincodeMessage ccMsg = executeProposal(txParams, up.chaincodeName, up.input);
        ProposalResponsePackage.ProposalResponse proposalResponse = executeProposal(txParams, up.chaincodeName, up.input);
//        ProposalResponsePackage.Response response = ProposalResponsePackage.Response.newBuilder()
//                .setStatus(200)
//                .setMessage("ChaincodeMessage_COMPLETED")
//                .build();

//        ByteString cceventBytes = createCCEventBytes(ccMsg.getChaincodeEvent());
//
//        Chaincode.ChaincodeID ccID = Chaincode.ChaincodeID.newBuilder().setName(up.chaincodeName).setVersion(up.chaincodeName).build();
//        System.out.println(up.proposalHash);
//        ByteString prpBytes = proputils.getBytesProposalResponsePayload(up.proposalHash, response, ccMsg.getPayload(), cceventBytes, ccID);

        ProposalResponsePackage.ProposalResponse pResp = ProposalResponsePackage.ProposalResponse
                .newBuilder()
                .setVersion(1)
//                .setPayload(prpBytes)
//                .setResponse(null)
                .build();

        return pResp;
    }

    public ProposalResponsePackage.Response executeProposal(TransactionParams txParams, String chaincodeName, Chaincode.ChaincodeInput chaincodeInput) throws InvalidProtocolBufferException {
        Proputils proputils = new Proputils();
        try {
//            Instant time = Instant.now();
//            Timestamp timestamp = Timestamp.newBuilder().setSeconds(time.getEpochSecond())
//                    .setNanos(time.getNano()).build();
//
//            ChaincodeEventPackage.ChaincodeEvent ccEvent = ChaincodeEventPackage.ChaincodeEvent.newBuilder()
//                    .setPayload(txParams.proposal.getPayload())
//                    .setChaincodeId(chaincodeName)
//                    .setEventName("success")
//                    .setTxId(txParams.txID)
//                    .build();
//
//            ChaincodeShim.ChaincodeMessage ccMsg =  ChaincodeShim.ChaincodeMessage
//                    .newBuilder()
//                    .setType(ChaincodeShim.ChaincodeMessage.Type.RESPONSE)
//                    .setTimestamp(timestamp)
//                    .setPayload(txParams.proposal.getPayload())
//                    .setTxid(txParams.txID)
//                    .setProposal(txParams.signedProp)
//                    .setChaincodeEvent(ccEvent)
//                    .setChannelId(txParams.channelID)
//                    .build();
//
//            ProposalResponsePackage.Response response = ProposalResponsePackage.Response.newBuilder()
//                .setStatus(200)
//                .setMessage("ChaincodeMessage_COMPLETED")
//                .build();

            //임시 prp만들어 내도록 설정, 수정 혹은 삭제해줘야 됨
//            ByteString cceventBytes = createCCEventBytes(ccMsg.getChaincodeEvent());
//            Chaincode.ChaincodeID ccID = Chaincode.ChaincodeID.newBuilder().setName(txParams.namespaceID).setVersion(txParams.namespaceID).build();
//            ByteString prpBytes = proputils.getBytesProposalResponsePayload(txParams.proposal.toByteString(), response, ccMsg.getPayload(), cceventBytes, ccID);

            ProposalResponsePackage.ProposalResponse pResp = ProposalResponsePackage.ProposalResponse
                    .newBuilder()
                    .setVersion(1)
//                    .setPayload(prpBytes)
//                .setResponse(null)
                    .build();
            ProposalResponsePackage.Response res = callChaincode(txParams, chaincodeName, chaincodeInput);

            return res;
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
            return null;
        }
//        InvocationStubImpl invocationStub = new InvocationStubImpl(ChaincodeMessageFactory.newGetStateEventMessage(txParams.channelID, txParams.txID, "", "CAR9", );
//        Context context = new Context(invocationStub);
////        Car car = fabcar.queryCar(context, chaincodeInput.getArgs(1).toString());
//        Car car1 = fabcar.queryCar(context, "CAR9");
//        System.out.println(car1);
//        fabcar.changeCarOwner(context,"CAR9", "newowner1");
//        Car car2 = fabcar.queryCar(context, "CAR9");
//        System.out.println(car2);

    }

    public ProposalResponsePackage.Response callChaincode(TransactionParams txParams, String chaincodeName, Chaincode.ChaincodeInput chaincodeInput) throws InvalidProtocolBufferException {

        //fabric chaincode_support.go 의 execute 에서의 선언 copy
        ChaincodeShim.ChaincodeMessage ccMsg = ChaincodeShim.ChaincodeMessage.newBuilder()
                .setType(ChaincodeShim.ChaincodeMessage.Type.TRANSACTION)
                .setChannelId(txParams.channelID)
                .setChaincodeId(txParams.namespaceID)
                .setTxid(txParams.txID)
                .setProposal(txParams.signedProp)
                .setPayload(chaincodeInput.toByteString())
                .build();

        corfu_access.issueSnapshotToken();
        fabcar fabcar = new fabcar();

//        int chaincodeInputsize = chaincodeInput.getArgsList().size();
//        ListIterator<ByteString> chaincodeArgList =  chaincodeInput.getArgsList().listIterator();
//        List<ByteString> chaincodeList =  chaincodeInput.getArgsList();
//        Object[] temp = chaincodeInput.getArgsList().toArray();
//        String[] chaincodeArgs = new String[chaincodeInputsize];
//        for (int i = 0; chaincodeArgList.hasNext(); i++) {
//            chaincodeArgs[i] = chaincodeArgList.next().toStringUtf8();
//        }
        //InvocationTaskManager를 통해 chaincode routing
        //chaincode interaction
        ChaincodeInvocationTask invocationTask = new ChaincodeInvocationTask(ccMsg, ChaincodeShim.ChaincodeMessage.Type.TRANSACTION, cfc);
        ChaincodeStub stub = new InvocationStubImpl(ccMsg, corfu_access);
        org.sslab.fabric.chaincodeshim.shim.Chaincode.Response ccresp  = cfc.invoke(stub);

        byte[] tesmp = ccresp.getPayload();
        String tesmp1 = new String(tesmp);
        Object result = genson.deserialize(tesmp1, Object.class);
        System.out.println("받아온 value는!: " + result);

        ChaincodeShim.ChaincodeMessage ccMessage =  createCCMSG(ccresp, ccMsg, stub);
        ByteString cceventBytes = createCCEventBytes(ccMessage.getChaincodeEvent());
//        processChaincodeExecutionResult(txParams.namespaceID, txParams.txID, ccMessage);

        /*
        체인코드 simulation result 만들어 주는 코딩
        */
        Chaincode.ChaincodeID ccID = Chaincode.ChaincodeID.newBuilder()
                .setName(txParams.namespaceID)
                .setVersion(txParams.namespaceID)
                .build();
        ProposalResponsePackage.Response res = toProtoResponse(ccresp);
        ByteString prpBytes = getBytesProposalResponsePayload(txParams.proposalHash, res, cceventBytes, cceventBytes, ccID);
        System.out.println("getChaincodeEvent:" + ccMessage.getChaincodeEvent());

//        InvocationTaskManager invocationTaskManager = new InvocationTaskManager();
//        final ChaincodeInvocationTask task = new ChaincodeInvocationTask(ccMsg, ccMsg.getType(), this.outgoingMessage, this.chaincode);

//        invocationTaskManager.call

        Context context = new Context(stub);
        String key = chaincodeInput.getArgs(1).toStringUtf8();
        System.out.println("전달받은 키: " + key);
        System.out.println("전달받은 channelID: " + txParams.channelID);
        System.out.println("전달받은 chaincodeName: " + chaincodeName);

//        fabcar.createCar(context, key, key, key, key, key);
        ProposalResponsePackage.ProposalResponse pResp = ProposalResponsePackage.ProposalResponse
                .newBuilder()
                .setVersion(1)
                .setPayload(prpBytes)
                .setResponse(res)
                .build();
        corfu_access.commitTransaction(txParams.signedProp, pResp);

        return res;
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

    @SneakyThrows
    public UnpackedProposal unpackProposal(ProposalPackage.SignedProposal signedProposal) {
        ProposalPackage.Proposal proposal;
        Common.Header header;
        Common.ChannelHeader channelHeader;
        Common.SignatureHeader signatureHeader;
        ProposalPackage.ChaincodeHeaderExtension chaincodeHdrExt;
        ProposalPackage.ChaincodeProposalPayload payload;
        Chaincode.ChaincodeInvocationSpec chaincodeInvocationSpec;
        ProposalPackage.ChaincodeProposalPayload cpp;
//        byte[] sp = signedProposal.getProposalBytes().toByteArray();
        proposal = ProposalPackage.Proposal.parseFrom(signedProposal.getProposalBytes());
        header = Common.Header.parseFrom(proposal.getHeader());
        channelHeader = Common.ChannelHeader.parseFrom(header.getChannelHeader());
        signatureHeader = Common.SignatureHeader.parseFrom(header.getSignatureHeader());
        chaincodeHdrExt = ProposalPackage.ChaincodeHeaderExtension.parseFrom(channelHeader.getExtension());
        payload = ProposalPackage.ChaincodeProposalPayload.parseFrom(proposal.getPayload());
        chaincodeInvocationSpec = Chaincode.ChaincodeInvocationSpec.parseFrom(payload.getInput());
        cpp =  ProposalPackage.ChaincodeProposalPayload.parseFrom(proposal.getPayload());
        ProposalPackage.ChaincodeProposalPayload cppNoTransient =  ProposalPackage.ChaincodeProposalPayload.newBuilder()
                .setInput(cpp.getInput())
                .build();
        byte[] ppBytes = cppNoTransient.toByteArray();

        // The proposal hash is the hash of the concatenation of:
        // 1) The serialized Channel Header object
        // 2) The serialized Signature Header object
        // 3) The hash of the part of the chaincode proposal payload that will go to the tx
        // (ie, the parts without the transient data)
        MessageDigest propHash = MessageDigest.getInstance("SHA-256");
        byte[] total = new byte[header.getChannelHeader().toByteArray().length + header.getSignatureHeader().toByteArray().length + ppBytes.length];
        System.arraycopy(header.getChannelHeader().toByteArray(),0,total,0,header.getChannelHeader().toByteArray().length);
        System.arraycopy(header.getSignatureHeader().toByteArray(),0,total, header.getChannelHeader().toByteArray().length, header.getSignatureHeader().toByteArray().length);
        System.arraycopy(ppBytes,0, total, header.getSignatureHeader().toByteArray().length, ppBytes.length);

//        propHash.update(total);
//        propHash.update(header.getSignatureHeader().toByteArray());
//        propHash.update(ppBytes);

        UnpackedProposal unpackedProposal = new UnpackedProposal(chaincodeHdrExt.getChaincodeId().getName(), channelHeader,
                chaincodeInvocationSpec.getChaincodeSpec().getInput(),
                proposal, signatureHeader, signedProposal, propHash.digest(total));

        return unpackedProposal;
    }

//    @SneakyThrows
//    public UnpackedProposal unpackProposal(ProposalPackage.SignedProposal signedProposal) {
//        ProposalPackage.Proposal proposal;
//        Common.Header header;
//        Common.ChannelHeader channelHeader;
//        Common.SignatureHeader signatureHeader;
//        ProposalPackage.ChaincodeHeaderExtension chaincodeHdrExt;
//        ProposalPackage.ChaincodeProposalPayload payload;
//        Chaincode.ChaincodeInvocationSpec chaincodeInvocationSpec;
//        ProposalPackage.ChaincodeProposalPayload cpp;
////        byte[] sp = signedProposal.getProposalBytes().toByteArray();
//        proposal = ProposalPackage.Proposal.parseFrom(signedProposal.getProposalBytes());
//        header = Common.Header.parseFrom(proposal.getHeader());
//        channelHeader = Common.ChannelHeader.parseFrom(header.getChannelHeader());
//        signatureHeader = Common.SignatureHeader.parseFrom(header.getSignatureHeader());
//        chaincodeHdrExt = ProposalPackage.ChaincodeHeaderExtension.parseFrom(channelHeader.getExtension());
//        payload = ProposalPackage.ChaincodeProposalPayload.parseFrom(proposal.getPayload());
//        chaincodeInvocationSpec = Chaincode.ChaincodeInvocationSpec.parseFrom(payload.getInput());
//        cpp =  ProposalPackage.ChaincodeProposalPayload.parseFrom(proposal.getPayload());
//        ProposalPackage.ChaincodeProposalPayload cppNoTransient =  ProposalPackage.ChaincodeProposalPayload.newBuilder()
//                .setInput(cpp.getInput())
//                .build();
//        byte[] ppBytes = cppNoTransient.toByteArray();
//
//        // The proposal hash is the hash of the concatenation of:
//        // 1) The serialized Channel Header object
//        // 2) The serialized Signature Header object
//        // 3) The hash of the part of the chaincode proposal payload that will go to the tx
//        // (ie, the parts without the transient data)
//        MessageDigest propHash = MessageDigest.getInstance("SHA-256");
//        byte[] total = new byte[header.getChannelHeader().toByteArray().length + header.getSignatureHeader().toByteArray().length + ppBytes.length];
//        System.arraycopy(header.getChannelHeader().toByteArray(),0,total,0,header.getChannelHeader().toByteArray().length);
//        System.arraycopy(header.getSignatureHeader().toByteArray(),0,total, header.getChannelHeader().toByteArray().length, header.getSignatureHeader().toByteArray().length);
//        System.arraycopy(ppBytes,0, total, header.getSignatureHeader().toByteArray().length, ppBytes.length);
//
////        propHash.update(total);
////        propHash.update(header.getSignatureHeader().toByteArray());
////        propHash.update(ppBytes);
//
//        UnpackedProposal unpackedProposal = new UnpackedProposal(chaincodeHdrExt.getChaincodeId().getName(), channelHeader,
//                chaincodeInvocationSpec.getChaincodeSpec().getInput(),
//                proposal, signatureHeader, signedProposal, propHash.digest(total));
//
//        return unpackedProposal;
//    }
}





