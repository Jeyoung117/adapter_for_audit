/*
 * Copyright 2019 IBM All Rights Reserved.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.sslab.adapter.chaincodeshim.contract.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Default Contract.
 *
 * Class level annotation that defines the contract that is the default
 * contract, and as such invoke of the transaction functions does not need to be
 * qualified by the contract name
 */
@Retention(RUNTIME)
@Target(ElementType.TYPE)
public @interface Default {
}
