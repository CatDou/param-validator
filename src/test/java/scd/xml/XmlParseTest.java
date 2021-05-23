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

package scd.xml;

import org.catdou.validate.io.FileResources;
import org.catdou.validate.model.config.CommonConfig;
import org.catdou.validate.model.config.UrlRuleBean;
import org.catdou.validate.xml.CommonXmlParser;
import org.catdou.validate.xml.UrlParamRuleXmlParser;
import org.catdou.validate.xml.XmlDocument;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.Resource;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.List;

/**
 * @author James
 */
public class XmlParseTest {

    @Test
    public void testParseCommonXml() throws IOException {
        Resource[] resources = FileResources.loadResourceByPath("classpath*:xml/**/validate_common_config.xml");
        XmlDocument xmlDocument = new XmlDocument(resources[0].getFile());
        CommonXmlParser commonXmlParser = new CommonXmlParser(xmlDocument);
        CommonConfig commonConfig = commonXmlParser.parseCommon();
        Assert.assertFalse(commonConfig.isCheckAllUrl());
        Assert.assertEquals(3, commonConfig.getWhiteList().size());
        Assert.assertEquals(100000, commonConfig.getMaxBodySize().intValue());
    }

    @Test
    public void testParseParamXml() throws IOException {
        Resource[] resources = FileResources.loadResourceByPath("classpath*:xml/**/validate_rule_param_controller.xml");
        XmlDocument xmlDocument = new XmlDocument(resources[0].getFile());
        UrlParamRuleXmlParser paramRuleXmlParser = new UrlParamRuleXmlParser(xmlDocument);
        List<UrlRuleBean> urlRuleBeans = paramRuleXmlParser.parseUrlParamRuleXml();
        Assert.assertEquals(3, urlRuleBeans.size());
    }
}
