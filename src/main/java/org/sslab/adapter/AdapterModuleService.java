package org.sslab.adapter;

import com.google.common.reflect.TypeToken;
import com.google.protobuf.ByteString;
import io.grpc.stub.StreamObserver;
import lombok.SneakyThrows;
import org.apache.commons.lang3.SerializationUtils;
import org.corfudb.protocols.wireprotocol.Token;
import org.corfudb.runtime.CorfuRuntime;
import org.corfudb.runtime.collections.CorfuTable;
import org.corfudb.runtime.object.CorfuCompileProxy;
import org.corfudb.runtime.object.ICorfuSMR;
import org.corfudb.runtime.object.ICorfuSMRProxyInternal;
import org.corfudb.runtime.object.transactions.Transaction;
import org.corfudb.runtime.object.transactions.TransactionType;
import org.corfudb.runtime.object.transactions.TransactionalContext;
import org.corfudb.runtime.view.AddressSpaceView;
import org.corfudb.runtime.view.ObjectsView;
import org.corfudb.runtime.view.StreamsView;
import org.corfudb.runtime.view.stream.IStreamView;
import org.hyperledger.fabric.protos.peer.*;
import org.hyperledger.fabric.sdk.ProposalResponse;

import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;
import java.util.Arrays;



import java.io.*;
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
    public void processProposal(ProposalResponsePackage1.ProposalResponse request, StreamObserver<ResCommit> responseObserver) {
//    public void commitTransaction(ReqCommit request, StreamObserver<ResCommit> responseObserver) {
//        System.out.println("[peer-interface] {commitTransaction} Corfu runtime is connected");
//        System.out.println("전송받은 request: " + request);
//        System.out.println(request.getTest());
        Token current_token = runtime.getSequencerView().query().getToken();
//        UUID streamID = CorfuRuntime.getStreamID("mychannel"); //추후 실제 channel ID로 변
//        IStreamView sv = streamViews.get(streamID);

        long appended_add = runtime.getObjectsView().TXEnd();


//            long logicalAddr = sv.append(request.toByteArray());
//        System.out.println("[peer-interface] {commitTransaction} ProposalResponse size: " + request.getSerializedSize());
//        System.out.println("[peer-interface] {commitTransaction} appended_add: " + appended_add);
//            System.out.println("[peer-interface] {commitTransaction} logical addr: " + logicalAddr);

        ResCommit reply;
//            if(logicalAddr >= 0) {
        reply = ResCommit.newBuilder()
//                    .setLogicalAddr(logicalAddr)
                .setSuccess(true)
                .build();
//            System.out.println("[peer-interface] {commitTransaction} return success");
//            }
//            else {
//                reply = ResCommit.newBuilder()
//                        .setSuccess(false)
//                        .build();
//                System.out.println("[peer-interface] {commitTransaction} return fail");
//            }

        responseObserver.onNext(reply);
        responseObserver.onCompleted();

//        System.out.println("appended_add:" + appended_add);
        System.out.println("[peer-interface] {commitTransaction} Corfu runtime is finished");
    }



    //commitTransaction version 3
    //Receive proposal response from peer

    @Override
    public void commitTransaction(ProposalResponsePackage1.ProposalResponse request, StreamObserver<ResCommit> responseObserver) {
//    public void commitTransaction(ReqCommit request, StreamObserver<ResCommit> responseObserver) {
//        System.out.println("[peer-interface] {commitTransaction} Corfu runtime is connected");
//        System.out.println("전송받은 request: " + request);
//        System.out.println(request.getTest());
        Token current_token = runtime.getSequencerView().query().getToken();
//        UUID streamID = CorfuRuntime.getStreamID("mychannel"); //추후 실제 channel ID로 변
//        IStreamView sv = streamViews.get(streamID);

        long appended_add = runtime.getObjectsView().TXEnd();


//            long logicalAddr = sv.append(request.toByteArray());
//        System.out.println("[peer-interface] {commitTransaction} ProposalResponse size: " + request.getSerializedSize());
//        System.out.println("[peer-interface] {commitTransaction} appended_add: " + appended_add);
//            System.out.println("[peer-interface] {commitTransaction} logical addr: " + logicalAddr);

            ResCommit reply;
//            if(logicalAddr >= 0) {
            reply = ResCommit.newBuilder()
//                    .setLogicalAddr(logicalAddr)
                    .setSuccess(true)
                    .build();
//            System.out.println("[peer-interface] {commitTransaction} return success");
//            }
//            else {
//                reply = ResCommit.newBuilder()
//                        .setSuccess(false)
//                        .build();
//                System.out.println("[peer-interface] {commitTransaction} return fail");
//            }

            responseObserver.onNext(reply);
            responseObserver.onCompleted();

//        System.out.println("appended_add:" + appended_add);
        System.out.println("[peer-interface] {commitTransaction} Corfu runtime is finished");
    }



    @Override
    public void issueSnapshotToken(ReqTxcont request, StreamObserver<SuccessResponse> responseObserver) {
//        System.out.println("[peer-interface] {issuSnapshotToken} Corfu runtime is connected");
        Token current_token = runtime.getSequencerView().query().getToken();
        Transaction vCorfutx = runtime.getObjectsView()
                .TXBuild()
                .type(TransactionType.OPTIMISTIC)
                .runtime(runtime)
                .snapshot(current_token)
                .build();
        vCorfutx.begin();

        tokenMap.put(request.getTemp(), current_token);
        String Success_msg = "Token 발급 완료";
//        System.out.println(Success_msg);

        SuccessResponse successResponse = SuccessResponse.newBuilder()
                .setSuccessMessage(Success_msg)
                .build();

        responseObserver.onNext(successResponse);
        responseObserver.onCompleted();
        System.out.println("[peer-interface] {issueSnapshotToken} success");
    }

    @Override
    public void getStringState(ReqGet request, StreamObserver<ResGet> responseObserver) {
        Token current_token = runtime.getSequencerView().query().getToken();
        long blockNum = current_token.getSequence();
        String channelID = request.getChannelID();
        String chaincodeID = request.getChaincodeID();
        String objectKey = request.getObjectname();

//        System.out.println("[peer-interface] {getstate} Corfu runtime is connected");
        Map<String, byte[]> map = runtime.getObjectsView()
                .build()
                .setStreamName(channelID + chaincodeID)     // stream ID
                .setTypeToken(new TypeToken<CorfuTable<String, byte[]>>() {
                })
                .open();

//        ByteString data = ByteString.copyFrom(map.get(objectKey));
        byte[] temp = map.get(objectKey);
        if (temp == null) {
            ResGet response = ResGet.newBuilder()
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
            System.out.println("[peer-interface] {getState} success");

        } else {
//            System.out.println(temp);
            ByteString tempbs = ByteString.copyFrom(temp);
            ResGet response = ResGet.newBuilder()
                    .setData(tempbs)
                    .setVersion(blockNum)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
            System.out.println("[peer-interface] {getState} success");
        }
    }

    @Override
    public void putStringState(ReqPut request, StreamObserver<ResPut> responseObserver) {
        String channelID = request.getChannelID();
        String chaincodeID = request.getChaincodeID();
        String objectKey = request.getObjectname();
        String Success_msg = "Putstate success ";

        System.out.println("[peer-interface] {putstate} Corfu runtime is connected");
        Map<String, byte[]> map = runtime.getObjectsView()
                .build()
                .setStreamName(channelID + chaincodeID)     // stream ID
                .setTypeToken(new TypeToken<CorfuTable<String, byte[]>>() {
                })
                .open();
        byte[] data = request.getData().toByteArray();
        map.put(objectKey, data);

        ResPut response = ResPut.newBuilder()
                .setSuccessmessage(Success_msg)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
        System.out.println("[peer-interface] {putState} success");
    }

    @Override
    public void getTransaction(ReqRead request, StreamObserver<ResRead> responseObserver) {
        Token current_token = runtime.getSequencerView().query().getToken();
        long blockNum = current_token.getSequence();
        UUID streamID = CorfuRuntime.getStreamID(request.getChannelID() + request.getChaincodeID() + request.getObjectname());
        IStreamView isv = runtime.getStreamsView().get(streamID);
        StreamsView sv = new StreamsView(runtime);

        System.out.println("[peer-interface] {getTransaction} Corfu runtime is connected");
        System.out.println("objectname: " + request.getObjectname());
        System.out.println("channelID: " + request.getChannelID());
        System.out.println("chaincodeID: " + request.getChaincodeID());
        System.out.println("corfuIndex: " + blockNum);


//        if (request.getChaincodeID().equals("_lifecycle")) {
//            ResRead response = ResRead.newBuilder()
//                    .setChaincodeID("_lifecycle")
//                    .build();
//
//            responseObserver.onNext(response);
//            responseObserver.onCompleted();
//
//        }
//        else {
        Map<String, byte[]> map1 = runtime.getObjectsView()
                .build()
                .setStreamName(request.getChannelID() + request.getChaincodeID())     // stream ID
                .setTypeToken(new TypeToken<CorfuTable<String, byte[]>>() {
                })
                .open();
        byte[] prbytes = new byte[]{};
        ByteString prbs;


        byte[] temp = map1.get(request.getObjectname());
        if (temp == null) {
            ResRead response = ResRead.newBuilder()
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } else {
            ByteString tempbs = ByteString.copyFrom(temp);
            ResRead response = ResRead.newBuilder()
                    .setVersion(blockNum)
                    .setTxContext(tempbs)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }


        System.out.println("[peer-interface] {getTransaction} success");
//        ILogData ldRead = addressSpaceView.read(request.getChannelID());
//        byte[] buffers = (byte[]) ldRead.getPayload(runtime);
//        Object payload = ldRead.getPayload(runtime);

//        }
    }

