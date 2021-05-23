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

package org.catdou.validate.xml;

import org.catdou.validate.model.config.CheckRule;
import org.catdou.validate.model.config.Param;
import org.catdou.validate.model.config.UrlRuleBean;
import org.catdou.validate.util.ReflectUtil;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathConstants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author James
 */
public class UrlParamRuleXmlParser extends XmlParser {

    public UrlParamRuleXmlParser(XmlDocument xmlDocument) {
        super(xmlDocument);
    }

    public List<UrlRuleBean> parseUrlParamRuleXml() {
        NodeList urlNodes = (NodeList) super.evaluate("/urls/item", document, XPathConstants.NODESET);
        int lenu = urlNodes.getLength();
        List<UrlRuleBean> ruleBeanList = new ArrayList<>();
        for (int i = 0; i < lenu; i++) {
            Node childNode = urlNodes.item(i);
            if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                Map<String, String> nodeAttributes = parseAttributes(childNode);
                UrlRuleBean urlRuleBean = ReflectUtil.convertMapToObject(nodeAttributes, UrlRuleBean.class);
                Object urlParamNodes = super.evaluate("urlParams/param", childNode, XPathConstants.NODESET);
                if (urlParamNodes instanceof NodeList) {
                    List<Param> paramsList = parseParamList((NodeList) urlParamNodes);
                    urlRuleBean.setUrlParams(paramsList);
                }
                Object pathParamNodes = super.evaluate("pathParams/param", childNode, XPathConstants.NODESET);
                if (pathParamNodes instanceof NodeList) {
                    List<Param> paramsList = parseParamList((NodeList) pathParamNodes);
                    urlRuleBean.setPathParams(paramsList);
                }
                Object bodyParamsNodes = super.evaluate("bodyParams/param", childNode, XPathConstants.NODESET);
                if (bodyParamsNodes instanceof NodeList) {
                    List<Param> paramsList = parseParamList((NodeList) bodyParamsNodes);
                    urlRuleBean.setBodyParams(paramsList);
                }
                ruleBeanList.add(urlRuleBean);
            }
        }
        return ruleBeanList;
    }

    private List<Param> parseParamList(NodeList nodeList)  {
        int lenn = nodeList.getLength();
        List<Param> paramList = new ArrayList<>();
        for (int i = 0; i < lenn; i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Map<String, String> nodeAttributes = parseAttributes(node);
                Param param = ReflectUtil.convertMapToObject(nodeAttributes, Param.class);
                Object object = super.evaluate("rules/rule", node, XPathConstants.NODESET);
                if (object instanceof NodeList) {
                    NodeList ruleNodes = (NodeList) object;
                    List<CheckRule> rules = parseCheckRule(ruleNodes);
                    param.setRules(rules);
                }
                paramList.add(param);
            }
        }
        return paramList;
    }

    private List<CheckRule> parseCheckRule(NodeList nodeList) {
        int lenn = nodeList.getLength();
        List<CheckRule> checkRuleList = new ArrayList<>();
        for (int i = 0; i < lenn; i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Map<String, String> nodeAttributes = parseAttributes(node);
                CheckRule checkRule = ReflectUtil.convertMapToObject(nodeAttributes, CheckRule.class);
                checkRuleList.add(checkRule);
            }
        }
        return checkRuleList;
    }

    private Map<String, String> parseAttributes(Node n) {
        Map<String, String> map = new HashMap<>(16);
        NamedNodeMap attributeNodes = n.getAttributes();
        if (attributeNodes != null) {
            for (int i = 0; i < attributeNodes.getLength(); i++) {
                Node attribute = attributeNodes.item(i);
                String value = attribute.getNodeValue();
                map.put(attribute.getNodeName(), value);
            }
        }
        return map;
    }

}
