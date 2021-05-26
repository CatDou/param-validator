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

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.catdou.validate.constant.ParamValidatorConstants;
import org.catdou.validate.exception.ConfigException;
import org.catdou.validate.exception.ParseException;
import org.catdou.validate.io.FileResourcesUtils;
import org.catdou.validate.log.ValidatorLog;
import org.catdou.validate.log.ValidatorLogFactory;
import org.catdou.validate.model.config.CommonConfig;
import org.catdou.validate.model.config.ParamConfig;
import org.catdou.validate.model.config.UrlRuleBean;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author James
 */
public class JsonConfigLoader implements ParamConfigLoader {
    private static final ValidatorLog LOGGER = ValidatorLogFactory.getLogger(JsonConfigLoader.class);

    @Override
    public List<UrlRuleBean> parseOneResource(Resource resource) throws IOException {
        String paramUrlJson = readStr(resource.getFile());
        return JSONObject.parseArray(paramUrlJson, UrlRuleBean.class);
    }

    @Override
    public ParamConfig loadParamConfig(String path) {
        try {
            return loadByPathMatchingResource(path);
        } catch (IOException e) {
            throw new ParseException("load param json config error", e);
        }
    }

    public ParamConfig loadByPathMatchingResource(String path) throws IOException {
        Resource[] resources = FileResourcesUtils.loadResourceByPath(path);
        Resource commonResource = getCommonResource(resources, ParamValidatorConstants.JSON_COMMON_NAME);
        String commonJson = readStr(commonResource.getFile());
        CommonConfig commonConfig = JSONObject.parseObject(commonJson, CommonConfig.class);
        LOGGER.info("load common config json success");
        List<UrlRuleBean> allRuleBeanList = parseAllParamResource(resources, ParamValidatorConstants.JSON_COMMON_NAME);
        LOGGER.info("load rule config json success");
        ParamConfig paramConfig = new ParamConfig();
        paramConfig.setCommonConfig(commonConfig);
        paramConfig.setUrlRuleBeanList(allRuleBeanList);
        return paramConfig;
    }

    public String readStr(File commonFile) {
        try {
            return FileUtils.readFileToString(commonFile, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new ConfigException("read json str error ", e);
        }
    }
}
