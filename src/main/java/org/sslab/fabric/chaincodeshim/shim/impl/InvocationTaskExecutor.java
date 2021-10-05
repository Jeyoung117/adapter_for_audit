/*
 * Copyright 2019 IBM DTCC All Rights Reserved.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.sslab.fabric.chaincodeshim.shim.impl;

import org.sslab.fabric.chaincodeshim.metrics.TaskMetricsCollector;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/**
 *
 *
 *
 */
public final class InvocationTaskExecutor extends ThreadPoolExecutor implements TaskMetricsCollector {
    private static Logger logger = Logger.getLogger(InvocationTaskExecutor.class.getName());

    /**
     *
     * @param corePoolSize
     * @param maximumPoolSize
     * @param keepAliveTime
     * @param unit
     * @param workQueue
     * @param factory
     * @param handler
     */
    public InvocationTaskExecutor(final int corePoolSize, final int maximumPoolSize, final long keepAliveTime, final TimeUnit unit,
            final BlockingQueue<Runnable> workQueue, final ThreadFactory factory, final RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, factory, handler);
        prestartCoreThread();
        logger.info("Thread pool created");
    }

    private final AtomicInteger count = new AtomicInteger();

    @Override
    protected void beforeExecute(final Thread thread, final Runnable task) {
        super.beforeExecute(thread, task);
        count.incrementAndGet();

    }

    @Override
    protected void afterExecute(final Runnable task, final Throwable throwable) {
        count.decrementAndGet();
        super.afterExecute(task, throwable);
    }

    @Override
    public int getCurrentTaskCount() {
        return count.get();
    }

    @Override
    public int getCurrentQueueCount() {
        return this.getQueue().size();
    }

}
