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
import org.catdou.validate.factory.JsonConfigLoader;
import org.catdou.validate.io.FileResourcesUtils;
import org.catdou.validate.model.config.CommonConfig;
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
public class JsonConfigLoaderTest  {
    private JsonConfigLoader jsonConfigLoader = new JsonConfigLoader();

    @Test
    public void testParseCommon() throws IOException {
        Resource[] xmlResource = FileResourcesUtils.loadResourceByPath("classpath*:xml/**/validate_*.xml");
        Resource commonResource = jsonConfigLoader.getCommonResource(xmlResource, ParamValidatorConstants.XML_COMMON_NAME);
        Assert.assertNotNull(commonResource);

    }

    @Test
    public void testParseParamConfig() throws IOException {
        ParamConfig paramConfig = jsonConfigLoader.loadParamConfig("classpath*:json/**/validate_*.json");
        Assert.assertNotNull(paramConfig);
        Assert.assertNotNull(paramConfig.getCommonConfig());
        CommonConfig commonConfig = paramConfig.getCommonConfig();
        Assert.assertFalse(commonConfig.isCheckAllUrl());
        Assert.assertEquals(3, commonConfig.getWhiteList().size());
        Assert.assertEquals(100000, commonConfig.getMaxBodySize().intValue());
        List<UrlRuleBean> urlRuleBeanList = paramConfig.getUrlRuleBeanList();
        Assert.assertTrue(urlRuleBeanList.size() > 0);
        Assert.assertNotNull(urlRuleBeanList.get(0).getUrl());
        Assert.assertNotNull(urlRuleBeanList.get(0).getMethod());
    }
}
