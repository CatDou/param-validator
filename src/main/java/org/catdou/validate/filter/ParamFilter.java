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

package org.catdou.validate.filter;

import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.catdou.validate.cache.ValidatorCache;
import org.catdou.validate.factory.ParamConfigFactory;
import org.catdou.validate.factory.ParamConfigLoader;
import org.catdou.validate.model.config.ParamConfig;
import org.catdou.validate.processor.ValidateProcessor;
import org.catdou.validate.request.ServletRequestParamWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author James
 */
public class ParamFilter implements Filter {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParamFilter.class);

    private ParamConfigFactory paramConfigFactory = new ParamConfigFactory();

    private ParamConfig paramConfig;

    private ValidatorCache validatorCache = new ValidatorCache();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LOGGER.info("load param config");
        String type = filterConfig.getInitParameter("type");
        String path = filterConfig.getInitParameter("path");
        ParamConfigLoader paramConfigLoader = paramConfigFactory.createParamConfigLoader(type);
        paramConfig = paramConfigLoader.loadParamConfig(path);
        paramConfig.setValidatorCache(validatorCache);
        paramConfig.init();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest && servletResponse instanceof HttpServletResponse) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
            if (!ServletFileUpload.isMultipartContent(httpServletRequest)) {
                ServletRequestParamWrapper servletRequestWapper = new ServletRequestParamWrapper(httpServletRequest, paramConfig);
                HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
                ValidateProcessor validateProcessor = new ValidateProcessor(paramConfig, servletRequestWapper, httpServletResponse, filterChain);
                validateProcessor.validate();
            }
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {
        LOGGER.info("destroy param config");
    }
}
