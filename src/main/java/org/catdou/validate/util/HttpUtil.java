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

package org.catdou.validate.util;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.catdou.validate.log.ValidatorLog;
import org.catdou.validate.log.ValidatorLogFactory;
import org.catdou.validate.model.url.UrlPath;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static org.catdou.validate.constant.ParamValidatorConstants.PATH_LEFT;
import static org.catdou.validate.constant.ParamValidatorConstants.PATH_RIGHT;
import static org.catdou.validate.constant.ParamValidatorConstants.URL_SLASH;

/**
 * @author James
 */
public class HttpUtil {
    private static final ValidatorLog LOGGER = ValidatorLogFactory.getLogger(HttpUtil.class);

    public static void printInfo(HttpServletResponse httpResponse, String str) {
        httpResponse.setCharacterEncoding("UTF-8");
        httpResponse.setContentType("application/json;charset=utf-8");
        try (PrintWriter printWriter = httpResponse.getWriter()) {
            printWriter.println(str);
        } catch (IOException e) {
            LOGGER.error("print json info error " +  ExceptionUtils.getStackTrace(e));
        }
    }

    public static void printObject(HttpServletResponse response, Object object) {
        printInfo(response, JSON.toJSONString(object));
    }

    public static UrlPath getUrlPath(String url) {
        // if not start with url slash
        if (url.charAt(0) != URL_SLASH) {
            url = URL_SLASH + url;
        }
        char[] urlArr = url.toCharArray();
        int lenu = urlArr.length;
        int moveIndex = 0;
        UrlPath urlPath = new UrlPath();
        while (moveIndex < lenu && moveIndex != -1) {
            int next = -1;
            if (urlArr[moveIndex] == URL_SLASH) {
                next = findNext(moveIndex + 1, urlArr, lenu);
            }
            if (moveIndex + 1 == next) {
                LOGGER.debug("find two slash //");
            } else {
                String pathItem;
                if (next != -1) {
                    pathItem = url.substring(moveIndex + 1, next);
                } else {
                    pathItem = url.substring(moveIndex + 1);
                }
                urlPath.getPaths().add(pathItem);
                if (pathItem.startsWith(PATH_LEFT) && pathItem.endsWith(PATH_RIGHT)) {
                    urlPath.getIndexs().add(urlPath.getPaths().size() - 1);
                }
            }
            // the end slash
            if (next == lenu - 1) {
                break;
            }
            moveIndex = next;
        }
        return urlPath;
    }

    private static int findNext(int startIndex, char[] urlArr, int lenu) {
        for (int i = startIndex; i < lenu; i++) {
            if (urlArr[i] == URL_SLASH) {
                return i;
            }
        }
        return -1;
    }
}
