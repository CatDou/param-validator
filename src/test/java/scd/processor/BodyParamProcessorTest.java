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

package scd.processor;

import org.apache.commons.io.FileUtils;
import org.catdou.validate.io.FileResourcesUtils;
import org.catdou.validate.model.config.ParamConfig;
import org.catdou.validate.model.config.UrlRuleBean;
import org.catdou.validate.processor.BodyParamProcessor;
import org.catdou.validate.request.ServletRequestParamWrapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import scd.base.BaseTest;
import scd.http.MockHttpServletRequest;
import scd.http.MockHttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author James
 */
public class BodyParamProcessorTest extends BaseTest {
    private BodyParamProcessor bodyParamProcessor;

    private String filePath = "data.txt";

    private ParamConfig paramConfig;



    @Before
    public void init() {
        bodyParamProcessor = new BodyParamProcessor();
        MockHttpServletResponse response = new MockHttpServletResponse(filePath);
        bodyParamProcessor.setHttpServletResponse(response);
        super.init();
        this.paramConfig = super.paramConfig;
        bodyParamProcessor.setParamConfig(paramConfig);
    }


    @Test
    public void testJsonList() throws IOException {
        ServletRequestParamWrapper servletRequestParamWrapper = createServletRequest();
        bodyParamProcessor.setHttpServletRequest(servletRequestParamWrapper);
        List<UrlRuleBean> urlRuleBeanList = paramConfig.getUrlRuleBeanList().stream()
                .filter(urlRuleBean -> "/param/body".equalsIgnoreCase(urlRuleBean.getUrl()))
                .collect(Collectors.toList());
        bodyParamProcessor.setUrlRuleBean(urlRuleBeanList.get(0));
        File file = new File(filePath);
        try {
            bodyParamProcessor.validate();
            Assert.assertFalse(file.exists());
        } catch (Exception e) {
            Assert.fail("validate fail " + e);
        } finally {
            if (file.exists()) {
                FileUtils.forceDelete(file);
            }
        }
    }

    @Test
    public void testCheckError() throws IOException {
        ServletRequestParamWrapper servletRequestParamWrapper = createServletErrorRequest();
        bodyParamProcessor.setHttpServletRequest(servletRequestParamWrapper);
        List<UrlRuleBean> urlRuleBeanList = paramConfig.getUrlRuleBeanList().stream()
                .filter(urlRuleBean -> "/param/body".equalsIgnoreCase(urlRuleBean.getUrl()))
                .collect(Collectors.toList());
        bodyParamProcessor.setUrlRuleBean(urlRuleBeanList.get(0));
        File file = new File(filePath);
        try {
            bodyParamProcessor.validate();
            Assert.assertTrue(file.exists());
            Assert.assertNotNull(FileResourcesUtils.readStr(file));
        } catch (Exception e) {
            Assert.fail("validate fail " + e);
        } finally {
            if (file.exists()) {
                FileUtils.forceDelete(file);
            }
        }
    }

    @Test
    public void testCheckBigBody() {
        paramConfig.getCommonConfig().setMaxBodySize(10L);
        IllegalArgumentException argumentException = Assert.assertThrows(IllegalArgumentException.class, this::createServletRequest);
        Assert.assertEquals("the http body size is larger than the configuration value", argumentException.getMessage());
    }

    @Test
    public void testRuleBeanBig() {
        ServletRequestParamWrapper servletRequestParamWrapper = createServletErrorRequest();
        bodyParamProcessor.setHttpServletRequest(servletRequestParamWrapper);
        List<UrlRuleBean> urlRuleBeanList = paramConfig.getUrlRuleBeanList().stream()
                .filter(urlRuleBean -> "/param/body".equalsIgnoreCase(urlRuleBean.getUrl()))
                .collect(Collectors.toList());
        UrlRuleBean urlRuleBean = urlRuleBeanList.get(0);
        urlRuleBean.setMaxBodySize(10L);
        bodyParamProcessor.setUrlRuleBean(urlRuleBean);
        IllegalArgumentException argumentException = Assert.assertThrows(IllegalArgumentException.class, bodyParamProcessor::validate);
        Assert.assertEquals("the request body size is larger than the configuration value", argumentException.getMessage());
    }

    @Test
    public void testComplexJson() throws IOException {
        ServletRequestParamWrapper servletRequestParamWrapper = createComplexJsonRequest();
        bodyParamProcessor.setHttpServletRequest(servletRequestParamWrapper);
        List<UrlRuleBean> urlRuleBeanList = paramConfig.getUrlRuleBeanList().stream()
                .filter(urlRuleBean -> "/param/body".equalsIgnoreCase(urlRuleBean.getUrl()))
                .collect(Collectors.toList());
        bodyParamProcessor.setUrlRuleBean(urlRuleBeanList.get(0));
        File file = new File(filePath);
        try {
            bodyParamProcessor.validate();
            Assert.assertFalse(file.exists());
        } catch (Exception e) {
            Assert.fail("validate fail " + e);
        } finally {
            if (file.exists()) {
                FileUtils.forceDelete(file);
            }
        }
    }

    public ServletRequestParamWrapper createServletRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest("/param/body/1", "POST");
        Map<String, String> urlParamMap = new HashMap<>();
        String bodyStr = "[{\"id\":0,\"name\":\"scd\"},{\"id\":1,\"name\":\"scd\"},{\"id\":2,\"name\":\"scd\"},{\"id\":3,\"name\":\"scd\"},{\"id\":4,\"name\":\"scd\"}]";
        return super.createBodyServletRequest(request, urlParamMap, bodyStr);
    }

    public ServletRequestParamWrapper createServletErrorRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest("/param/body/1", "POST");
        Map<String, String> urlParamMap = new HashMap<>();
        String bodyStr = "[{\"id\":0,\"name\":\"scd\"},{\"id\":\"scd\",\"name\":\"scd\"},{\"id\":2,\"name\":\"scd\"},{\"id\":3,\"name\":\"scd\"},{\"id\":4,\"name\":\"scd\"}]";
        return super.createBodyServletRequest(request, urlParamMap, bodyStr);
    }

    public ServletRequestParamWrapper createComplexJsonRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest("/param/body/1", "POST");
        Map<String, String> urlParamMap = new HashMap<>();
        String bodyStr = "[{\"id\":0,\"list\":[{\"id\":0}],\"name\":\"scd\"},{\"id\":1,\"list\":[{\"id\":1}],\"name\":\"scd\"},{\"id\":2,\"list\":[],\"name\":\"scd\"},{\"id\":3,\"list\":[{\"id\":3}],\"name\":\"scd\"},{\"id\":4,\"list\":[{\"id\":4}],\"name\":\"scd\"}]";
        return super.createBodyServletRequest(request, urlParamMap, bodyStr);
    }

}
