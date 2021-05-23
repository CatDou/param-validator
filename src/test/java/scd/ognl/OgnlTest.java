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

package scd.ognl;

import org.catdou.validate.enums.ValidatorType;
import org.catdou.validate.exception.ValidatorOgnlException;
import org.catdou.validate.model.InputParam;
import org.catdou.validate.model.config.CheckRule;
import org.catdou.validate.type.OgnlValidator;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author James
 */
public class OgnlTest {

    private OgnlValidator ognlValidator = new OgnlValidator();

    private CheckRule createCheckRule(String ognlExpression) {
        CheckRule checkRule = new CheckRule();
        checkRule.setName(ValidatorType.OGNL.name());
        checkRule.setValue(ognlExpression);
        return checkRule;
    }

    @Test
    public void testMathExpression() {
        String expression = "a >= 1 and a <= 100";
        CheckRule checkRule = createCheckRule(expression);
        Assert.assertTrue(ognlValidator.validate(new InputParam("a", 50), checkRule).isSuccess());
        Assert.assertFalse(ognlValidator.validate(new InputParam("a", 120), checkRule).isSuccess());
    }

    @Test
    public void testArgMethod() {
        String expression = "@scd.ognl.enums.EnumTest@contains(name)";
        CheckRule checkRule = createCheckRule(expression);
        Assert.assertTrue(ognlValidator.validate(new InputParam("name", "XML"), checkRule).isSuccess());
        Assert.assertTrue(ognlValidator.validate(new InputParam("name", "JSON"), checkRule).isSuccess());
        Assert.assertFalse(ognlValidator.validate(new InputParam("name", "ZIP"), checkRule).isSuccess());
    }

    @Test
    public void testNonStaticMethod() {
        String expression = "@scd.ognl.enums.EnumTest@value(name)";
        CheckRule checkRule = createCheckRule(expression);
        ValidatorOgnlException validatorOgnlException = Assert.assertThrows(ValidatorOgnlException.class,
                () -> ognlValidator.validate(new InputParam("name", "XML"), checkRule).isSuccess());
        Assert.assertEquals("ognl get value error", validatorOgnlException.getMessage());
    }
}
