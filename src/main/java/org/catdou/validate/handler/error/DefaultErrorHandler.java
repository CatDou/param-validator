/*
 * Copyright 2022 catdou
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

package org.catdou.validate.handler.error;

import org.catdou.validate.model.ValidateResult;
import org.catdou.validate.util.HttpUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author James
 */
public class DefaultErrorHandler implements ErrorHandler {
    @Override
    public void handler(HttpServletRequest request, HttpServletResponse response, ValidateResult validateResult) {
        HttpUtil.printObject(response, validateResult);
    }
}
