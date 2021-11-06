package org.sslab.fabric.corfu;

import com.google.protobuf.ByteString;
import org.hyperledger.fabric.protos.ledger.rwset.kvrwset.KvRwset;

import java.util.HashMap;
import java.util.Map;

public class Rwset_builder {
    Map<String, KvRwset.KVRead> readMap = new HashMap<String, KvRwset.KVRead>();
    Map<String, KvRwset.KVWrite> writeMap = new HashMap<String, KvRwset.KVWrite>();

    public Rwset_builder(String ns) {

    }

    public void AddToReadSet(String ns, String key, long version) {
        KvRwset.KVRead kvRead= KvRwset.KVRead.newBuilder()
                .setKey(key)
//                .setVersion(version)
                .build();

        this.readMap.put(key, kvRead);
    }

    public void AddToWriteSet(String ns, String key, ByteString value) {
        KvRwset.KVWrite kvWrite= KvRwset.KVWrite.newBuilder()
                .setKey(key)
                .setValue(value)
                .build();

        this.writeMap.put(key, kvWrite);

    }
}
