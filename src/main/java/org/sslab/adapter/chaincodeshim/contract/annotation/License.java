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
 * Class level annotation that identifies this class as being a license object.
 * Can be populated to include name and url.
 *
 */
@Retention(RUNTIME)
@Target(ElementType.TYPE)
public @interface License {

    /**
     *
     * @return String
     */
    String name() default "";

    /**
     *
     * @return String
     */
    String url() default "";

}
