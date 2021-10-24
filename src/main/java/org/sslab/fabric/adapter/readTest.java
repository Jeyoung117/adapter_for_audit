//package org.sslab.fabric.adapter;
//
//import org.corfudb.protocols.logprotocol.LogEntry;
//import org.corfudb.protocols.logprotocol.MultiObjectSMREntry;
//import org.corfudb.protocols.logprotocol.MultiSMREntry;
//import org.corfudb.protocols.logprotocol.SMREntry;
//import org.corfudb.protocols.wireprotocol.ILogData;
//import org.corfudb.runtime.CorfuRuntime;
//import org.corfudb.runtime.object.transactions.TransactionalContext;
//import org.corfudb.runtime.view.AddressSpaceView;
//import org.corfudb.runtime.view.StreamsView;
//import org.corfudb.runtime.view.stream.IStreamView;
//
//import java.nio.charset.StandardCharsets;
//import java.util.Arrays;
//import java.util.UUID;
//
//public class readTest {
//    private static CorfuRuntime getRuntimeAndConnect(String configurationString) {
//        CorfuRuntime corfuRuntime = new CorfuRuntime(configurationString).connect();
//        return corfuRuntime;
//    }
//    static CorfuRuntime runtime =  getRuntimeAndConnect("141.223.121.251:12011");
//
//    public static void main(String[] args) {
//        UUID streamID = runtime.getStreamID("mychannel" + "fabcar"); //추후 실제 channel ID로 변
//        System.out.println("UUID: " + streamID);
//
//        long targetAddr = 30;
//        AddressSpaceView addressSpaceView = runtime.getAddressSpaceView();
//        ILogData ilogData = addressSpaceView.read(targetAddr);
//        IStreamView iStreamView = runtime.getStreamsView().get(streamID);
//        StreamsView streamsView = new StreamsView(runtime);
//
//        LogEntry logEntry = ilogData.getLogEntry(runtime);
//        MultiObjectSMREntry multiObjectSMREntry = (MultiObjectSMREntry) ilogData.getPayload(runtime);
//        MultiSMREntry multiSMREntry = multiObjectSMREntry.getEntryMap().entrySet().iterator().next().getValue();
//
//        System.out.println(targetAddr +  " log 조사 시작");
//        System.out.println("[corfu-access-interface] {commitTransaction} targetAddr: " + targetAddr);
//        System.out.println("streamsView.get(streamID).next(): " + streamsView.get(streamID).previous());
//        System.out.println("ilogData: " + ilogData);
//        System.out.println("logData: " + ilogData.getPayload(runtime));
//        System.out.println("txMetadata: " + ((MultiObjectSMREntry) ilogData.getPayload(runtime)));
////        System.out.println("ilogData.getSerializedForm: " + ilogData.getSerializedForm().getSerialized());
//        System.out.println("logData: " + multiObjectSMREntry.getSMRUpdates(streamID));
//        System.out.println("multiSMREntry.getSMRUpdates(streamID;: " + multiSMREntry.getSMRUpdates(streamID).get(0));
//        SMREntry smrEntry =  multiSMREntry.getSMRUpdates(streamID).get(0);
//
//        System.out.println("smrEntry.getSMRArguments(): " + smrEntry.getSMRArguments());
//        Object[] ob = Arrays.stream(smrEntry.getSMRArguments()).toArray();
////        Car car =
//        System.out.println("smrEntry.getSMRArguments(): " + ob[0] + ob[1]);
//        String temp = new String((byte[]) ob[1]);
//        System.out.println("value: " + temp);
//        System.out.println("key: " + ob[0]);
//    }
//}
