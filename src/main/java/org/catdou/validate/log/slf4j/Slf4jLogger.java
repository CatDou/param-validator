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

package org.catdou.validate.log.slf4j;

import org.catdou.validate.log.ValidatorLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author James
 */
public class Slf4jLogger implements ValidatorLog {
    private Logger LOGGER;

    public Slf4jLogger(Class<?> clazz) {
        LOGGER = LoggerFactory.getLogger(clazz);
    }


    @Override
    public void debug(String s) {
        LOGGER.debug(s);
    }

    @Override
    public void warn(String s) {
        LOGGER.warn(s);
    }

    @Override
    public void info(String s) {
        LOGGER.info(s);
    }

    @Override
    public void error(String s) {
        LOGGER.error(s);
    }
}
