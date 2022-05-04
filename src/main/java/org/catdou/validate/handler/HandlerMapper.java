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

package org.catdou.validate.handler;

import org.catdou.validate.exception.ConfigException;
import org.catdou.validate.handler.error.ErrorHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * @author James
 */
public class HandlerMapper {
    private static Map<Class<?>, BaseTypeHandler> CLAZZ_MAP = new HashMap<>();

    static {
        CLAZZ_MAP.put(String.class, new StringTypeHandler());
        CLAZZ_MAP.put(Boolean.class, new BooleanTypeHandler());
        CLAZZ_MAP.put(Long.class, new LongTypeHandler());
        CLAZZ_MAP.put(boolean.class, new BooleanTypeHandler());
        CLAZZ_MAP.put(int.class, new IntegerTypeHandler());
        CLAZZ_MAP.put(ErrorHandler.class, new ErrorHandlerTypeHandler());
    }

    public static BaseTypeHandler getTypeHandler(Class<?> clazz) {
        BaseTypeHandler baseTypeHandler = CLAZZ_MAP.get(clazz);
        if (baseTypeHandler == null) {
            throw new ConfigException(clazz + " not found, please add it to CLAZZ_MAP ");
        }
        return baseTypeHandler;
    }
}
