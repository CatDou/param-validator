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

package scd.processor;

import org.catdou.validate.cache.ValidatorCache;
import org.catdou.validate.factory.ParamConfigFactory;
import org.catdou.validate.factory.ParamConfigLoader;
import org.catdou.validate.model.config.ParamConfig;
import org.catdou.validate.processor.BodyParamProcessor;
import org.junit.Before;
import scd.http.MockHttpServletResponse;

/**
 * @author James
 */
public class BodyParamProcessorTest {
    private BodyParamProcessor bodyParamProcessor;

    private String filePath = "data.txt";

    private ParamConfig paramConfig;



    @Before
    public void init() {
        bodyParamProcessor = new BodyParamProcessor();
        MockHttpServletResponse response = new MockHttpServletResponse(filePath);
        bodyParamProcessor.setHttpServletResponse(response);
        ParamConfigFactory paramConfigFactory = new ParamConfigFactory();
        ValidatorCache validatorCache = new ValidatorCache();
        ParamConfigLoader paramConfigLoader = paramConfigFactory.createParamConfigLoader("xml");
        paramConfig = paramConfigLoader.loadParamConfig("classpath*:xml/**/validate_*.xml");
        paramConfig.setValidatorCache(validatorCache);
        paramConfig.init();
    }


}
