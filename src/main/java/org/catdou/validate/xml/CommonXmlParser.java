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

import org.catdou.validate.model.config.CommonConfig;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathConstants;
import java.util.ArrayList;
import java.util.List;

/**
 * @author James
 */
public class CommonXmlParser extends XmlParser {

    public CommonXmlParser(XmlDocument xmlDocument) {
        super(xmlDocument);
    }

    public CommonConfig parseCommon() {
        CommonConfig commonConfig = new CommonConfig();
        Object checkAllUrl = evaluate("/common/checkAllUrl", document, XPathConstants.NODE);
        if (checkAllUrl instanceof Node) {
            String text = ((Node) checkAllUrl).getTextContent();
            commonConfig.setCheckAllUrl(Boolean.valueOf(text));
        }
        Object whiteList = evaluate("/common/whiteList", document, XPathConstants.NODE);
        if (whiteList instanceof Node) {
            List<String> strWhiteList = parseChildText((Node) whiteList);
            commonConfig.setWhiteList(strWhiteList);
        }
        Object maxBodySizeNode = evaluate("/common/maxBodySize", document, XPathConstants.NODE);
        if (maxBodySizeNode instanceof Node) {
            String text = ((Node) maxBodySizeNode).getTextContent();
            commonConfig.setMaxBodySize(Long.valueOf(text));
        }
        return commonConfig;
    }

    private List<String> parseChildText(Node node) {
        NodeList nodeList = node.getChildNodes();
        List<String> textList = new ArrayList<>();
        if (nodeList != null) {
            int lenn = nodeList.getLength();
            for (int i = 0; i < lenn; i++) {
                Node childNode = nodeList.item(i);
                if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                    textList.add(childNode.getTextContent());
                }
            }
        }
        return textList;
    }
}
