/*
 * Copyright 2019 IBM DTCC All Rights Reserved.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.sslab.adapter.chaincodeshim.shim.ext.sbe.impl;

import org.sslab.adapter.chaincodeshim.shim.ext.sbe.StateBasedEndorsement;

/**
 * Factory for {@link StateBasedEndorsement} objects.
 */
public class StateBasedEndorsementFactory {
    private static StateBasedEndorsementFactory instance;

    /**
     *
     * @return Endorsement Factory
     */
    public static synchronized StateBasedEndorsementFactory getInstance() {
        if (instance == null) {
            instance = new StateBasedEndorsementFactory();
        }
        return instance;
    }

    /**
     * Constructs a state-based endorsement policy from a given serialized EP byte
     * array. If the byte array is empty, a new EP is created.
     *
     * @param ep serialized endorsement policy
     * @return New StateBasedEndorsement instance
     */
    public StateBasedEndorsement newStateBasedEndorsement(final byte[] ep) {
        return new StateBasedEndorsementImpl(ep);
    }
}
