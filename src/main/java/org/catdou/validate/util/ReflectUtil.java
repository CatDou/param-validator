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
import org.catdou.validate.handler.BaseTypeHandler;
import org.catdou.validate.handler.HandlerMapper;
import org.catdou.validate.type.ParamValidator;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author James
 */
public class ReflectUtil {
    private static final Map<Class<?>, Field[]> declaredFieldsCache = new ConcurrentHashMap<>(256);

    private static final Map<Class<?>, Map<String, Field>> declareMapFieldsCache = new ConcurrentHashMap<>(256);

    private static final Field[] EMPTY_FIELD_ARRAY = new Field[0];

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

    public static <T> T convertMapToObject(Map<String, String> map, Class<T> clazz) {
        try {
            T t = clazz.newInstance();
            Map<String, Field> stringFieldMap = findStrFieldMap(clazz);
            Set<Map.Entry<String, String>> entrySet = map.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                String filedName = entry.getKey();
                Field field = stringFieldMap.get(filedName);
                if (field != null && !Modifier.isFinal(field.getModifiers())) {
                    field.setAccessible(true);
                    BaseTypeHandler baseTypeHandler = HandlerMapper.getTypeHandler(field.getType());
                    field.set(t, baseTypeHandler.convertType(entry.getValue()));
                }
            }
            return t;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ReflectException("convert map to object error ", e);
        }
    }

    public static Map<String, Field> findStrFieldMap(Class<?> clazz) {
        Assert.notNull(clazz, "Class must not be null");
        Map<String, Field> stringFieldMap = declareMapFieldsCache.computeIfAbsent(clazz, k -> new HashMap<>());
        Class<?> searchType = clazz;
        while (Object.class != searchType && searchType != null) {
            Field[] fields = getDeclaredFields(searchType);
            for (Field field : fields) {
                field.setAccessible(true);
                if (!stringFieldMap.containsKey(field.getName())) {
                    stringFieldMap.put(field.getName(), field);
                }
            }
            searchType = searchType.getSuperclass();
        }
        return stringFieldMap;
    }

    private static Field[] getDeclaredFields(Class<?> clazz) {
        Assert.notNull(clazz, "Class must not be null");
        Field[] result = declaredFieldsCache.get(clazz);
        if (result == null) {
            try {
                result = clazz.getDeclaredFields();
                declaredFieldsCache.put(clazz, (result.length == 0 ? EMPTY_FIELD_ARRAY : result));
            }
            catch (Throwable ex) {
                throw new IllegalStateException("Failed to introspect Class [" + clazz.getName() +
                        "] from ClassLoader [" + clazz.getClassLoader() + "]", ex);
            }
        }
        return result;
    }
}
