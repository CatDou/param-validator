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

package scd.cache;

import org.catdou.validate.cache.ValidatorCache;
import org.junit.Assert;
import org.junit.Test;
import scd.util.define.DefineParamValidator;

/**
 * @author James
 */
public class ValidatorCacheTest {
    private ValidatorCache validatorCache = new ValidatorCache();

    @Test
    public void testAdd() {
        validatorCache.add(DefineParamValidator.class.getName(), new DefineParamValidator());
        Assert.assertTrue(validatorCache.getParamValidatorMap().size() > 0);
        Object defineParamValidator = validatorCache.getParamValidatorMap().get(DefineParamValidator.class.getName());
        Assert.assertTrue(defineParamValidator instanceof DefineParamValidator);
    }
}
