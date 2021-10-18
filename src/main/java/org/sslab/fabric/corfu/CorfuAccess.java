package org.sslab.fabric.corfu;

import com.google.common.reflect.TypeToken;
import org.corfudb.protocols.logprotocol.LogEntry;
import org.corfudb.protocols.logprotocol.MultiObjectSMREntry;
import org.corfudb.protocols.logprotocol.MultiSMREntry;
import org.corfudb.protocols.logprotocol.SMREntry;
import org.corfudb.protocols.wireprotocol.ILogData;
import org.corfudb.protocols.wireprotocol.Token;
import org.corfudb.runtime.CorfuRuntime;
import org.corfudb.runtime.collections.CorfuTable;
import org.corfudb.runtime.view.AddressSpaceView;
import org.corfudb.runtime.view.StreamsView;
import org.corfudb.runtime.view.stream.IStreamView;
//import org.hyperledger.fabric.protos.corfu.CorfuChaincodeShim;
import org.sslab.fabric.adapter.AdapterModuleService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
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
    CorfuRuntime runtime =  getRuntimeAndConnect("141.223.121.251:13011");
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
                .setStreamName(channelID + chaincodeID)     // stream ID
                .setTypeToken(new TypeToken<CorfuTable<String, byte[]>>() {
                })
                .open();

        byte[] value = map.get(objectKey);
        if (value == null) {
            System.out.println("[corfu-access-interface] {getState} null!!!!");
        } else {
            System.out.println("[corfu-access-interface] {getState} success");
        }
        return value;
    }


    public void putStringState(String objectKey, String channelID, String chaincodeID, byte[] data) {
        String Success_msg = "Putstate success ";

        Map<String, byte[]> map = runtime.getObjectsView()
                .build()
                .setStreamName(channelID + chaincodeID)     // stream ID
                .setTypeToken(new TypeToken<CorfuTable<String, byte[]>>() {
                })
                .open();
        String test = "test";
        byte[] testbytes = test.getBytes();
        int destLength = testbytes.length + data.length;
        byte[] dest = new byte[destLength];
        System.arraycopy(testbytes, 0,dest,0,testbytes.length);
        System.arraycopy(data, 0,dest,testbytes.length, data.length);

        map.put(objectKey, dest);

        System.out.println("[corfu-access-interface] {putState} success");
    }

    public void issueSnapshotToken() {
        runtime.getObjectsView().TXBegin();
        System.out.println("[corfu-access-interface] {issueSnapshotToken} success");
    }

    public void commitTransaction() {
//        Token current_token = runtime.getSequencerView().query().getToken();
        AddressSpaceView addressSpaceView = runtime.getAddressSpaceView();
        UUID streamID = runtime.getStreamID("mychannel" + "fabcar"); //추후 실제 channel ID로 변
        IStreamView iStreamView = runtime.getStreamsView().get(streamID);
        StreamsView streamsView = new StreamsView(runtime);
        long appended_add = runtime.getObjectsView().TXEnd();
        iStreamView.seek(appended_add);
        ILogData ilogData = addressSpaceView.read(appended_add);
        LogEntry logEntry = ilogData.getLogEntry(runtime);
//        SMREntry smrEntry = ilogData.getLogEntry(runtime);
        MultiObjectSMREntry multiObjectSMREntry = (MultiObjectSMREntry) ilogData.getPayload(runtime);
        MultiSMREntry  multiSMREntry = multiObjectSMREntry.getEntryMap().entrySet().iterator().next().getValue();

//        for (Map.Entry<UUID, MultiSMREntry> multiSMREntry : multiObjectSMREntry.getEntryMap().entrySet()) {
//            for (SMREntry update : multiSMREntry.getValue().getUpdates()) {
//
//            }
//        }
//        MultiSMREntry multiSMREntry = (MultiSMREntry) ilogData.getLogEntry(runtime);
//        SMREntry smrEntry = (SMREntry) ilogData.getLogEntry(runtime);
        System.out.println(appended_add +  " log 조사 시작");
        System.out.println("[corfu-access-interface] {commitTransaction} appended_add: " + appended_add);
        System.out.println("streamsView.get(streamID).next(): " + streamsView.get(streamID).next());
        System.out.println("ilogData: " + ilogData);
        System.out.println("logData: " + ilogData.getPayload(runtime));
        System.out.println("type: " + ilogData.getType());
        System.out.println("ilogData.getLogEntry: " + ilogData.getLogEntry(runtime));
//        System.out.println("ilogData.getSerializedForm: " + ilogData.getSerializedForm().getSerialized());
        System.out.println("logData: " + multiObjectSMREntry.getSMRUpdates(streamID));
        System.out.println("multiSMREntry.getSMRUpdates(streamID;: " + multiSMREntry.getSMRUpdates(streamID).get(0));
        SMREntry smrEntry =  multiSMREntry.getSMRUpdates(streamID).get(0);

        System.out.println("smrEntry.getSMRArguments(): " + smrEntry.getSMRArguments());
        Object[] ob = Arrays.stream(smrEntry.getSMRArguments()).toArray();
//        Car car =
        System.out.println("smrEntry.getSMRArguments(): " + ob[0] + ob[1]);
        String temp = new String((byte[]) ob[1]);
        System.out.println("temp: " + temp);
        System.out.println("key: " + ob[0]);
        System.out.println("[corfu-access-interface] {commitTransaction} Corfu runtime is finished");
    }
}

