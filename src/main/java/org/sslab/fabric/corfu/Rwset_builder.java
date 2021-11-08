package org.sslab.fabric.corfu;

import bsp_transaction.BspTransactionOuterClass;
import com.google.protobuf.ByteString;
import org.hyperledger.fabric.protos.ledger.rwset.kvrwset.KvRwset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Rwset_builder {

    private ArrayList<BspTransactionOuterClass.KVRead> readArray = new ArrayList<BspTransactionOuterClass.KVRead>();
    private ArrayList<BspTransactionOuterClass.KVWrite> writeArray = new ArrayList<BspTransactionOuterClass.KVWrite>();

    public void AddToReadSet(String key, byte[] value, long sequence) {
        BspTransactionOuterClass.Version version = makeVersion(sequence);

        BspTransactionOuterClass.KVRead kvRead= BspTransactionOuterClass.KVRead.newBuilder()
                .setKey(key)
                .setValue(ByteString.copyFrom(value))
                .setVersion(version)
                .build();

        this.readArray.add(kvRead);
    }

    public void AddToWriteSet(String key, byte[] value, long sequence) {
        BspTransactionOuterClass.Version version = makeVersion(sequence);
        BspTransactionOuterClass.KVWrite kvWrite= BspTransactionOuterClass.KVWrite.newBuilder()
                .setKey(key)
                .setValue(ByteString.copyFrom(value))
                .setVersion(version)
                .build();

        this.writeArray.add(kvWrite);
    }
    public BspTransactionOuterClass.Version makeVersion(long sequence) {
        return BspTransactionOuterClass.Version.newBuilder()
                .setBlockNumber(sequence)
                .setTxOffset(0)
                .build();
    }

    public ArrayList<BspTransactionOuterClass.KVRead> getReadSet() { return readArray; }

    public ArrayList<BspTransactionOuterClass.KVWrite> getWriteSet() { return writeArray;}
}