//    private TxResolutionInfo translate_conflictSetInfo(UUID txid) {
//        //conflictsetinfo 생성
//        ConflictSetInfo conflictSet = new ConflictSetInfo();
//
//        conflictSet.getConflicts();
//
//        ILogData ldRead1 = addressSpaceView.read(663);
////        Map<UUID, Set<byte[]> conflictMap = instantiateCorfuObject(CorfuTable.class, "conflictMap");
////        conflictSet.add();
//
//
//        Set<Object> objectSet = new HashSet<Object>();
//        objectSet.add(ldRead1.getPayload(runtime));
//
//        Token snapshotTimestamp = runtime.getSequencerView().query().getToken();
//        //sequencerServer.getSequencerEpoch()
//        //Token snapshotTimestamp = new Token(sequencerEpoch, globalTail);
////        long sequence = snapshotTimestamp.getSequence() -1L;
////        Token snapshotTimestamp1 = new Token(snapshotTimestamp.getEpoch(), sequence);
////        System.out.println(conflictSet);
//        Map<String, Integer> map1 = runtime.getObjectsView()
//                .build()
//                .setStreamName("A")     // stream name
//                .setTypeToken(new TypeToken<CorfuTable<String, Integer>>() {
//                })
//                .open();
//
//        Map<String, Integer> map2 = runtime.getObjectsView()
//                .build()
//                .setStreamName("B")     // stream name
//                .setTypeToken(new TypeToken<CorfuTable<String, Integer>>() {
//                })
//                .open();
//
//        runtime.getObjectsView().TXBegin();
//        Integer previous1 = map1.get("a");
//        Integer previous2 = map2.get("b");
//        System.out.println("Initial previous1:" + previous1);
//
//        if (previous2 == null) {
//            System.out.println("This is the first time we were run!");
//            map2.put("b", 1);
//            System.out.println(map1.get("a"));
//        } else {
//            ++previous1;
//            previous2 = 2;
//            map1.put("a", previous1);
//            map2.put("b", previous2);
////                map1.put("a", ++previous1);
//            System.out.println("This is the " + previous1 + " time we were run!");
//            System.out.println(map1.get("a"));
//            System.out.println(TransactionalContext.getCurrentContext().getReadSetInfo().getConflicts().keySet().stream().findFirst().get());
//            System.out.println(map2.get("b"));
//            System.out.println(TransactionalContext.getCurrentContext().getReadSetInfo().getConflicts().keySet().stream().findAny().get());
//            System.out.println(map1.get("c"));
//            System.out.println(TransactionalContext.getCurrentContext().getReadSetInfo().getConflicts().keySet().stream().findAny().get());
//            System.out.println(TransactionalContext.getCurrentContext().getReadSetInfo().getConflicts().keySet());
//            Set<ICorfuSMRProxyInternal> set = TransactionalContext.getCurrentContext().getReadSetInfo().getConflicts().keySet();
//            Iterator<ICorfuSMRProxyInternal> it = set.iterator();
//            while (it.hasNext()) { // hasNext() : 데이터가 있으면 true 없으면 false
//                System.out.println(it.next()); // next() : 다음 데이터 리턴
//            }
//
//            ILogData ldRead = addressSpaceView.read(927);
////                System.out.println(ldRead.getLogEntry(runtime));
////                TransactionalContext.getCurrentContext().getReadSetInfo().getConflicts();
////                System.out.println(TransactionalContext.getCurrentContext().getWriteSetInfo().add(streamID_1, ldRead(runtime)));
////                ILogData ldRead = addressSpaceView.read(800);
////                byte[] buffers1 = (byte[]) ldRead.getPayload(runtime);
//
////                String str1 = new String(buffers1);
////                System.out.println("ldRead.getLogEntry: " + ldRead.getLogEntry(runtime));
////                System.out.println("ldRead.getPayload: " + ldRead.getPayload(runtime));
////                System.out.println(str1);
////                System.out.println(TransactionalContext.getCurrentContext().getReadSetInfo().getConflicts().entrySet());
//        }
////
////
////
////        runtime.getObjectsView().TXEnd();
////
////
////        TxResolutionInfo txResolutionInfo = new TxResolutionInfo(txid, snapshotTimestamp, Collections.emptyMap(), Collections.emptyMap());
////
//        return txResolutionInfo;
//    }
//
//    private void translate_transaction () {
//            Token current_token = runtime.getSequencerView().query().getToken();
////        ILogData ldRead1 = addressSpaceView.read(request.getVersion());
////        Token token1 = ldRead1.getToken();
////        Map<String, Integer> map = runtime.getObjectsView()
////                .build()
////                .setStreamName(request.getChainID() + request.getChannelID())     // stream ID
////                .setTypeToken(new TypeToken<CorfuTable<String, Integer>>() {
////                })
////                .open();
//
//            Map<String, Integer> map1 = runtime.getObjectsView()
//                    .build()
//                    .setStreamName("A")     // stream ID
//                    .setTypeToken(new TypeToken<CorfuTable<String, Integer>>() {
//                    })
//                    .open();
//
//            Map<String, Integer> map2 = runtime.getObjectsView()
//                    .build()
//                    .setStreamName("B")     // stream ID
//                    .setTypeToken(new TypeToken<CorfuTable<String, Integer>>() {
//                    })
//                    .open();
//
//            Transaction vCorfutx = runtime.getObjectsView().TXBuild().type(TransactionType.OPTIMISTIC).runtime(runtime).snapshot(current_token).build();
//            vCorfutx.begin();
//
//            for (int i = 0; i <= 1; i++) { //vCorfu read-set translate 하는 부분
//                String streamID = "A";
//
//                if (i == 1) streamID = "B";
//
//                ObjectsView.ObjectID mapID = new ObjectsView.
//                        ObjectID(CorfuRuntime.getStreamID(streamID), CorfuTable.class);
//
//                CorfuCompileProxy corfuCompileProxy = ((CorfuCompileProxy) ((ICorfuSMR) runtime.getObjectsView().
//                        getObjectCache().
//                        get(mapID)).
//                        getCorfuSMRProxy());
//
//                Set<Object> objectSet1 = new HashSet<>();
//                String object_name = "a";
//                if (i == 1) object_name = "b";
//
//                objectSet1.add(object_name);
//                TransactionalContext.getCurrentContext().getReadSetInfo().getConflicts().put(corfuCompileProxy, objectSet1);
//
//            }
//
//            map1.put("a", 205);
//            System.out.println(map1.get("a"));
//
//            System.out.println("getWriteSetInfo():" + TransactionalContext.getCurrentContext().getWriteSetInfo());
//            System.out.println("getReadSetInfo():" + TransactionalContext.getCurrentContext().getReadSetInfo().getConflicts().values());
//            System.out.println("getReadSetInfo().getConflicts:" + TransactionalContext.getCurrentContext().getReadSetInfo().getConflicts());
//
//            long appended_add = runtime.getObjectsView().TXEnd();
//
//        }
//
//    private void translate_snapshotTimestamp () {
//
//    }

