/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package io.netty.handler.codec.rtsp;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.TooLongFrameException;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

/**
 * Decodes {@link ByteBuf}s into RTSP responses represented in
 * {@link HttpResponse}s.
 * <p>
 * <h3>Parameters that prevents excessive memory consumption</h3>
 * <table border="1">
 * <tr>
 * <th>Name</th><th>Meaning</th>
 * </tr>
 * <tr>
 * <td>{@code maxInitialLineLength}</td>
 * <td>The maximum length of the initial line (e.g. {@code "RTSP/1.0 200 OK"})
 *     If the length of the initial line exceeds this value, a
 *     {@link TooLongFrameException} will be raised.</td>
 * </tr>
 * <tr>
 * <td>{@code maxHeaderSize}</td>
 * <td>The maximum length of all headers.  If the sum of the length of each
 *     header exceeds this value, a {@link TooLongFrameException} will be raised.</td>
 * </tr>
 * <tr>
 * <td>{@code maxContentLength}</td>
 * <td>The maximum length of the content.  If the content length exceeds this
 *     value, a {@link TooLongFrameException} will be raised.</td>
 * </tr>
 * </table>
 */
public class RtspResponseDecoder extends RtspObjectDecoder {

    private static final HttpResponseStatus UNKNOWN_STATUS = new HttpResponseStatus(999, "Unknown");

    /**
     * Creates a new instance with the default
     * {@code maxInitialLineLength (4096}}, {@code maxHeaderSize (8192)}, and
     * {@code maxContentLength (8192)}.
     */
    public RtspResponseDecoder() {
    }

    /**
     * Creates a new instance with the specified parameters.
     */
    public RtspResponseDecoder(int maxInitialLineLength, int maxHeaderSize,
            int maxContentLength) {
        super(maxInitialLineLength, maxHeaderSize, maxContentLength);
    }

    @Override
    protected HttpMessage createMessage(String[] initialLine) throws Exception {
        return new DefaultHttpResponse(
                RtspVersions.valueOf(initialLine[0]),
                new HttpResponseStatus(Integer.valueOf(initialLine[1]), initialLine[2]));
    }

    @Override
    protected HttpMessage createInvalidMessage() {
        return new DefaultHttpResponse(RtspVersions.RTSP_1_0, UNKNOWN_STATUS);
    }

    @Override
    protected boolean isDecodingRequest() {
        return false;
    }
}
