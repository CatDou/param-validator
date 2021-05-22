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

package org.catdou.validate.factory;


import com.alibaba.fastjson.JSONArray;
import org.catdou.validate.exception.ConfigException;
import org.catdou.validate.io.FileResources;
import org.catdou.validate.model.config.ParamConfig;
import org.catdou.validate.model.config.UrlRuleBean;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author James
 */
public interface ParamConfigLoader {
    String CHECK_RULE_NAME = "validate_rule_";

    default Resource getCommonResource(Resource[] resources, String commonName) {
        List<Resource> resourceList = Arrays.stream(resources)
                .filter(resource -> commonName.equals(resource.getFilename()))
                .collect(Collectors.toList());
        if (resourceList.size() != 1) {
            throw new ConfigException(commonName + " config error");
        }
        return resourceList.get(0);
    }

    default List<UrlRuleBean> parseAllParamResource(Resource[] resources, String commonName) throws IOException {
        List<UrlRuleBean> allUrlRuleBean = new ArrayList<>();
        for (Resource resource : resources) {
            String fileName = resource.getFilename();
            if (!commonName.equals(fileName) && StringUtils.hasText(fileName) && fileName.startsWith(CHECK_RULE_NAME)) {
                List<UrlRuleBean> ruleBeanList = parseOneResource(resource);
                allUrlRuleBean.addAll(ruleBeanList);
            }
        }
        return allUrlRuleBean;
    }

    List<UrlRuleBean> parseOneResource(Resource resource) throws IOException;

    ParamConfig loadParamConfig(String path);
}
