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

package org.catdou.validate.model.config;

import com.alibaba.fastjson.annotation.JSONField;
import org.catdou.validate.handler.error.ErrorHandler;
import org.catdou.validate.json.ErrorHandlerDeserialize;

import java.util.List;

/**
 * @author James
 */
public class CommonConfig {
    private boolean checkAllUrl = false;

    private List<String> whiteList;

    private Long maxBodySize = 10000L;

    @JSONField(deserializeUsing = ErrorHandlerDeserialize.class)
    private ErrorHandler globalErrorHandler;

    public boolean isCheckAllUrl() {
        return checkAllUrl;
    }

    public void setCheckAllUrl(boolean checkAllUrl) {
        this.checkAllUrl = checkAllUrl;
    }

    public List<String> getWhiteList() {
        return whiteList;
    }

    public void setWhiteList(List<String> whiteList) {
        this.whiteList = whiteList;
    }

    public Long getMaxBodySize() {
        return maxBodySize;
    }

    public void setMaxBodySize(Long maxBodySize) {
        this.maxBodySize = maxBodySize;
    }

    public ErrorHandler getGlobalErrorHandler() {
        return globalErrorHandler;
    }

    public void setGlobalErrorHandler(ErrorHandler globalErrorHandler) {
        this.globalErrorHandler = globalErrorHandler;
    }
}
