/*
 * Copyright 2021 catdou
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package scd.logger;

import org.catdou.validate.log.jdk.JdkLogger;
import org.junit.Before;
import org.junit.Test;

/**
 * @author James
 */
public class JdkLoggerTest {
    private JdkLogger jdkLogger;

    @Before
    public void before() {
        jdkLogger = new JdkLogger(JdkLoggerTest.class);
    }

    @Test
    public void testLog() {
        jdkLogger.debug("hello ");
        jdkLogger.warn("hello ");
        jdkLogger.info("hello ");
        jdkLogger.error("hello ");
    }
}
