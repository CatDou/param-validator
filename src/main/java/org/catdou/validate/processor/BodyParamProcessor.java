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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.catdou.validate.enums.ValidatorType;
import org.catdou.validate.model.InputParam;
import org.catdou.validate.model.ValidateResult;
import org.catdou.validate.model.config.CheckRule;
import org.catdou.validate.model.config.Param;
import org.catdou.validate.type.ParamValidator;
import org.catdou.validate.util.HttpUtil;
import org.catdou.validate.util.ReflectUtil;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author James
 */
public class BodyParamProcessor extends BaseParamProcessor {

    public boolean validate() {
        Long urlBodySize = urlRuleBean.getMaxBodySize();
        if (urlBodySize != null && httpServletRequest.getBodySize() > urlRuleBean.getMaxBodySize()) {
            throw new IllegalArgumentException("the request body size is larger than the configuration value");
        }
        List<Param> paramList = urlRuleBean.getBodyParams();
        if (CollectionUtils.isEmpty(paramList)) {
            return true;
        }
        String bodyStr = httpServletRequest.getBodyParamStr();
        Object object = JSONObject.parse(bodyStr);
        if (object instanceof JSONObject) {
            if (!checkJsonData(paramList, (JSONObject) object)) {
                return false;
            }
        } else if (object instanceof JSONArray) {
            JSONArray jsonArray = (JSONArray) object;
            int size = jsonArray.size();
            for (int i = 0; i < size; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (!checkJsonData(paramList, jsonObject)) {
                    return false;
                }

            }
        }
        return true;
    }

    private boolean checkJsonData(List<Param> paramList, JSONObject jsonObject) {
        for (Param param : paramList) {
            String configName = param.getName();
            List<CheckRule> checkRuleList = param.getRules();
            Object dataObj = jsonObject.get(configName);
            // 嵌套类型的json 需要自定以校验器
            if (dataObj instanceof JSONArray || dataObj instanceof JSONObject) {
                List<CheckRule> ruleList = checkRuleList.stream()
                        .filter(checkRule -> ValidatorType.DEFINE.name().equals(checkRule.getName()))
                        .collect(Collectors.toList());
                if (CollectionUtils.isEmpty(ruleList)) {
                    continue;
                }
                if (!checkComplex(ruleList.get(0), configName, dataObj)) {
                    return false;
                }
            }
            if (!checkRule(configName, dataObj.toString(), checkRuleList)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkComplex(CheckRule checkRule, String configName, Object dataObj) {
        Map<String, ParamValidator> paramValidatorMap = paramConfig.getValidatorCache().getParamValidatorMap();
        String ruleName = checkRule.getName().toUpperCase();
        // if define
        if (ValidatorType.DEFINE.name().equals(ruleName)) {
            String clazz = checkRule.getValue();
            ParamValidator paramValidator = paramValidatorMap.get(clazz);
            if (paramValidator == null) {
                paramValidator = ReflectUtil.createValidator(clazz);
                paramValidatorMap.put(clazz, paramValidator);
            }
            InputParam inputParam = new InputParam(configName, JSON.toJSONString(dataObj));
            ValidateResult validateResult = paramValidator.validate(inputParam, checkRule);
            if (validateResult != null && !validateResult.isSuccess()) {
                HttpUtil.printObject(httpServletResponse, validateResult);
                return false;
            }
        }
        return true;
    }
}
