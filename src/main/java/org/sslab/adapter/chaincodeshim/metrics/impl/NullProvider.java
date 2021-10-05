/*
 * Copyright 2019 IBM All Rights Reserved.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.sslab.adapter.chaincodeshim.metrics.impl;

import org.sslab.adapter.chaincodeshim.metrics.MetricsProvider;

/**
 * Very simple provider that does absolutely nothing. Used when metrics are
 * disabled.
 *
 */
public class NullProvider implements MetricsProvider {

    /**
     *
     */
    public NullProvider() {
    }

}
