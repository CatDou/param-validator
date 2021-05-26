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

package scd.io;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.catdou.validate.io.FileResourcesUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * @author James
 */
public class FileResourceUtilsTest {

    @Test
    public void resourceLoad() {
        Resource[] resources = FileResourcesUtils.loadResourceByPath("classpath*:json/**/validate_*.json");
        boolean isFind = false;
        for(Resource resource : resources) {
            if (resource.getFilename().equals("validate_common_config.json")) {
                isFind = true;
                break;
            }
        }
        Assert.assertTrue(isFind);
    }


    @Test
    public void readJson() throws IOException {
        Resource[] resources = FileResourcesUtils.loadResourceByPath("classpath*:json/**/validate_*.json");
        for (Resource resource : resources) {
            String json = FileResourcesUtils.readStr(resource.getFile());
            Assert.assertTrue(StringUtils.hasText(json));
            Object object = JSONObject.parse(json);
            if (object instanceof JSONObject) {
                JSONObject jsonObject = (JSONObject) object;
                Assert.assertTrue(jsonObject.size() > 0);
            } else if (object instanceof JSONArray) {
                JSONArray jsonArray = (JSONArray) object;
                Assert.assertTrue(jsonArray.size() > 0);
            } else {
                Assert.fail("unknown type");
            }
        }
    }
}
