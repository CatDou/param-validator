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

import org.catdou.validate.exception.ParseException;
import org.catdou.validate.log.ValidatorLog;
import org.catdou.validate.log.ValidatorLogFactory;
import org.catdou.validate.model.config.CommonConfig;
import org.catdou.validate.model.config.UrlRuleBean;
import org.springframework.util.Assert;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.namespace.QName;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author James
 */
public class XmlParser {
    private static final ValidatorLog LOG = ValidatorLogFactory.getLogger(XmlParser.class);

    protected Document document;

    protected XPath xpath;

    public XmlParser(XmlDocument xmlDocument) {
        document = xmlDocument.getDocument();
        xpath = XmlDocument.getXpath();
    }

    public Object evaluate(String expression, Object item, QName returnType) {
        try {
            return xpath.evaluate(expression, item, returnType);
        } catch (XPathExpressionException e) {
            throw new ParseException("xpath evaluate error ", e);
        }
    }


}
