/*
 * Copyright 2019 IBM All Rights Reserved.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.sslab.adapter.chaincodeshim.contract.systemcontract;

import org.sslab.adapter.chaincodeshim.contract.Context;
import org.sslab.adapter.chaincodeshim.contract.ContractInterface;
import org.sslab.adapter.chaincodeshim.contract.annotation.Contract;
import org.sslab.adapter.chaincodeshim.contract.annotation.Info;
import org.sslab.adapter.chaincodeshim.contract.annotation.Transaction;
import org.sslab.adapter.chaincodeshim.contract.metadata.MetadataBuilder;

/**
 *
 */
@Contract(name = "org.hyperledger.fabric",
        info = @Info(title = "Fabric System Contract", description = "Provides information about the contracts within this container"))
public final class SystemContract implements ContractInterface {

    /**
     *
     */
    public SystemContract() {

    }

    /**
     *
     * @param ctx
     * @return Metadata
     */
    @Transaction(submit = false, name = "GetMetadata")
    public String getMetadata(final Context ctx) {
        final String jsonmetadata = MetadataBuilder.getMetadata();
        return jsonmetadata;
    }

}
