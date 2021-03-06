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

package org.catdou.validate.log.jdk;

import org.catdou.validate.log.ValidatorLog;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author James
 */
public class JdkLogger implements ValidatorLog {
    private Logger logger;

    public JdkLogger(Class<?> clazz) {
        logger = Logger.getLogger(clazz.getName());
    }

    @Override
    public void debug(String s) {
        logger.log(Level.FINE, s);
    }

    @Override
    public void warn(String s) {
        logger.log(Level.WARNING, s);
    }

    @Override
    public void info(String s) {
        logger.log(Level.INFO, s);
    }

    @Override
    public void error(String s) {
        logger.log(Level.SEVERE, s);
    }
}
