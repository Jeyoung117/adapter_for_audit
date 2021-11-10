package org.sslab.fabric.corfu;

import com.google.common.reflect.TypeToken;
import com.google.protobuf.ByteString;
import org.corfudb.protocols.wireprotocol.Token;
import org.corfudb.runtime.CorfuRuntime;
import org.corfudb.runtime.collections.CorfuTable;
import org.corfudb.runtime.object.transactions.OptimisticTransactionalContext;
import org.corfudb.runtime.object.transactions.Transaction;
import org.corfudb.runtime.object.transactions.TransactionType;
import org.corfudb.runtime.object.transactions.TransactionalContext;
import org.corfudb.runtime.view.stream.IStreamView;
import org.sslab.fabric.adapter.AdapterModuleService;


import java.nio.charset.StandardCharsets;
import java.util.HashMap;
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
    private final Logger logger = Logger.getLogger(AdapterModuleService.class.getName());
    static CorfuRuntime runtime;

    public Corfu_access(CorfuRuntime runtime) {
        streamViews = new HashMap<UUID, IStreamView>();
        runtimes = new HashMap<UUID, CorfuRuntime>();
        this.runtime = runtime;
        lastReadAddrs = new HashMap<String, Long>();
//        System.out.println("Init AdapterModuleService");
        tokenMap = new HashMap<String, Token>();
    }

//    private static CorfuRuntime getRuntimeAndConnect(String configurationString) {
//        CorfuRuntime corfuRuntime = new CorfuRuntime(configurationString).connect();
//        return corfuRuntime;
//    }
//    static CorfuRuntime runtime = getRuntimeAndConnect("141.223.121.251:12011");

    //local method call 전용 getstringstate
    public byte[] getStringState(String objectKey, String channelID, String chaincodeID) {
            Map<String, byte[]> map = runtime.getObjectsView()
                .build()
                .setStreamName(chaincodeID)     // stream ID
                .setTypeToken(new TypeToken<CorfuTable<String, byte[]>>() {
                })
                .open();

        byte[] value = map.get(objectKey);
        if (value == null) {
//            System.out.println("[corfu-access-interface] {getState} null!!!!");
            return null;
        } else {
//            System.out.println("[corfu-access-interface] {getState} success");
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
//        System.out.println("[corfu-access-interface] {issueSnapshotToken} success");

        return snapshotTimestamp;
    }

    public void commitTransaction() {
        OptimisticTransactionalContext transactionalContext = (OptimisticTransactionalContext) TransactionalContext.getCurrentContext();
//        transactionalContext.setTxMetadata(envelope.toByteArray());
//        String test_env = "test_env";
//        transactionalContext.setTxMetadata(test_env.getBytes(StandardCharsets.UTF_8));
//        transactionalContext.setFabricProposal(test_env.getBytes(StandardCharsets.UTF_8));
        long appended_add = runtime.getObjectsView().TXEnd();
        System.out.println("[peer-interface] {commitTransaction} Corfu runtime is finished");
    }
}
