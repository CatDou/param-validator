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

import org.catdou.validate.constant.ParamValidatorConstants;
import org.catdou.validate.processor.ValidateProcessor;
import org.catdou.validate.request.ServletRequestParamWrapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import scd.base.BaseTest;
import scd.http.MockFilterChain;
import scd.http.MockHttpServletRequest;
import scd.http.MockHttpServletResponse;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author James
 */
public class ValidateProcessorTest extends BaseTest {
    private MockFilterChain filterChain;


    @Before
    public void init() {
        super.init();
        System.setSecurityManager(null);
        filterChain = new MockFilterChain();
    }

    @Test
    public void testValidateUrlParam() {
        String filePath = "data.txt";
        MockHttpServletResponse response = new MockHttpServletResponse(filePath);
        ServletRequestParamWrapper servletRequest = createServletRequest();
        ValidateProcessor validateProcessor = new ValidateProcessor(paramConfig, servletRequest, response, filterChain);
        try {
            validateProcessor.validate();
            File file = new File(filePath);
            Assert.assertFalse(file.exists());
        } catch (Exception e) {
            Assert.fail("validate fail " + e);
        }
    }

    public ServletRequestParamWrapper createServletRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest("/param/body/1", "POST");
        Map<String, String> urlParamMap = new HashMap<>();
        urlParamMap.put("taskId", "123");
        request.setUrlParamMap(urlParamMap);
        String bodyStr = "{\n" +
                "  \"id\": 1,\n" +
                "  \"name\": \"scd\"\n" +
                "}";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bodyStr.getBytes());
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put(ParamValidatorConstants.CONTENT_TYPE_KEY, ParamValidatorConstants.JSON_CONTENT_TYPE);
        request.setByteArrayInputStream(inputStream);
        request.setHeaderParamMap(headerMap);
        request.setUrlParamMap(urlParamMap);
        ServletRequestParamWrapper servletRequestParamWrapper = new ServletRequestParamWrapper(request, paramConfig);
        return servletRequestParamWrapper;
    }
}
