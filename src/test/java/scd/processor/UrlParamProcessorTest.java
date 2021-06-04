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
import org.catdou.validate.model.config.UrlRuleBean;
import org.catdou.validate.processor.UrlParamProcessor;
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
public class UrlParamProcessorTest extends BaseTest {

    private UrlParamProcessor urlParamProcessor;

    private String filePath = "data.txt";

    @Before
    public void init() {
        super.init();
        urlParamProcessor = new UrlParamProcessor();
        MockHttpServletResponse response = new MockHttpServletResponse(filePath);
        urlParamProcessor.setHttpServletResponse(response);
        urlParamProcessor.setParamConfig(paramConfig);
    }


    @Test
    public void testUrlParam() throws IOException {
        List<UrlRuleBean> urlRuleBeanList = paramConfig.getUrlRuleBeanList().stream()
                .filter(ruleBean -> "/param/url".equalsIgnoreCase(ruleBean.getUrl()))
                .collect(Collectors.toList());
        UrlRuleBean urlRuleBean = urlRuleBeanList.get(0);
        urlParamProcessor.setUrlRuleBean(urlRuleBean);
        MockHttpServletRequest mockHttpServletRequest = createMockHttpServletRequest();
        ServletRequestParamWrapper requestParamWrapper = new ServletRequestParamWrapper(mockHttpServletRequest, paramConfig);
        urlParamProcessor.setHttpServletRequest(requestParamWrapper);
        File file = new File(filePath);
        try {
            urlParamProcessor.validate();
            Assert.assertFalse(file.exists());
        } catch (Exception e) {
            Assert.fail("test url param error ");
        } finally {
            if (file.exists()) {
                FileUtils.forceDelete(file);
            }
        }
    }

    private MockHttpServletRequest createMockHttpServletRequest() {
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest("/param/url", "POST");
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("name","scd");
        paramMap.put("age","28");
        paramMap.put("score","99");
        mockHttpServletRequest.setUrlParamMap(paramMap);
        return mockHttpServletRequest;
    }
}