//    @Override
//    public void sendTxContext(Req_Append request, StreamObserver<Res_Append> responseObserver) {
//        //기본 configuration
////        UUID streamID = CorfuRuntime.getStreamID(request.getChainID());
//        UUID streamID = CorfuRuntime.getStreamID(request.getChainID());
//        IStreamView isv = runtime.getStreamsView().get(streamID);
//        StreamsView sv = new StreamsView(runtime);
//
//        long snapshotTimestamp = runtime.getSequencerView().query(streamID);
//        UUID uuid = UUID.randomUUID();
//        TxResolutionInfo txResolutionInfo = translate_conflictSetInfo(uuid);
//        String data = new String(request.getTxContext().toByteArray());
//
//
//        Car txcontext = new Car("MyChannel", "fabcar", "car1", "car2", 100);
//        byte[] serializedtxcon = SerializationUtils.serialize(txcontext);
//        Car deserializedtxcon = (Car) SerializationUtils.deserialize(serializedtxcon);
//        System.out.println("serializedtxcon: " + serializedtxcon.getClass());
//        ByteString yourByteString = ByteString.copyFrom(serializedtxcon);
//        System.out.println("bytestring of txcontext: " + yourByteString);
//        System.out.println("deserializedtxcon.getChannelID(): " + deserializedtxcon.getChannelID());
//
//        System.out.println("snapshotTimestamp: " + snapshotTimestamp);
//        System.out.println("request.getTxContext(): " + request.getTxContext().getClass());
//        System.out.println("request.getTxContext().toByteArray(): " + request.getTxContext().toByteArray().getClass());
//        System.out.println("request.getTxContext().toByteArray() 실제값: " + data);
//
//        Car txcontext1 = (Car) SerializationUtils.deserialize(request.getTxContext().toByteArray());
//        System.out.println("객체로 변환: " + txcontext1.getChannelID());
//
//
//
//        System.out.println("[client-interface] {sendTxContext} Corfu runtime is connected");
//
//        long logicalAddr = sv.append(request.getTxContext().toByteArray(), txResolutionInfo, CacheOption.WRITE_THROUGH, streamID);
//
//        System.out.println("append success");
//        ILogData ldRead = addressSpaceView.read(logicalAddr);
//        byte[] buffers = (byte[]) ldRead.getPayload(runtime);
//
//
//        String str = new String(request.getTxContext().toByteArray());
//        String str1 = new String(buffers);
//
//        //if(txContext.readset == empty)
////            long logicalAddr = sv.append(testPayload, null, streamID);
////        else
////            long logicalAddr = sv.append(testPayload, conflictSetInfo, streamID);
//
//        Res_Append resappend = Res_Append.newBuilder()
//                .setLogicalAddr(logicalAddr)
//                .setSuccess(true)
//                .build();
//
//        responseObserver.onNext(resappend);
//        responseObserver.onCompleted();
//    }

