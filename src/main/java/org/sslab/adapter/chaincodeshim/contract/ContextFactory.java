/*
 * Copyright 2019 IBM All Rights Reserved.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.sslab.adapter.chaincodeshim.contract;

import org.sslab.adapter.chaincodeshim.shim.ChaincodeStub;

/**
 * Factory to create {@link Context} from {@link ChaincodeStub} by wrapping stub
 * with dynamic proxy.
 */
public final class ContextFactory {
    private static ContextFactory cf;

    /**
     *
     * @return ContextFactory
     */
    public static synchronized ContextFactory getInstance() {
        if (cf == null) {
            cf = new ContextFactory();
        }
        return cf;
    }

    /**
     *
     * @param stub
     * @return Context
     */
    public Context createContext(final ChaincodeStub stub) {
        final Context newContext = new Context(stub);
        return newContext;
    }

}
