package org.sslab.fabric.corfu;

import com.google.common.reflect.TypeToken;
import com.google.protobuf.ByteString;
import org.corfudb.protocols.wireprotocol.Token;
import org.corfudb.runtime.CorfuRuntime;
import org.corfudb.runtime.collections.CorfuTable;
import org.corfudb.runtime.object.transactions.Transaction;
import org.corfudb.runtime.object.transactions.TransactionType;
import org.corfudb.runtime.view.stream.IStreamView;
import org.sslab.fabric.adapter.AdapterModuleService;


import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * @author Jeyoung Hwang.capricorn116@postech.ac.kr
 *         created on 2021. 9. 28.
 */
public class Corfu_access  {
    Map<UUID, CorfuRuntime> runtimes;
    Map<UUID, IStreamView> streamViews;
    Map<String, Long> lastReadAddrs;
    //tokenMap key: fabric txID, value: access token
    Map<String, Token> tokenMap;

//    public Corfu_access() {
//        streamViews = new HashMap<UUID, IStreamView>();
//        runtimes = new HashMap<UUID, CorfuRuntime>();
////        runtime = new CorfuRuntime(runtimeAddr[0]).connect();
//        lastReadAddrs = new HashMap<String, Long>();
////        System.out.println("Init AdapterModuleService");
//        tokenMap = new HashMap<String, Token>();
//    }



    private static CorfuRuntime getRuntimeAndConnect(String configurationString) {
        CorfuRuntime corfuRuntime = new CorfuRuntime(configurationString).connect();
        return corfuRuntime;
    }
    CorfuRuntime runtime = getRuntimeAndConnect("141.223.121.251:12011");
//    AddressSpaceView addressSpaceView = runtime.getAddressSpaceView();

    private final Logger logger = Logger.getLogger(AdapterModuleService.class.getName());



    //local method call 전용 getstringstate
    public byte[] getStringState(String objectKey, String channelID, String chaincodeID) {
//        CorfuRuntime runtime = getRuntimeAndConnect("141.223.121.251:12011");
            Map<String, byte[]> map = runtime.getObjectsView()
                .build()
                .setStreamName(channelID + chaincodeID)     // stream ID
                .setTypeToken(new TypeToken<CorfuTable<String, byte[]>>() {
                })
                .open();

        byte[] value = map.get(objectKey);
        if (value == null) {
            System.out.println("[corfu-access-interface] {getState} null!!!!");
            return value;
        } else {
//            System.out.println(temp);
            ByteString tempbs = ByteString.copyFrom(value);

            System.out.println("[corfu-access-interface] {getState} success");
            return value;
        }
    }


    public void putStringState(String objectKey, String channelID, String chaincodeID, byte[] data) {
        CorfuRuntime runtime = getRuntimeAndConnect("141.223.121.251:12011");
        Map<String, byte[]> map = runtime.getObjectsView()
                .build()
                .setStreamName(channelID + chaincodeID)     // stream ID
                .setTypeToken(new TypeToken<CorfuTable<String, byte[]>>() {
                })
                .open();

        map.put(objectKey, data);

        System.out.println("[corfu-access-interface] {putState} success");
    }

    public void issueSnapshotToken() {
        CorfuRuntime runtime = getRuntimeAndConnect("141.223.121.251:12011");
        Token current_token = runtime.getSequencerView().query().getToken();
        Transaction vCorfutx = runtime.getObjectsView()
                .TXBuild()
                .type(TransactionType.OPTIMISTIC)
                .runtime(runtime)
                .snapshot(current_token)
                .build();
        vCorfutx.begin();

//        tokenMap.put(request.getTemp(), current_token);

        System.out.println("[corfu-access-interface] {issueSnapshotToken} success");
    }

    public void commitTransaction() {
        CorfuRuntime runtime = getRuntimeAndConnect("141.223.121.251:12011");
//    public void commitTransaction(ReqCommit request, StreamObserver<ResCommit> responseObserver) {
//        System.out.println("[peer-interface] {commitTransaction} Corfu runtime is connected");
//        System.out.println("전송받은 request: " + request);
//        System.out.println(request.getTest());
        Token current_token = runtime.getSequencerView().query().getToken();
//        UUIfD streamID = CorfuRuntime.getStreamID("mychannel"); //추후 실제 channel ID로 변
//        IStreamView sv = streamViews.get(streamID);

        long appended_add = runtime.getObjectsView().TXEnd();


//            long logicalAddr = sv.append(request.toByteArray());
//        System.out.println("[peer-interface] {commitTransaction} ProposalResponse size: " + request.getSerializedSize());
//        System.out.println("[peer-interface] {commitTransaction} appended_add: " + appended_add);
//            System.out.println("[peer-interface] {commitTransaction} logical addr: " + logicalAddr);

//            if(logicalAddr >= 0) {

//            System.out.println("[peer-interface] {commitTransaction} return success");
//            }
//            else {
//                reply = ResCommit.newBuilder()
//                        .setSuccess(false)
//                        .build();
//                System.out.println("[peer-interface] {commitTransaction} return fail");
//            }


//        System.out.println("appended_add:" + appended_add);
        System.out.println("[peer-interface] {commitTransaction} Corfu runtime is finished");
    }
}
