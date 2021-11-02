package org.sslab.fabric.corfu;

import org.hyperledger.fabric.protos.ledger.rwset.kvrwset.KvRwset;

import java.util.HashMap;
import java.util.Map;

public class rwset_builder {
    Map<String, KvRwset.KVRead> readMap = new HashMap<String, KvRwset.KVRead>();
    Map<String, KvRwset.KVWrite> writeMap = new HashMap<String, KvRwset.KVWrite>();

    public rwset_builder(String ns) {

    }

    public void AddToReadSet(String ns, String key, long version) {
        KvRwset.KVRead = KvRwset.KVRead.newBuilder()
                .setKey(key)
                .setVersion(version)
                .build();

        this.readMap.put(key, )

    }

    public void AddToWriteSet(String ns, String key, long version) {
        Map<String, KvRwset.KVWrite> writeMap = new HashMap<String, KvRwset.KVWrite>();

    }
}
