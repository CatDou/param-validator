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

package org.catdou.validate.cache;

import org.catdou.validate.enums.ValidatorType;
import org.catdou.validate.type.OgnlValidator;
import org.catdou.validate.type.ParamValidator;
import org.catdou.validate.type.RegexpValidator;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author James
 */
public class ValidatorCache {
    private Map<String, ParamValidator> paramValidatorMap = new ConcurrentHashMap<>();

    public ValidatorCache() {
        add(ValidatorType.REGEXP.name(), new RegexpValidator());
        add(ValidatorType.OGNL.name(), new OgnlValidator());
    }

    public void add(String type, ParamValidator paramValidator) {
        paramValidatorMap.put(type, paramValidator);
    }

    public Map<String, ParamValidator> getParamValidatorMap() {
        return paramValidatorMap;
    }

    public void setParamValidatorMap(Map<String, ParamValidator> paramValidatorMap) {
        this.paramValidatorMap = paramValidatorMap;
    }
}
