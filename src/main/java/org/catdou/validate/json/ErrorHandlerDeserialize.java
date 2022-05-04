/*
 * Copyright 2022 catdou
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

package org.catdou.validate.json;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import org.catdou.validate.handler.error.ErrorHandler;
import org.catdou.validate.util.ReflectUtil;

import java.lang.reflect.Type;

/**
 * @author James
 */
public class ErrorHandlerDeserialize implements ObjectDeserializer {
    @Override
    @SuppressWarnings("unchecked")
    public ErrorHandler deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        Object object = parser.parse(fieldName);
        if (object instanceof String) {
            Object instance = ReflectUtil.createNewInstance((String) object);
            if (instance instanceof ErrorHandler) {
                return (ErrorHandler) instance;
            }
        }
        return null;
    }

    @Override
    public int getFastMatchToken() {
        return 0;
    }
}
