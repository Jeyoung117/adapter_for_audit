package org.sslab.fabric.corfu;

import com.google.common.reflect.TypeToken;
import com.google.protobuf.ByteString;
import io.grpc.stub.StreamObserver;
import org.corfudb.protocols.wireprotocol.Token;
import org.corfudb.runtime.CorfuRuntime;
import org.corfudb.runtime.collections.CorfuTable;
import org.corfudb.runtime.object.transactions.Transaction;
import org.corfudb.runtime.object.transactions.TransactionType;
import org.corfudb.runtime.view.AddressSpaceView;
import org.corfudb.runtime.view.stream.IStreamView;
//import org.hyperledger.fabric.protos.corfu.CorfuChaincodeShim;
import org.sslab.fabric.adapter.AdapterModuleService;

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
    CorfuRuntime runtime =  getRuntimeAndConnect("141.223.121.251:12011");
    AddressSpaceView addressSpaceView = runtime.getAddressSpaceView();

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
        Token current_token = runtime.getSequencerView().query().getToken();
        long blockNum = current_token.getSequence();

        System.out.println("channelID:" + channelID);
        System.out.println("chaincodeID:" + chaincodeID);


        Map<String, byte[]> map = runtime.getObjectsView()
                .build()
                .setStreamName(channelID + chaincodeID)     // stream ID
                .setTypeToken(new TypeToken<CorfuTable<String, byte[]>>() {
                })
                .open();

//        ByteString data = ByteString.copyFrom(map.get(objectKey));
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
        String Success_msg = "Putstate success ";

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
        Token current_token = runtime.getSequencerView().query().getToken();
        UUID streamID = CorfuRuntime.getStreamID("mychannel"); //추후 실제 channel ID로 변
        IStreamView sv = streamViews.get(streamID);

        long appended_add = runtime.getObjectsView().TXEnd();



//            long logicalAddr = sv.append(request.toByteArray());
        System.out.println("[corfu-access-interface] {commitTransaction} ProposalResponse size: " + request.getSerializedSize());
        System.out.println("[corfu-access-interface] {commitTransaction} appended_add: " + appended_add);
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
        System.out.println("[corfu-access-interface] {commitTransaction} Corfu runtime is finished");
    }
}
