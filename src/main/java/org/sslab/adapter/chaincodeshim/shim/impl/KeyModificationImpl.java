/*
 * Copyright 2019 IBM All Rights Reserved.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.sslab.adapter.chaincodeshim.shim.impl;

import com.google.protobuf.ByteString;
import org.hyperledger.fabric.protos.ledger.queryresult.KvQueryResult;
import org.sslab.adapter.chaincodeshim.shim.ledger.KeyModification;

import java.time.Instant;

public final class KeyModificationImpl implements KeyModification {

    private final String txId;
    private final ByteString value;
    private final Instant timestamp;
    private final boolean deleted;

    KeyModificationImpl(final KvQueryResult.KeyModification km) {
        this.txId = km.getTxId();
        this.value = km.getValue();
        this.timestamp = Instant.ofEpochSecond(km.getTimestamp().getSeconds(), km.getTimestamp().getNanos());
        this.deleted = km.getIsDelete();
    }

    @Override
    public String getTxId() {
        return txId;
    }

    @Override
    public byte[] getValue() {
        return value.toByteArray();
    }

    @Override
    public String getStringValue() {
        return value.toStringUtf8();
    }

    @Override
    public Instant getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean isDeleted() {
        return deleted;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (deleted ? 1231 : 1237);
        result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
        result = prime * result + ((txId == null) ? 0 : txId.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final KeyModificationImpl other = (KeyModificationImpl) obj;
        if (deleted != other.deleted) {
            return false;
        }
        if (!timestamp.equals(other.timestamp)) {
            return false;
        }
        if (!txId.equals(other.txId)) {
            return false;
        }
        if (!value.equals(other.value)) {
            return false;
        }
        return true;
    }

}
