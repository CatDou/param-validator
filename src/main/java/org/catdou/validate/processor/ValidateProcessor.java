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

package org.catdou.validate.processor;

import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.catdou.validate.UrlMatchHelper;
import org.catdou.validate.exception.ConfigException;
import org.catdou.validate.model.config.Param;
import org.catdou.validate.model.config.ParamConfig;
import org.catdou.validate.model.config.UrlRuleBean;
import org.catdou.validate.request.ServletRequestParamWrapper;
import org.springframework.util.CollectionUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author James
 */
public class ValidateProcessor {
    private ParamConfig paramConfig;

    private ServletRequestParamWrapper httpServletRequest;

    private HttpServletResponse httpServletResponse;

    private FilterChain filterChain;


    public ValidateProcessor(ParamConfig paramConfig, ServletRequestParamWrapper httpServletRequest,
                             HttpServletResponse httpServletResponse, FilterChain filterChain) {
        this.paramConfig = paramConfig;
        this.httpServletRequest = httpServletRequest;
        this.httpServletResponse = httpServletResponse;
        this.filterChain = filterChain;
    }

    public void validate() throws IOException, ServletException {
        List<UrlRuleBean> matchedList = new ArrayList<>();
        if (isNoCheckRequired(matchedList)) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }
        if (matchedList.size() > 1) {
            Collections.sort(matchedList);
            UrlRuleBean firstBean = matchedList.get(0);
            UrlRuleBean secondBean = matchedList.get(1);
            if (firstBean.getSort() == secondBean.getSort()) {
                throw new ConfigException("match same sort url " + firstBean.getUrl() + "," + secondBean.getUrl());
            }
        }
        UrlRuleBean urlRuleBean = matchedList.get(0);
        List<Param> urlParams = urlRuleBean.getUrlParams();
        if (!CollectionUtils.isEmpty(urlParams)) {
            UrlParamProcessor urlParamProcessor = new UrlParamProcessor();
            setBaseCommonProperties(urlParamProcessor, urlRuleBean);
            if (!urlParamProcessor.validate()) {
                return;
            }
        }
        List<Param> pathParams = urlRuleBean.getPathParams();
        if (!CollectionUtils.isEmpty(pathParams)) {
            PathParamProcessor pathParamProcessor = new PathParamProcessor();
            setBaseCommonProperties(pathParamProcessor, urlRuleBean);
            if (!pathParamProcessor.validate()) {
                return;
            }
        }
        List<Param> bodyParams = urlRuleBean.getBodyParams();
        if (!CollectionUtils.isEmpty(bodyParams)) {
            BodyParamProcessor bodyParamProcessor = new BodyParamProcessor();
            setBaseCommonProperties(bodyParamProcessor, urlRuleBean);
            if (!bodyParamProcessor.validate()) {
                return;
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private boolean isNoCheckRequired(List<UrlRuleBean> matchedList) {
        if (ServletFileUpload.isMultipartContent(httpServletRequest)) {
            return true;
        }
        String url = httpServletRequest.getServletPath();
        if (isWhiteUrl(url)) {
            return true;
        }
        String method = httpServletRequest.getMethod();
        List<UrlRuleBean> urlRuleBeanList = paramConfig.getMethodUrlMap().get(method);
        if (!CollectionUtils.isEmpty(urlRuleBeanList)) {
            for (UrlRuleBean urlRuleBean : urlRuleBeanList) {
                if (UrlMatchHelper.isMatch(urlRuleBean.getUrl(), url)) {
                    matchedList.add(urlRuleBean);
                }
            }
        }
        if (matchedList.size() == 0 ) {
            if (paramConfig.getCommonConfig().isCheckAllUrl()) {
                throw new ConfigException(url + " " + method + " not config check param");
            } else {
                return true;
            }
        }
        return false;
    }

    private boolean isWhiteUrl(String url) {
        List<String> whiteList = paramConfig.getCommonConfig().getWhiteList();
        if (!CollectionUtils.isEmpty(whiteList)) {
            for (String white : whiteList) {
                if (UrlMatchHelper.isMatch(white, url)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void setBaseCommonProperties(BaseParamProcessor baseCommonProperties, UrlRuleBean urlRuleBean) {
        baseCommonProperties.setHttpServletRequest(httpServletRequest);
        baseCommonProperties.setHttpServletResponse(httpServletResponse);
        baseCommonProperties.setParamConfig(paramConfig);
        baseCommonProperties.setUrlRuleBean(urlRuleBean);
    }
}
