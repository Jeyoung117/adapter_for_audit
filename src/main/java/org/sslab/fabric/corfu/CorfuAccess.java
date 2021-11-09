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
import org.hyperledger.fabric.protos.common.Common;
import org.hyperledger.fabric.protos.ledger.rwset.kvrwset.KvRwset;
import org.hyperledger.fabric.protos.peer.ProposalPackage;
import org.hyperledger.fabric.protos.peer.ProposalResponsePackage;
import org.sslab.fabric.adapter.AdapterModuleService;

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
    static CorfuRuntime runtime =  getRuntimeAndConnect("141.223.121.139:12011");
    private final Logger logger = Logger.getLogger(AdapterModuleService.class.getName());

    //local method call 전용 getstringstate
    public byte[] getStringState(String objectKey, String channelID, String chaincodeID) {
        System.out.println("chaincodeID:" + chaincodeID);

        Map<String, byte[]> map = runtime.getObjectsView()
                .build()
                .setStreamName(chaincodeID)     // stream ID
                .setTypeToken(new TypeToken<CorfuTable<String, byte[]>>() {
                })
                .open();

        byte[] value = map.get(objectKey);
        if (value == null) {
            System.out.println("[corfu-access-interface] {getState} null!!!!");
            return null;
        } else {
            System.out.println("[corfu-access-interface] {getState} success");
        }
        return value;
    }


    public void putStringState(String objectKey, String channelID, String chaincodeID, byte[] data) {
        Map<String, byte[]> map = runtime.getObjectsView()
                .build()
                .setStreamName(chaincodeID)     // stream ID
                .setTypeToken(new TypeToken<CorfuTable<String, byte[]>>() {
                })
                .open();

        map.put(objectKey, data);
        System.out.println("[corfu-access-interface] {putState} success");
    }

    public Token issueSnapshotToken() {
        runtime.getObjectsView().TXBegin();
        Token snapshotTimestamp = TransactionalContext.getCurrentContext().getSnapshotTimestamp();
        System.out.println("[corfu-access-interface] {issueSnapshotToken} success");

        return snapshotTimestamp;
    }

    public void commitTransaction() {
        OptimisticTransactionalContext transactionalContext = (OptimisticTransactionalContext) TransactionalContext.getCurrentContext();
//        transactionalContext.setTxMetadata(envelope.toByteArray());
        String test_env = "test_env";
        transactionalContext.setTxMetadata(test_env.getBytes(StandardCharsets.UTF_8));
        transactionalContext.setFabricProposal(test_env.getBytes(StandardCharsets.UTF_8));
        long appended_add = runtime.getObjectsView().TXEnd();
        System.out.println("[corfu-access-interface] {commitTransaction} Corfu runtime is finished, appended address is " + appended_add);
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

