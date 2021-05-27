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

package scd.util;

import org.catdou.validate.model.url.UrlPath;
import org.catdou.validate.util.HttpUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author James
 */
public class HttpUtilTest {

    @Test
    public void testPathUrl() {
        String configUrl = "/url/{item}/{key}";
        String url = "/url/name/1";
        UrlPath configPath = HttpUtil.getUrlPath(configUrl);
        UrlPath urlPath = HttpUtil.getUrlPath(url);
        Assert.assertEquals(3, configPath.getPaths().size());
        Assert.assertEquals(2, configPath.getIndexs().size());
        Assert.assertEquals(3, urlPath.getPaths().size());
        Assert.assertEquals(0, urlPath.getIndexs().size());
        Assert.assertEquals("url", configPath.getPaths().get(0));
        Assert.assertEquals("{item}", configPath.getPaths().get(1));
        Assert.assertEquals("{key}", configPath.getPaths().get(2));
        Assert.assertEquals("url", urlPath.getPaths().get(0));
        Assert.assertEquals("name", urlPath.getPaths().get(1));
        Assert.assertEquals("1", urlPath.getPaths().get(2));
    }

    @Test
    public void testPathUrlTowSlash() {
        String configUrl = "//url/{item}//{key}";
        UrlPath configPath = HttpUtil.getUrlPath(configUrl);
        Assert.assertEquals(3,configPath.getPaths().size() );
        Assert.assertEquals(2,configPath.getIndexs().size());
        Assert.assertEquals("url", configPath.getPaths().get(0));
        Assert.assertEquals("{item}", configPath.getPaths().get(1));
        Assert.assertEquals("{key}", configPath.getPaths().get(2));
    }

    @Test
    public void testPathUrlEndWithSlash() {
        String configUrl = "//url/{item}//{key}/";
        UrlPath configPath = HttpUtil.getUrlPath(configUrl);
        Assert.assertEquals(3, configPath.getPaths().size());
        Assert.assertEquals(2, configPath.getIndexs().size());
        Assert.assertEquals("url", configPath.getPaths().get(0));
        Assert.assertEquals("{item}", configPath.getPaths().get(1));
        Assert.assertEquals("{key}", configPath.getPaths().get(2));
    }

    @Test
    public void testOneSlash() {
        String configUrl = "/url";
        UrlPath configPath = HttpUtil.getUrlPath(configUrl);
        Assert.assertEquals(1, configPath.getPaths().size());
    }

    @Test
    public void testNoSlash() {
        String configUrl = "url";
        UrlPath configPath = HttpUtil.getUrlPath(configUrl);
        Assert.assertEquals(1, configPath.getPaths().size());
    }

}
