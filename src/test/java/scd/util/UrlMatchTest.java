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

import org.catdou.validate.util.UrlMatchHelper;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.util.AntPathMatcher;


/**
 * @author James
 */
public class UrlMatchTest {

    @Test
    public void testMatch() {
        Assert.assertTrue(UrlMatchHelper.isMatch("/param/body/{key}", "/param/body/1"));
        Assert.assertTrue(UrlMatchHelper.isMatch("/param/body/{data}", "/param/body/1"));
        Assert.assertFalse(UrlMatchHelper.isMatch("/param/body/{data}/1", "/param/body/1"));
        Assert.assertTrue(UrlMatchHelper.isMatch("/**/*.html", "/aaaa/bbb/cccc.html"));
    }
}
