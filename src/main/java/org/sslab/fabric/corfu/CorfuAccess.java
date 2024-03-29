package org.sslab.fabric.corfu;

import com.google.common.reflect.TypeToken;
import org.corfudb.protocols.logprotocol.MultiObjectSMREntry;
import org.corfudb.protocols.wireprotocol.ILogData;
import org.corfudb.protocols.wireprotocol.LogData;
import org.corfudb.protocols.wireprotocol.Token;

import org.corfudb.runtime.CorfuRuntime;
import org.corfudb.runtime.collections.CorfuTable;
import org.corfudb.runtime.object.transactions.OptimisticTransactionalContext;
import org.corfudb.runtime.object.transactions.TransactionalContext;
import org.corfudb.runtime.object.transactions.WriteSetInfo;
import org.corfudb.runtime.view.AddressSpaceView;
import org.corfudb.runtime.view.StreamsView;
import org.corfudb.runtime.view.stream.IStreamView;
//import org.hyperledger.fabric.protos.corfu.CorfuChaincodeShim;
import org.hyperledger.fabric.protos.ledger.rwset.kvrwset.KvRwset;
import org.hyperledger.fabric.protos.peer.ProposalPackage;
import org.hyperledger.fabric.protos.peer.ProposalResponsePackage;
import org.sslab.fabric.adapter.AdapterModuleService;
import org.sslab.fabric.adapter.TransactionParams;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.Logger;

/**
 * @author Jeyoung Hwang.capricorn116@postech.ac.kr
 *         created on 2021. 9. 28.
 */
public class CorfuAccess {
    Map<UUID, CorfuRuntime> runtimes;
    Map<UUID, IStreamView> streamViews;
    Map<String, Long> lastReadAddrs;
    //tokenMap key: fabric txID, value: access token
    Map<String, Token> tokenMap;

    public CorfuAccess() {
        streamViews = new HashMap<UUID, IStreamView>();
        runtimes = new HashMap<UUID, CorfuRuntime>();
//        runtime = new CorfuRuntime(runtimeAddr[0]).connect();
        lastReadAddrs = new HashMap<String, Long>();
//        System.out.println("Init AdapterModuleService");
        tokenMap = new HashMap<String, Token>();
    }


    private static CorfuRuntime getRuntimeAndConnect(String configurationString) {
        CorfuRuntime corfuRuntime = new CorfuRuntime(configurationString).connect();
        return corfuRuntime;
    }
    CorfuRuntime runtime =  getRuntimeAndConnect("141.223.121.139:12011");
    private final Logger logger = Logger.getLogger(AdapterModuleService.class.getName());


//    public void getStringState(CorfuChaincodeShim.CorfuGetState corfuGetState, StreamObserver<CorfuChaincodeShim.CorfuGetStateResponse> responseObserver) {
//        Token current_token = runtime.getSequencerView().query().getToken();
//        long blockNum = current_token.getSequence();
//
//
//        String channelID = "mychannel";
//        String chaincodeID = "fabcar";
//        System.out.println("channelID:" + channelID);
//        System.out.println("chaincodeID:" + chaincodeID);
//
//
//        Map<String, byte[]> map = runtime.getObjectsView()
//                .build()
//                .setStreamName(channelID + chaincodeID)     // stream ID
//                .setTypeToken(new TypeToken<CorfuTable<String, byte[]>>() {
//                })
//                .open();
//
////        ByteString data = ByteString.copyFrom(map.get(objectKey));
//        byte[] value = map.get(corfuGetState.getKey());
//        if (value == null) {
//            System.out.println("[corfu-access-interface] {getState} null!!!!");
//
//        } else {
////            System.out.println(temp);
//            ByteString tempbs = ByteString.copyFrom(value);
//
//            System.out.println("[corfu-access-interface] {getState} success");
//        }
//        CorfuChaincodeShim.CorfuGetStateResponse response = CorfuChaincodeShim.CorfuGetStateResponse.newBuilder()
//                .setKeyBytes(ByteString.copyFrom(value))
//                .build();
//
//        responseObserver.onNext(response);
//        responseObserver.onCompleted();
//
//    }