//    @SneakyThrows
//    @Override
//    public void sendTxContext(Req_Append request, StreamObserver<Res_Append> responseObserver) {
//        //기본 configuration
////        UUID streamID = CorfuRuntime.getStreamID(request.getChainID());
//        UUID streamID = CorfuRuntime.getStreamID("test1");
//        IStreamView isv = runtime.getStreamsView().get(streamID);
//        StreamsView sv = new StreamsView(runtime);
//
//        long snapshotTimestamp = runtime.getSequencerView().query(streamID);
//        System.out.println(snapshotTimestamp);
//
//
////        final Map<UUID, Set<byte[]>> conflictSet = new HashMap<UUID,Set<byte[]>>();
////        Set<byte[]> set = new HashSet<byte[]>();
////        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
////        buffer.putLong(631);
////
////        set.add(buffer.array());
//        UUID uuid = UUID.randomUUID();
//        //conflictSet.put(uuid, set);
////        TxResolutionInfo txResolutionInfo = translate_conflictSetInfo(uuid);
////
////        System.out.println(txResolutionInfo);
////        System.out.println(txResolutionInfo.getConflictSet());
////        System.out.println(txResolutionInfo.getWriteConflictParams());
////        System.out.println(txResolutionInfo.getSnapshotTimestamp());
////        System.out.println(txResolutionInfo.getTXid());
//
//        System.out.println("[client-interface] {sendTxContext} Corfu runtime is connected");
//        long logicalAddr = sv.append(request.getTxContext().toByteArray(), null, CacheOption.WRITE_THROUGH, streamID);
////        long logicalAddr = sv.append(request.getTxContext().toByteArray(), txResolutionInfo, CacheOption.WRITE_THROUGH, streamID);
////        long logicalAddr = isv.append("hello test");
//        System.out.println("append success");
//        ILogData ldRead = addressSpaceView.read(logicalAddr);
//        byte[] buffers = (byte[]) ldRead.getPayload(runtime);
////        Object payload = ldRead.getPayload(runtime);
////        ByteString bstr = ByteString.copyFrom((byte[])payload);
//
////        ByteArrayInputStream bis = new ByteArrayInputStream(request.getTxContext().toByteArray());
////        ObjectInput in = new ObjectInputStream(bis);
////        TxnContext txcontext = (TxnContext) in.readObject();
//
//        ByteArrayInputStream bis = new ByteArrayInputStream(request.getTxContext().toByteArray());
//        ObjectInput in = new ObjectInputStream(bis);
//        txContext txcontext = (txContext) in.readObject();
//
//        String str = new String(request.getTxContext().toByteArray());
//        String str1 = new String(buffers);
//
////        System.out.println(request.getTxContext().toByteArray());
////        System.out.println(str);
////        System.out.println(str1);
////        System.out.println(buffers);
////        System.out.println(payload);
////        System.out.println(bstr.toByteArray());
//        System.out.println(txcontext.getChannelID());
//        //if(txContext.readset == empty)
////            long logicalAddr = sv.append(testPayload, null, streamID);
////        else
////            long logicalAddr = sv.append(testPayload, conflictSetInfo, streamID);
//
//        Res_Append resappend = Res_Append.newBuilder()
//                .setLogicalAddr(logicalAddr)
//                .setSuccess(true)
//                .build();
//
//            responseObserver.onNext(resappend);
//            responseObserver.onCompleted();
//        }

