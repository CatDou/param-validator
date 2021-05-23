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

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.catdou.validate.exception.ParseException;
import org.catdou.validate.log.ValidatorLog;
import org.catdou.validate.log.ValidatorLogFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;

/**
 * @author James
 */
public class XmlDocument {
    private static final ValidatorLog LOG = ValidatorLogFactory.getLogger(XmlDocument.class);

    private Document document;

    private static XPath xpath;

    private static DocumentBuilder documentBuilder;

    static {
        initDocumentBuilder();
        initXpath();
    }

    public XmlDocument(File file) {
        try {
            document = documentBuilder.parse(file);
        } catch (SAXException | IOException e) {
            LOG.error("parse file " + file.getName() + " error " + ExceptionUtils.getStackTrace(e));
            throw new ParseException("parse file " + file.getName() + " error", e);
        }
    }

    private static void initXpath() {
        XPathFactory xPathFactory = XPathFactory.newInstance();
        xpath = xPathFactory.newXPath();
    }

    private static void initDocumentBuilder() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            documentBuilder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new ParseException("init document builder error ", e);
        }
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public static XPath getXpath() {
        return xpath;
    }

    public static void setXpath(XPath xpath) {
        XmlDocument.xpath = xpath;
    }

    public static DocumentBuilder getDocumentBuilder() {
        return documentBuilder;
    }

    public static void setDocumentBuilder(DocumentBuilder documentBuilder) {
        XmlDocument.documentBuilder = documentBuilder;
    }
}
