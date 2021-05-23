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

package org.catdou.validate.log;

import org.catdou.validate.exception.ReflectException;
import org.catdou.validate.log.jdk.JdkLogger;
import org.catdou.validate.log.slf4j.Slf4jLogger;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author James
 */
public class ValidatorLogFactory {

    private static String findClazz;

    private static final Map<String, Constructor<? extends ValidatorLog>> LOG_MAP = new LinkedHashMap<>();

    static {
        LOG_MAP.put("org.slf4j.Logger", getConstructor(Slf4jLogger.class));
        LOG_MAP.put("java.util.logging.Logger", getConstructor(JdkLogger.class));
        LOG_MAP.forEach((className, constructor) -> {
            if (findClazz == null) {
                try {
                    findClazz = loadLoggerClazz(className);
                } catch (Exception e) {
                }
            }
        });
    }

    public static void setFindClazz(String logClazz) {
        loadLoggerClazz(logClazz);
        findClazz = logClazz;
    }

    public static ValidatorLog getLogger(Class<?> clazz) {
        try {
            return LOG_MAP.get(findClazz).newInstance(clazz);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new ReflectException("get logger reflect exception ", e);
        }
    }

    public static String loadLoggerClazz(String clazz) {
        try {
            Class.forName(clazz);
            return clazz;
        } catch (ClassNotFoundException e) {
            throw new ReflectException("class not found ", e);
        }
    }

    @SuppressWarnings("unchecked")
    public static Constructor<? extends ValidatorLog> getConstructor(Class<?> clazz) {
        try {
            return (Constructor<? extends ValidatorLog>) clazz.getConstructor(Class.class);
        } catch (NoSuchMethodException e) {
            throw new ReflectException("construct clazz error ", e);
        }
    }
}