    //local method call 전용 getstringstate
    public byte[] getStringState(String objectKey, String channelID, String chaincodeID) {
        System.out.println("channelID:" + channelID);
        System.out.println("chaincodeID:" + chaincodeID);

        Map<String, byte[]> map = runtime.getObjectsView()
                .build()
                .setStreamName(channelID+chaincodeID)     // stream ID
                .setTypeToken(new TypeToken<CorfuTable<String, byte[]>>() {
                })
                .open();

        byte[] value = map.get(objectKey);
        if (value == null) {
            System.out.println("[corfu-access-interface] {getState} null!!!!");
        } else {
            String car = new String(value);
            System.out.println("value: " + car);
            System.out.println("[corfu-access-interface] {getState} success");
        }
        return value;
    }


    public void putStringState(String objectKey, String channelID, String chaincodeID, byte[] data) {
        String Success_msg = "Putstate success ";

        Map<String, byte[]> map = runtime.getObjectsView()
                .build()
                .setStreamName(channelID+chaincodeID)     // stream ID
                .setTypeToken(new TypeToken<CorfuTable<String, byte[]>>() {
                })
                .open();
//        String test = "test";
//        byte[] testbytes = test.getBytes();
//        int destLength = testbytes.length + data.length;
//        byte[] dest = new byte[destLength];
//        System.arraycopy(testbytes, 0,dest,0,testbytes.length);
//        System.arraycopy(data, 0,dest,testbytes.length, data.length);

        map.put(objectKey, data);
        String car = new String(data);
        System.out.println("{putState}로 집어넣은 value: " + car);
        System.out.println("[corfu-access-interface] {putState} success");
    }

    public void issueSnapshotToken() {
        runtime.getObjectsView().TXBegin();
        System.out.println("[corfu-access-interface] {issueSnapshotToken} success");
    }

    public void commitTransaction(TransactionParams txParams, ProposalResponsePackage.ProposalResponse proposalResponse) {
        UUID streamID = runtime.getStreamID(txParams.channelID+txParams.namespaceID); //추후 실제 channel ID로 변
        System.out.println("UUID: " + streamID);


        OptimisticTransactionalContext transactionalContext = (OptimisticTransactionalContext) TransactionalContext.getCurrentContext();
        transactionalContext.setTxMetadata(proposalResponse.toByteArray());
        transactionalContext.setFabricProposal(txParams.signedProp.toByteArray());
        System.out.println("getTxMetadata();: " + transactionalContext.getTxMetadata());
        System.out.println("getFabricProposal();: " + transactionalContext.getFabricProposal());
        long appended_add = runtime.getObjectsView().TXEnd();

        System.out.println("[corfu-access-interface] {commitTransaction} appended_add is:" + appended_add);
        System.out.println("[corfu-access-interface] {commitTransaction} Corfu runtime is finished");
    }

    public void logSearch() {
        UUID streamID = runtime.getStreamID("mychannel" + "fabcar"); //추후 실제 channel ID로 변
        System.out.println("UUID: " + streamID);

        String metadataTest = "metadataTest";
        byte[] metaTXbytes = metadataTest.getBytes(StandardCharsets.UTF_8);
//        TransactionalContext.getCurrentContext().getWriteSetInfo().getWriteSet().setTxMetadata(metaTXbytes);
//        System.out.println("TransactionalContext.getCurrentContext().getWriteSetInfo().getWriteSet().getTxMetadata();: " + TransactionalContext.getCurrentContext().getWriteSetInfo().getWriteSet().getTxMetadata());
        long appended_add = runtime.getObjectsView().TXEnd();
        System.out.println("[corfu-access-interface] {commitTransaction} Appended global address: " + appended_add);
        System.out.println("[corfu-access-interface] {commitTransaction} Corfu runtime is finished");

        AddressSpaceView addressSpaceView = runtime.getAddressSpaceView();

//        IStreamView iStreamView = runtime.getStreamsView().get(streamID);
        StreamsView streamsView = new StreamsView(runtime);

        ILogData ilogData = addressSpaceView.read(appended_add);
        LogData logData = (LogData) ilogData;

        System.out.println(appended_add +  " log 조사 시작");
        System.out.println("[corfu-access-interface] {commitTransaction} appended_add: " + appended_add);
        System.out.println("streamsView.get(streamID).next(): " + streamsView.get(streamID).current());
        System.out.println("ilogData의 data: " + logData.getData());
        System.out.println("logData: " + ilogData.getPayload(runtime));
    }



}

