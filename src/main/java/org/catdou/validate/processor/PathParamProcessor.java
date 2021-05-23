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

import org.catdou.validate.exception.ConfigException;
import org.catdou.validate.model.config.CheckRule;
import org.catdou.validate.model.config.Param;
import org.catdou.validate.model.url.UrlPath;
import org.catdou.validate.util.HttpUtil;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author James
 */
public class PathParamProcessor extends BaseParamProcessor {

    public boolean validate() {
        List<Param> paramList = urlRuleBean.getPathParams();
        if (CollectionUtils.isEmpty(paramList)) {
            return true;
        }
        String configUrl = urlRuleBean.getUrl();
        String url = httpServletRequest.getServletPath();
        UrlPath configUrlPath = HttpUtil.getUrlPath(configUrl);
        UrlPath inputPath = HttpUtil.getUrlPath(url);
        if (configUrlPath.getPaths().size() != inputPath.getPaths().size()) {
            throw new ConfigException("config path error, input url " + url);
        }
        Map<String, String> paramMap = createParamMap(configUrlPath, inputPath);
        for (Param param : paramList) {
            String configName = param.getName();
            List<CheckRule> checkRuleList = param.getRules();
            String reqParam = paramMap.get(configName);
            if (!checkRule(param, reqParam, checkRuleList)) {
                return false;
            }
        }
        return true;
    }

    private Map<String, String> createParamMap(UrlPath configUrlPath, UrlPath inputPath) {
        List<String> configItems = configUrlPath.getPaths();
        List<String> inputItems = inputPath.getPaths();
        List<Integer> indexList = configUrlPath.getIndexs();
        Map<String, String> paramMap = new HashMap<>(16);
        for (Integer index : indexList) {
            String itemKey = configItems.get(index);
            paramMap.put(itemKey, inputItems.get(index));
        }
        return paramMap;
    }
}
