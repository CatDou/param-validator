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


import org.catdou.validate.cache.ValidatorCache;
import org.catdou.validate.enums.ValidatorType;
import org.catdou.validate.exception.ConfigException;
import org.catdou.validate.model.InputParam;
import org.catdou.validate.model.ValidateResult;
import org.catdou.validate.model.config.CheckRule;
import org.catdou.validate.model.config.Param;
import org.catdou.validate.model.config.ParamConfig;
import org.catdou.validate.model.config.UrlRuleBean;
import org.catdou.validate.request.ServletRequestParamWrapper;
import org.catdou.validate.type.ParamValidator;
import org.catdou.validate.util.HttpUtil;
import org.catdou.validate.util.ReflectUtil;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author James
 */
public abstract class BaseParamProcessor {
    protected ServletRequestParamWrapper httpServletRequest;

    protected HttpServletResponse httpServletResponse;

    protected UrlRuleBean urlRuleBean;

    protected ParamConfig paramConfig;

    /**
     * is need check
     * @param urlRuleBean
     * @return true or false
     */
    abstract boolean isNeedCheck(UrlRuleBean urlRuleBean);

    /**
     * validate
     * @return true or false
     */
    abstract boolean validate();



    public ParamValidator getParamValidator(CheckRule checkRule) {
        String ruleName = checkRule.getName().toUpperCase();
        Map<String, ParamValidator> paramValidatorMap = paramConfig.getValidatorCache().getParamValidatorMap();
        ParamValidator paramValidator;
        // if define
        if (ValidatorType.DEFINE.name().equals(ruleName)) {
            String clazz = checkRule.getValue();
            paramValidator = paramValidatorMap.get(clazz);
            if (paramValidator == null) {
                paramValidator = ReflectUtil.createValidator(clazz);
                paramValidatorMap.put(clazz, paramValidator);
            }
        } else {
            paramValidator = paramValidatorMap.get(ruleName);
            if (paramValidator == null) {
                throw new ConfigException("unknown " + ruleName + " param validator. please add it to ValidatorCache ");
            }
        }
        return paramValidator;
    }


    public boolean checkRule(Param configParam, Object reqParam, List<CheckRule> checkRuleList) {
        String configName = configParam.getName();
        if (reqParam == null) {
            if (configParam.isNullable()) {
                return true;
            } else {
                ValidateResult validateResult = new ValidateResult();
                String msg = "request param " + configName + " is null";
                validateResult.setMsg(msg);
                urlRuleBean.getErrorHandler().handler(httpServletRequest, httpServletResponse, validateResult);
                return false;
            }
        }
        return checkRule(configName, reqParam, checkRuleList);
    }

    public boolean checkRule(String configName, Object reqParam, List<CheckRule> checkRuleList) {
        for (CheckRule checkRule : checkRuleList) {
            ParamValidator paramValidator = getParamValidator(checkRule);
            InputParam inputParam = new InputParam(configName, reqParam);
            checkRule.setParamConfig(paramConfig);
            ValidateResult validateResult = paramValidator.validate(inputParam, checkRule);
            if (!validateResult.isSuccess()) {
                urlRuleBean.getErrorHandler().handler(httpServletRequest, httpServletResponse, validateResult);
                return false;
            }
        }
        return true;
    }

    public ServletRequestParamWrapper getHttpServletRequest() {
        return httpServletRequest;
    }

    public void setHttpServletRequest(ServletRequestParamWrapper httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    public HttpServletResponse getHttpServletResponse() {
        return httpServletResponse;
    }

    public void setHttpServletResponse(HttpServletResponse httpServletResponse) {
        this.httpServletResponse = httpServletResponse;
    }

    public UrlRuleBean getUrlRuleBean() {
        return urlRuleBean;
    }

    public void setUrlRuleBean(UrlRuleBean urlRuleBean) {
        this.urlRuleBean = urlRuleBean;
    }

    public ParamConfig getParamConfig() {
        return paramConfig;
    }

    public void setParamConfig(ParamConfig paramConfig) {
        this.paramConfig = paramConfig;
    }
}
