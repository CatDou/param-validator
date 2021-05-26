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

package scd.str.json;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.catdou.validate.io.FileResourcesUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.Set;

/**
 * @author James
 */
public class JsonParseTest {

    @Test
    public void testJson() throws IOException {
        String path = "json/validate_rule_param_controller.json";
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resourcePatternResolver.getResources(path);
        String ruleJson = FileResourcesUtils.readStr(resources[0].getFile());
        Object obj = JSONObject.parse(ruleJson);
        if (obj instanceof JSONArray) {
            JSONArray jsonArray = (JSONArray) obj;
            int size = jsonArray.size();
            for (int i = 0; i < size; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Set<String> keySet = jsonObject.keySet();
                for (String key : keySet) {
                    Object dataObj = jsonObject.get(key);
                    if (dataObj instanceof JSONObject || dataObj instanceof JSONArray) {
                        continue;
                    }
                    Assert.assertNotNull(dataObj.toString());
                }
            }
        }
    }
}
