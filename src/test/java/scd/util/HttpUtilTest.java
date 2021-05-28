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

package scd.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.catdou.validate.io.FileResourcesUtils;
import org.catdou.validate.model.InputParam;
import org.catdou.validate.model.url.UrlPath;
import org.catdou.validate.util.HttpUtil;
import org.junit.Assert;
import org.junit.Test;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Locale;

/**
 * @author James
 */
public class HttpUtilTest {

    @Test
    public void testPathUrl() {
        String configUrl = "/url/{item}/{key}";
        String url = "/url/name/1";
        UrlPath configPath = HttpUtil.getUrlPath(configUrl);
        UrlPath urlPath = HttpUtil.getUrlPath(url);
        Assert.assertEquals(3, configPath.getPaths().size());
        Assert.assertEquals(2, configPath.getIndexs().size());
        Assert.assertEquals(3, urlPath.getPaths().size());
        Assert.assertEquals(0, urlPath.getIndexs().size());
        Assert.assertEquals("url", configPath.getPaths().get(0));
        Assert.assertEquals("{item}", configPath.getPaths().get(1));
        Assert.assertEquals("{key}", configPath.getPaths().get(2));
        Assert.assertEquals("url", urlPath.getPaths().get(0));
        Assert.assertEquals("name", urlPath.getPaths().get(1));
        Assert.assertEquals("1", urlPath.getPaths().get(2));
    }

    @Test
    public void testPathUrlTowSlash() {
        String configUrl = "//url/{item}//{key}";
        UrlPath configPath = HttpUtil.getUrlPath(configUrl);
        Assert.assertEquals(3,configPath.getPaths().size() );
        Assert.assertEquals(2,configPath.getIndexs().size());
        Assert.assertEquals("url", configPath.getPaths().get(0));
        Assert.assertEquals("{item}", configPath.getPaths().get(1));
        Assert.assertEquals("{key}", configPath.getPaths().get(2));
    }

    @Test
    public void testPathUrlEndWithSlash() {
        String configUrl = "//url/{item}//{key}/";
        UrlPath configPath = HttpUtil.getUrlPath(configUrl);
        Assert.assertEquals(3, configPath.getPaths().size());
        Assert.assertEquals(2, configPath.getIndexs().size());
        Assert.assertEquals("url", configPath.getPaths().get(0));
        Assert.assertEquals("{item}", configPath.getPaths().get(1));
        Assert.assertEquals("{key}", configPath.getPaths().get(2));
    }

    @Test
    public void testOneSlash() {
        String configUrl = "/url";
        UrlPath configPath = HttpUtil.getUrlPath(configUrl);
        Assert.assertEquals(1, configPath.getPaths().size());
    }

    @Test
    public void testNoSlash() {
        String configUrl = "url";
        UrlPath configPath = HttpUtil.getUrlPath(configUrl);
        Assert.assertEquals(1, configPath.getPaths().size());
    }

    @Test
    public void testPrintInfo() throws IOException {
        String filePath = "test.txt";
        HttpServletResponse response = createResponse(filePath);
        InputParam inputParam = new InputParam("address", "chengdu");
        String json = JSON.toJSONString(inputParam);
        HttpUtil.printInfo(response, json);
        File file = new File(filePath);
        InputParam deInput = JSONObject.parseObject(FileResourcesUtils.readStr(file), InputParam.class);
        Assert.assertEquals(json, JSON.toJSONString(deInput));
        FileUtils.forceDelete(file);
    }

    @Test
    public void testPrintObject() throws IOException {
        String filePath = "test.txt";
        HttpServletResponse response = createResponse(filePath);
        InputParam inputParam = new InputParam("address", "chengdu");
        HttpUtil.printObject(response, inputParam);
        File file = new File(filePath);
        InputParam deInput = JSONObject.parseObject(FileResourcesUtils.readStr(file), InputParam.class);
        Assert.assertEquals(JSON.toJSONString(inputParam), JSON.toJSONString(deInput));
        FileUtils.forceDelete(file);
    }

    private HttpServletResponse createResponse(String filePath) {
        HttpServletResponse response = new HttpServletResponse() {
            @Override
            public void addCookie(Cookie cookie) {
            }

            @Override
            public boolean containsHeader(String name) {
                return false;
            }

            @Override
            public String encodeURL(String url) {
                return null;
            }

            @Override
            public String encodeRedirectURL(String url) {
                return null;
            }

            @Override
            public String encodeUrl(String url) {
                return null;
            }

            @Override
            public String encodeRedirectUrl(String url) {
                return null;
            }

            @Override
            public void sendError(int sc, String msg) throws IOException {

            }

            @Override
            public void sendError(int sc) throws IOException {

            }

            @Override
            public void sendRedirect(String location) throws IOException {

            }

            @Override
            public void setDateHeader(String name, long date) {

            }

            @Override
            public void addDateHeader(String name, long date) {

            }

            @Override
            public void setHeader(String name, String value) {

            }

            @Override
            public void addHeader(String name, String value) {

            }

            @Override
            public void setIntHeader(String name, int value) {

            }

            @Override
            public void addIntHeader(String name, int value) {

            }

            @Override
            public void setStatus(int sc) {

            }

            @Override
            public void setStatus(int sc, String sm) {

            }

            @Override
            public int getStatus() {
                return 0;
            }

            @Override
            public String getHeader(String name) {
                return null;
            }

            @Override
            public Collection<String> getHeaders(String name) {
                return null;
            }

            @Override
            public Collection<String> getHeaderNames() {
                return null;
            }

            @Override
            public String getCharacterEncoding() {
                return null;
            }

            @Override
            public String getContentType() {
                return null;
            }

            @Override
            public ServletOutputStream getOutputStream() throws IOException {
                return null;
            }

            @Override
            public PrintWriter getWriter() throws IOException {
                return new PrintWriter(filePath);
            }

            @Override
            public void setCharacterEncoding(String charset) {

            }

            @Override
            public void setContentLength(int len) {

            }

            @Override
            public void setContentLengthLong(long length) {

            }

            @Override
            public void setContentType(String type) {

            }

            @Override
            public void setBufferSize(int size) {

            }

            @Override
            public int getBufferSize() {
                return 0;
            }

            @Override
            public void flushBuffer() throws IOException {

            }

            @Override
            public void resetBuffer() {

            }

            @Override
            public boolean isCommitted() {
                return false;
            }

            @Override
            public void reset() {

            }

            @Override
            public void setLocale(Locale loc) {

            }

            @Override
            public Locale getLocale() {
                return null;
            }
        };
        return response;
    }

}
