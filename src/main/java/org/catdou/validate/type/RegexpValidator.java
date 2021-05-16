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

package org.catdou.validate.type;

import org.catdou.validate.model.InputParam;
import org.catdou.validate.model.ValidateResult;
import org.catdou.validate.model.config.CheckRule;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * @author James
 */
public class RegexpValidator implements ParamValidator {
    private Map<String, Pattern> patternMap = new ConcurrentHashMap<>(16);

    @Override
    public ValidateResult validate(InputParam inputParam, CheckRule checkRule) {
        String regexpVal = checkRule.getValue();
        Pattern pattern = patternMap.get(regexpVal.trim());
        if (pattern == null) {
            pattern = Pattern.compile(checkRule.getValue());
            patternMap.put(checkRule.getValue(), pattern);
        }
        ValidateResult validateResult = new ValidateResult();
        if (pattern.matcher(inputParam.getParam().toString()).matches()) {
            validateResult.setSuccess(true);
        } else {
            validateResult.setSuccess(false);
            validateResult.setMsg(inputParam.getName() + " : " + inputParam.getParam() + " check error");
        }
        return validateResult;
    }
}
