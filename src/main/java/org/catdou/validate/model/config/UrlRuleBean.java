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

import java.util.List;

/**
 * @author James
 */
public class UrlRuleBean implements Comparable<UrlRuleBean> {
    private String url;

    private String method;

    private int sort;

    private List<Param> urlParams;

    private List<Param> pathParams;

    private List<Param> bodyParams;

    private Long maxBodySize;

    private boolean checkAllParam;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method.toUpperCase();
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public List<Param> getUrlParams() {
        return urlParams;
    }

    public void setUrlParams(List<Param> urlParams) {
        this.urlParams = urlParams;
    }

    public List<Param> getPathParams() {
        return pathParams;
    }

    public void setPathParams(List<Param> pathParams) {
        this.pathParams = pathParams;
    }

    public List<Param> getBodyParams() {
        return bodyParams;
    }

    public void setBodyParams(List<Param> bodyParams) {
        this.bodyParams = bodyParams;
    }

    public Long getMaxBodySize() {
        return maxBodySize;
    }

    public void setMaxBodySize(Long maxBodySize) {
        this.maxBodySize = maxBodySize;
    }

    public boolean isCheckAllParam() {
        return checkAllParam;
    }

    public void setCheckAllParam(boolean checkAllParam) {
        this.checkAllParam = checkAllParam;
    }

    @Override
    public int compareTo(UrlRuleBean o) {
        return this.sort - o.getSort();
    }
}
