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

package org.catdou.validate.util;

import org.catdou.validate.exception.ConfigException;
import org.catdou.validate.exception.ReflectException;
import org.catdou.validate.type.ParamValidator;

/**
 * @author James
 */
public class ReflectUtil {

    public static Object createNewInstance(String clazz) {
        try {
            return Class.forName(clazz).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            throw new ReflectException("create new instance error ", e);
        }
    }

    public static ParamValidator createValidator(String clazz) {
        Object object = createNewInstance(clazz);
        if (object instanceof ParamValidator) {
            return (ParamValidator) object;
        } else {
            throw new ConfigException("config define clazz " + clazz + " error");
        }
    }
}
