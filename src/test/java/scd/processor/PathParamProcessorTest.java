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
import org.catdou.validate.exception.ConfigException;
import org.catdou.validate.io.FileResourcesUtils;
import org.catdou.validate.model.config.UrlRuleBean;
import org.catdou.validate.processor.PathParamProcessor;
import org.catdou.validate.request.ServletRequestParamWrapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import scd.base.BaseTest;
import scd.http.MockHttpServletRequest;
import scd.http.MockHttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author James
 */
public class PathParamProcessorTest extends BaseTest {

    private PathParamProcessor pathParamProcessor;

    private String filePath = "data.txt";

    @Before
    public void init() {
        pathParamProcessor = new PathParamProcessor();
        super.init();
        MockHttpServletResponse response = new MockHttpServletResponse(filePath);
        pathParamProcessor.setHttpServletResponse(response);
        pathParamProcessor.setParamConfig(paramConfig);
    }

    @Test
    public void testManyRules() throws IOException {
        List<UrlRuleBean> urlRuleBeanList = paramConfig.getUrlRuleBeanList().stream()
                .filter(ruleBean -> "/param/{path}".equalsIgnoreCase(ruleBean.getUrl()))
                .collect(Collectors.toList());
        UrlRuleBean urlRuleBean = urlRuleBeanList.get(0);
        pathParamProcessor.setUrlRuleBean(urlRuleBean);
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest("/param/BODY", "POST");
        ServletRequestParamWrapper requestParamWrapper = new ServletRequestParamWrapper(mockHttpServletRequest, paramConfig);
        pathParamProcessor.setHttpServletRequest(requestParamWrapper);
        File file = new File(filePath);
        try {
            pathParamProcessor.validate();
            Assert.assertFalse(file.exists());
        } catch (Exception e) {
            Assert.fail("test path param error ");
        } finally {
            if (file.exists()) {
                FileUtils.forceDelete(file);
            }
        }
    }

    @Test
    public void testCheckError() throws IOException {
        List<UrlRuleBean> urlRuleBeanList = paramConfig.getUrlRuleBeanList().stream()
                .filter(ruleBean -> "/param/{path}".equalsIgnoreCase(ruleBean.getUrl()))
                .collect(Collectors.toList());
        UrlRuleBean urlRuleBean = urlRuleBeanList.get(0);
        pathParamProcessor.setUrlRuleBean(urlRuleBean);
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest("/param/SSS", "POST");
        ServletRequestParamWrapper requestParamWrapper = new ServletRequestParamWrapper(mockHttpServletRequest, paramConfig);
        pathParamProcessor.setHttpServletRequest(requestParamWrapper);
        File file = new File(filePath);
        try {
            pathParamProcessor.validate();
            Assert.assertTrue(file.exists());
            Assert.assertNotNull(FileResourcesUtils.readStr(file));
        } catch (Exception e) {
            Assert.fail("test path param error ");
        } finally {
            if (file.exists()) {
                FileUtils.forceDelete(file);
            }
        }
    }

    @Test
    public void testPathConfigError() throws IOException {
        List<UrlRuleBean> urlRuleBeanList = paramConfig.getUrlRuleBeanList().stream()
                .filter(ruleBean -> "/param/{path}/{key}".equalsIgnoreCase(ruleBean.getUrl()))
                .collect(Collectors.toList());
        UrlRuleBean urlRuleBean = urlRuleBeanList.get(0);
        pathParamProcessor.setUrlRuleBean(urlRuleBean);
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest("/param/BODY", "POST");
        ServletRequestParamWrapper requestParamWrapper = new ServletRequestParamWrapper(mockHttpServletRequest, paramConfig);
        pathParamProcessor.setHttpServletRequest(requestParamWrapper);
        File file = new File(filePath);
        try {
            ConfigException configException = Assert.assertThrows(ConfigException.class,
                    () -> pathParamProcessor.validate());
            Assert.assertEquals("config path error, input url /param/{path}/{key}", configException.getMessage());
        } catch (Exception e) {
            Assert.fail("test path param error ");
        } finally {
            if (file.exists()) {
                FileUtils.forceDelete(file);
            }
        }
    }
}
