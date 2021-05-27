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

package scd.util;

import org.catdou.validate.model.config.Param;
import org.catdou.validate.type.ParamValidator;
import org.catdou.validate.util.ReflectUtil;
import org.junit.Assert;
import org.junit.Test;
import scd.util.define.DefineParamValidator;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author James
 */
public class ReflectUtilTest {

    @Test
    public void testCreateNewInstance() {
        Object object = ReflectUtil.createNewInstance(ReflectUtilTest.class.getName());
        Assert.assertTrue(object instanceof ReflectUtilTest);
    }

    @Test
    public void testCreateValidator() {
        ParamValidator paramValidator = ReflectUtil.createValidator(DefineParamValidator.class.getName());
        Assert.assertNotNull(paramValidator);
    }

    @Test
    public void testfindStrFieldMap() {
        Map<String, Field> map = ReflectUtil.findStrFieldMap(Param.class);
        Assert.assertNotNull(map);
        Assert.assertNotNull(map.get("name"));
        Assert.assertNotNull(map.get("rules"));
        Assert.assertNotNull(map.get("nullable"));
    }

    @Test
    public void testConvertMapToObject() {
        Map<String, Object> dataMap = createDataMap();
        Param param = ReflectUtil.convertMapToObject(dataMap, Param.class);
        Assert.assertTrue(param.isNullable());
        Assert.assertEquals("scd", param.getName());
    }

    private Map<String, Object> createDataMap() {
        Map<String, Object> map = new HashMap<>(16);
        map.put("name", "scd");
        map.put("nullable", true);
        return map;
    }
}
