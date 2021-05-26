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

package org.catdou.validate.request;

import org.catdou.validate.exception.StreamException;
import org.catdou.validate.log.ValidatorLog;
import org.catdou.validate.log.ValidatorLogFactory;
import org.catdou.validate.model.config.ParamConfig;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * @author James
 */
public class ServletRequestParamWrapper extends HttpServletRequestWrapper {
    private static final ValidatorLog LOG = ValidatorLogFactory.getLogger(ServletRequestParamWrapper.class);

    private ParamConfig paramConfig;

    private byte[] bodyParam;

    private int bodySize;

    public ServletRequestParamWrapper(HttpServletRequest request, ParamConfig paramConfig) {
        super(request);
        this.paramConfig = paramConfig;
        bodyParam = readRequest(request);
    }

    private byte[] readRequest(HttpServletRequest request) {
        long maxBody = paramConfig.getCommonConfig().getMaxBodySize();
        try {
            InputStream inputStream = request.getInputStream();
            int readLen;
            byte[] buffer = new byte[1024];
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            while ((readLen = inputStream.read(buffer)) != -1) {
                bodySize = bodySize + readLen;
                if (bodySize > maxBody) {
                    throw new IllegalArgumentException("the request body size is larger than the configuration value");
                }
                outputStream.write(buffer, 0, readLen);
            }
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new StreamException("read request body error ", e);
        }
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bodyParam);
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
                LOG.info("set read listener ");
            }

            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }
        };
    }

    public byte[] getBodyParam() {
        return bodyParam;
    }

    public String getBodyParamStr() {
        return new String(bodyParam, StandardCharsets.UTF_8);
    }

    public int getBodySize() {
        return bodySize;
    }
}