////sendTransaction version 1
    @Override
    public void sendTransaction(ReqAppend request, StreamObserver<ResAppend> responseObserver) {
        System.out.println("[client-interface] {sendTransaction} Corfu runtime is connected");
        Token current_token = runtime.getSequencerView().query().getToken();

        System.out.println("channelID: " + request.getChannelID());
        System.out.println("chaincodeID: " + request.getChaincodeID());

        ByteString readsetbytestring = request.getReadset();
        ByteString writesetbytestring = request.getWriteset();

        String readsetstring =  readsetbytestring.toStringUtf8();
        String writesetstring =  writesetbytestring.toStringUtf8();

        readsetstring = readsetstring.replace("[", "");
        readsetstring = readsetstring.replace("]", "");

        writesetstring = writesetstring.replace("[", "");
        writesetstring = writesetstring.replace("]", "");
        String[] readsetarray = readsetstring.split(", ");
        String[] writesetkeyarray = writesetstring.split(", ");


//        System.out.println("writesetarray: "+ writesetarray[0]);
        System.out.println("readsetarray: "+ readsetarray[0]);
//        System.out.println("readsetarray: "+ readsetarray[0] +  readsetarray[1]+  readsetarray[2]+  readsetarray[3]);
//        ILogData ldRead1 = addressSpaceView.read(request.getVersion());
//        ILogData ldRead2 = addressSpaceView.read(85);
//        Token token1 = ldRead1.getToken();
//        Map<String, Integer> map = runtime.getObjectsView()
//                .build()
//                .setStreamName("A")     // stream ID
//                .setTypeToken(new TypeToken<CorfuTable<String, Integer>>() {
//                })
//                .open();

//issue snapshot에서 해주니까 일단 주석처리
//        Transaction vCorfutx = runtime.getObjectsView()
//                .TXBuild()
//                .type(TransactionType.OPTIMISTIC)
//                .runtime(runtime)
//                .snapshot(current_token)
//                .build();
//        vCorfutx.begin();

//        Set<Object> vCorfureadSet = Collections.singleton(new HashMap<Integer, Integer>());

        //vCorfu write-set translate 하는 부분
        for(int j =0; j < writesetkeyarray.length; j++) {
            Map<String, byte[]> map1 = runtime.getObjectsView()
                    .build()
                    .setStreamName(request.getChannelID() + request.getChaincodeID())     // stream ID
                    .setTypeToken(new TypeToken<CorfuTable<String, byte[]>>() {
                    })
                    .open();

            map1.put(writesetkeyarray[j], request.getTxContext().toByteArray());
        }
        //vCorfu read-set translate 하는 부분
        for(int i= 0; i < readsetarray.length; i++) {
            Map<String, byte[]> map = runtime.getObjectsView()
                    .build()
                    .setStreamName(request.getChannelID() + request.getChaincodeID())     // stream ID
                    .setTypeToken(new TypeToken<CorfuTable<String, byte[]>>() {
                    })
                    .open();

            map.get(writesetkeyarray[0]);
//            String streamID = request.getChannelID() + request.getChaincodeID();
//
//            ObjectsView.ObjectID mapID = new ObjectsView.
//                    ObjectID(CorfuRuntime.getStreamID(streamID), CorfuTable.class);
//
//            CorfuCompileProxy corfuCompileProxy = ((CorfuCompileProxy) ((ICorfuSMR) runtime.getObjectsView().
//                    getObjectCache().
//                    get(mapID)).
//                    getCorfuSMRProxy());

//            System.out.println(corfuCompileProxy);
//            TransactionalContext.getCurrentContext().getReadSetInfo().getConflicts().put(corfuCompileProxy, Collections.singleton(readsetarray[i]));
//            System.out.println(TransactionalContext.getCurrentContext().getReadSetInfo().getConflicts());
//            Map<ICorfuSMRProxyInternal, Set<Object>> set1 = TransactionalContext.getCurrentContext().getReadSetInfo().getConflicts().put(corfuCompileProxy, vCorfureadSet);
        }

//            Set<ICorfuSMRProxyInternal> set = TransactionalContext.getCurrentContext().getReadSetInfo().getConflicts().keySet();
//            Map<ICorfuSMRProxyInternal, Set<Object>> set1 = TransactionalContext.getCurrentContext().getReadSetInfo().getConflicts();
//            Iterator<ICorfuSMRProxyInternal> it = set.iterator();
//            while (it.hasNext()) { // hasNext() : 데이터가 있으면 true 없으면 false
//                System.out.println(it.next()); // next() : 다음 데이터 리턴
//            }
//        System.out.println("TransactionalContext.getCurrentContext().getReadSetInfo().getConflicts().values(): " +  TransactionalContext.getCurrentContext().getReadSetInfo().getConflicts().keySet());
//        System.out.println("vCorfu r/wset: " +  TransactionalContext.getCurrentContext().getTxnContext().);
        System.out.println("getWriteSetInfo().getConflicts: " + TransactionalContext.getCurrentContext().getWriteSetInfo().getConflicts());
        System.out.println("getReadSetInfo().getConflicts: " + TransactionalContext.getCurrentContext().getReadSetInfo().getConflicts());


        long appended_add = runtime.getObjectsView().TXEnd();
        System.out.println("appended_add:" + appended_add);

        // abort 났을 경우 예외처리 추가

        //아니라면 syncWSDB
////        syncWSDB();

                ResAppend resappend = ResAppend.newBuilder()
                .setSuccess(true)
                .build();

        responseObserver.onNext(resappend);
        responseObserver.onCompleted();

        System.out.println("[client-interface] {sendTransaction} success");
    }

