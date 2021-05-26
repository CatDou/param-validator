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

package org.catdou.validate.factory;

import org.catdou.validate.exception.ParseException;
import org.catdou.validate.log.ValidatorLog;
import org.catdou.validate.log.ValidatorLogFactory;
import org.catdou.validate.model.config.CommonConfig;
import org.catdou.validate.model.config.ParamConfig;
import org.catdou.validate.model.config.UrlRuleBean;
import org.catdou.validate.xml.CommonXmlParser;
import org.catdou.validate.xml.UrlParamRuleXmlParser;
import org.catdou.validate.xml.XmlDocument;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.List;

/**
 * @author James
 */
public class XmlConfigLoader implements ParamConfigLoader {
    private static final ValidatorLog LOGGER = ValidatorLogFactory.getLogger(JsonConfigLoader.class);

    private static final String COMMON_NAME = "validate_common_config.xml";

    @Override
    public List<UrlRuleBean> parseOneResource(Resource resource) throws IOException {
        XmlDocument xmlDocument = new XmlDocument(resource.getFile());
        UrlParamRuleXmlParser paramRuleXmlParser = new UrlParamRuleXmlParser(xmlDocument);
        return paramRuleXmlParser.parseUrlParamRuleXml();
    }

    @Override
    public ParamConfig loadParamConfig(String path) {
        try {
            return loadByPathMatchingResource(path);
        } catch (IOException e) {
            throw new ParseException("load param xml config error", e);
        }
    }

    public ParamConfig loadByPathMatchingResource(String path) throws IOException {
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resourcePatternResolver.getResources(path);
        Resource commonResource = getCommonResource(resources, COMMON_NAME);
        CommonConfig commonConfig = parseCommon(commonResource);
        LOGGER.info("load xml config file success");
        List<UrlRuleBean> allRuleBeanList = parseAllParamResource(resources, COMMON_NAME);
        LOGGER.info("load xml config file success");
        ParamConfig paramConfig = new ParamConfig();
        paramConfig.setCommonConfig(commonConfig);
        paramConfig.setUrlRuleBeanList(allRuleBeanList);
        return paramConfig;
    }

    private CommonConfig parseCommon(Resource resource) throws IOException {
        XmlDocument xmlDocument = new XmlDocument(resource.getFile());
        CommonXmlParser commonXmlParser = new CommonXmlParser(xmlDocument);
        return commonXmlParser.parseCommon();
    }
}
