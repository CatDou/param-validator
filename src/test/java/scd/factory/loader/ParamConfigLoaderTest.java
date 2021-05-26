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

package scd.factory.loader;

import org.catdou.validate.constant.ParamValidatorConstants;
import org.catdou.validate.factory.ParamConfigLoader;
import org.catdou.validate.io.FileResourcesUtils;
import org.catdou.validate.model.config.ParamConfig;
import org.catdou.validate.model.config.UrlRuleBean;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.List;

/**
 * @author James
 */
public class ParamConfigLoaderTest {

    private ParamConfigLoader paramConfigLoader = new ParamConfigLoader() {

        @Override
        public List<UrlRuleBean> parseOneResource(Resource resource) throws IOException {
            return null;
        }

        @Override
        public ParamConfig loadParamConfig(String path) {
            return null;
        }
    };

    @Test
    public void testCommonResource() {
        Resource[] resources = FileResourcesUtils.loadResourceByPath("classpath*:json/**/validate_*.json");
        Resource resource = paramConfigLoader.getCommonResource(resources, ParamValidatorConstants.JSON_COMMON_NAME);
        Assert.assertNotNull(resource);
        Resource[] xmlResource = FileResourcesUtils.loadResourceByPath("classpath*:xml/**/validate_*.xml");
        Resource commonResource = paramConfigLoader.getCommonResource(xmlResource, ParamValidatorConstants.XML_COMMON_NAME);
        Assert.assertNotNull(commonResource);
    }
}
