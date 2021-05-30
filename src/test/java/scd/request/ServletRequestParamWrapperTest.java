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

package scd.request;

import org.catdou.validate.constant.ParamValidatorConstants;
import org.catdou.validate.request.ServletRequestParamWrapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import scd.base.BaseTest;
import scd.http.MockHttpServletRequest;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author James
 */
public class ServletRequestParamWrapperTest extends BaseTest {

    @Before
    public void init() {
        super.init();
    }

    @Test
    public void testGetBodyParam() {
        String bodyStr = "{\n" +
                "  \"id\": 1,\n" +
                "  \"name\": \"scd\"\n" +
                "}";
        byte[] bodyByte = bodyStr.getBytes();
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put(ParamValidatorConstants.CONTENT_TYPE_KEY, ParamValidatorConstants.JSON_CONTENT_TYPE);
        ServletRequestParamWrapper servletRequestParamWrapper = getServletRequestParamWrapper(bodyByte, headerMap);
        byte[] getByte = servletRequestParamWrapper.getBodyParam();
        Assert.assertNotNull(bodyByte);
        Assert.assertEquals(bodyByte.length, getByte.length);
    }

    private ServletRequestParamWrapper getServletRequestParamWrapper(byte[] bodyByte, Map<String, String> headerMap) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bodyByte);
        request.setByteArrayInputStream(inputStream);
        request.setHeaderParamMap(headerMap);
        return new ServletRequestParamWrapper(request, paramConfig);
    }

    @Test
    public void testGetBodyParamStr() {
        String bodyStr = "{\n" +
                "  \"id\": 1,\n" +
                "  \"name\": \"scd\"\n" +
                "}";
        byte[] bodyByte = bodyStr.getBytes();
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put(ParamValidatorConstants.CONTENT_TYPE_KEY, ParamValidatorConstants.JSON_CONTENT_TYPE);
        ServletRequestParamWrapper servletRequestParamWrapper = getServletRequestParamWrapper(bodyByte, headerMap);
        String paramStr = servletRequestParamWrapper.getBodyParamStr();
        Assert.assertNotNull(paramStr);
        Assert.assertEquals(bodyStr, paramStr);
    }

    @Test
    public void testGetBodySize() {
        String bodyStr = "{\n" +
                "  \"id\": 1,\n" +
                "  \"name\": \"scd\"\n" +
                "}";
        byte[] bodyByte = bodyStr.getBytes();
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put(ParamValidatorConstants.CONTENT_TYPE_KEY, ParamValidatorConstants.JSON_CONTENT_TYPE);
        ServletRequestParamWrapper servletRequestParamWrapper = getServletRequestParamWrapper(bodyByte, headerMap);
        int size = servletRequestParamWrapper.getBodySize();
        Assert.assertEquals(bodyStr.length(), size);
    }
}