//    //sendTransaction version 2
//@Override
//public void sendTransaction(ReqAppend request, StreamObserver<ResAppend> responseObserver) {
//    System.out.println("[client-interface] {sendTransaction} Corfu runtime is connected");
//    Token current_token = runtime.getSequencerView().query().getToken();
//    String channelID = request.getChannelID();
//    String chaincodeID = request.getChaincodeID();
////    UUID streamID_1 = CorfuRuntime.getStreamID(channelID+chaincodeID+request+request.);
//    System.out.println("channelID: " + channelID);
//    System.out.println("chaincodeID: " + chaincodeID);
//
//        Map<String, byte[]> map1 = runtime.getObjectsView()
//                .build()
//                .setStreamName(channelID)     // stream ID
//                .setTypeToken(new TypeToken<CorfuTable<String, byte[]>>() {
//                })
//                .open();
//
//        map1.put(channelID, request.getTxContext().toByteArray());
//        byte[] tempPR = request.getTxContext().toByteArray();
//    ByteArrayIntputSream bis = new ByteArrayInputStream(tempPR);
//
//
//        ProposalResponse proposalResponse = tempPR;
//    System.out.println("getWriteSetInfo().getConflicts: " + TransactionalContext.getCurrentContext().getWriteSetInfo().getConflicts());
//    System.out.println("getReadSetInfo().getConflicts: " + TransactionalContext.getCurrentContext().getReadSetInfo().getConflicts());
//    System.out.println("getReadSetInfo().getConflicts: " + TransactionalContext.getCurrentContext().getWriteSetInfo().getWriteSet().getEntryMap());
//
//
//    long appended_add = runtime.getObjectsView().TXEnd();
//    System.out.println("appended_add:" + appended_add);
//
//    // abort 났을 경우 예외처리 추가
//
//    //아니라면 syncWSDB
//////        syncWSDB();
//
//    ResAppend resappend = ResAppend.newBuilder()
//            .setSuccess(true)
//            .build();
//
//    responseObserver.onNext(resappend);
//    responseObserver.onCompleted();
//
//    System.out.println("[client-interface] {sendTransaction} success");
//}


//    @Override
//    public void checkVersion(ReqCheck request, StreamObserver<ResCheck> responseObserver) {
//        StreamsView sv = new StreamsView(runtime);
//        boolean result;
//
//        System.out.println("check 성공적으로 전달 받음");
//        System.out.println(request.getKey());
//        System.out.println(request.getKey().toStringUtf8());
//        UUID streamID = CorfuRuntime.getStreamID(request.getChannelID() + request.getChaincodeID() + request.getKey());
//
//        ResCheck response = ResCheck.newBuilder()
//                .setCheckresult(10)
//                .build();
//
//        responseObserver.onNext(response);
//        responseObserver.onCompleted();
//    }
}





