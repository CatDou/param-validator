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

import ognl.Ognl;
import ognl.OgnlException;
import org.catdou.validate.constant.ParamValidatorConstant;
import org.catdou.validate.exception.ConfigException;
import org.catdou.validate.exception.ValidatorOgnlException;
import org.catdou.validate.log.ValidatorLog;
import org.catdou.validate.log.ValidatorLogFactory;
import org.catdou.validate.model.InputParam;
import org.catdou.validate.model.ValidateResult;
import org.catdou.validate.model.config.CheckRule;
import org.catdou.validate.ognl.ValidatorMemberAccess;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author James
 */
public class OgnlValidator implements ParamValidator {
    private static final ValidatorMemberAccess VALIDATOR_MEMBER_ACCESS = new ValidatorMemberAccess();

    private static Map<String, Object> OGNL_NODE_CACHE = new ConcurrentHashMap<>(256);

    @Override
    public ValidateResult validate(InputParam inputParam, CheckRule checkRule) {
        String expression = checkRule.getValue();
        Object node = getOgnlNode(expression);
        Map<String, Object> map = new HashMap<>();
        map.put(inputParam.getName(), inputParam.getParam());
        Map context = Ognl.createDefaultContext(map, VALIDATOR_MEMBER_ACCESS);
        try {
            Object value = Ognl.getValue(node, context, map);
            if (!(value instanceof Boolean)) {
                throw new ConfigException("config expression except return boolean, but not found bool");
            }
            ValidateResult validateResult = new ValidateResult();
            Boolean checked = (Boolean) value;
            validateResult.setSuccess(checked);
            if (!checked) {
                String msg = String.format(ParamValidatorConstant.ERROR_MSG, checkRule.getName(),
                        inputParam.getName(), inputParam.getParam());
                validateResult.setMsg(msg);
            }
            return validateResult;
        } catch (OgnlException e) {
            throw new ValidatorOgnlException("ognl get value error", e);
        }
    }

    private Object getOgnlNode(String expression) {
        Object node = OGNL_NODE_CACHE.get(expression);
        if (node == null) {
            try {
                node = Ognl.parseExpression(expression);
                OGNL_NODE_CACHE.put(expression, node);
            } catch (OgnlException e) {
                throw new ConfigException("ognl expression config error " + expression, e);
            }
        }
        return node;
    }
}
