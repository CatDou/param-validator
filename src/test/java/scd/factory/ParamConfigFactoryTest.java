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

package scd.factory;

import org.catdou.validate.enums.LoaderType;
import org.catdou.validate.exception.ConfigException;
import org.catdou.validate.factory.JsonConfigLoader;
import org.catdou.validate.factory.ParamConfigFactory;
import org.catdou.validate.factory.ParamConfigLoader;
import org.catdou.validate.factory.XmlConfigLoader;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author James
 */
public class ParamConfigFactoryTest {
    private ParamConfigFactory paramConfigFactory = new ParamConfigFactory();

    @Test
    public void testCreateLoader() {
        ParamConfigLoader xmlLoader = paramConfigFactory.createParamConfigLoader(LoaderType.XML.name());
        Assert.assertTrue(xmlLoader instanceof XmlConfigLoader);
        ParamConfigLoader jsonLoader = paramConfigFactory.createParamConfigLoader(LoaderType.JSON.name());
        Assert.assertTrue(jsonLoader instanceof JsonConfigLoader);
        ConfigException configException = Assert.assertThrows(ConfigException.class,
                () -> paramConfigFactory.createParamConfigLoader("zip"));
        Assert.assertEquals("unknown param file load type " + "zip", configException.getMessage());
    }
}
