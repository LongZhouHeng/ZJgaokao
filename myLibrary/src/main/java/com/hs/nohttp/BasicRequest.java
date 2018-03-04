/*
 * Copyright 2015 Yan Zhenjie
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hs.nohttp;

import android.text.TextUtils;

import com.hs.nohttp.tools.CounterOutputStream;
import com.hs.nohttp.tools.IOUtils;
import com.hs.nohttp.tools.LinkedMultiValueMap;
import com.hs.nohttp.tools.MultiValueMap;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Proxy;
import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;

/**
 * <p>
 * Implement all the methods of the base class {@link ImplServerRequest} and {@link ImplClientRequest}.
 * </p>
 * Created in Nov 4, 2015 8:28:50 AM.
 *
 * @author Yan Zhenjie.
 */
;import static com.hs.camera.CameraContainer.TAG;

public abstract class BasicRequest implements BasicClientRequest, BasicServerRequest {

    private final String boundary = createBoundary();
    private final String startBoundary = "--" + boundary;
    private final String endBoundary = startBoundary + "--";

    /**
     * Request priority.
     */
    private Priority mPriority = Priority.DEFAULT;
    /**
     * The sequence.
     */
    private int sequence;
    /**
     * Target address.
     */
    private String url;
    /**
     * The real .
     */
    private String buildUrl;
    /**
     * Request method.
     */
    private RequestMethod mRequestMethod;
    /**
     * Proxy server.
     */
    private Proxy mProxy;
    /**
     * SSLSockets.
     */
    private SSLSocketFactory mSSLSocketFactory = null;
    /**
     * HostnameVerifier.
     */
    private HostnameVerifier mHostnameVerifier = null;
    /**
     * Connect timeout of request.
     */
    private int mConnectTimeout = NoHttp.TIMEOUT_8S;
    /**
     * Read data timeout.
     */
    private int mReadTimeout = NoHttp.TIMEOUT_8S;
    /**
     * ContentType
     */
    private String mContentType;
    /**
     * Request heads.
     */
    private Headers mHeaders;
    /**
     * Param collection.
     */
    private MultiValueMap<String, Object> mParamKeyValues;
    /**
     * RequestBody.
     */
    private InputStream mRequestBody;
    /**
     * Redirect handler.
     */
    private RedirectHandler mRedirectHandler;
    /**
     * Request queue
     */
    private BlockingQueue<?> blockingQueue;
    /**
     * The record has started.
     */
    private boolean isStart = false;
    /**
     * The request is completed.
     */
    private boolean isFinished = false;
    /**
     * Has been canceled.
     */
    private boolean isCanceled = false;
    /**
     * Cancel sign.
     */
    private Object cancelSign;
    /**
     * Tag of request.
     */
    private Object mTag;

    /**
     * Create a request, RequestMethod is {@link RequestMethod#GET}.
     *
     * @param url request address, like: http://www.google.com.
     */
    public BasicRequest(String url) {
        this(url, RequestMethod.GET);
    }

    /**
     * Create a request.
     *
     * @param url           request adress, like: http://www.google.com.
     * @param requestMethod request method, like {@link RequestMethod#GET}, {@link RequestMethod#POST}.
     */
    public BasicRequest(String url, RequestMethod requestMethod) {
        this.url = url;
        mRequestMethod = requestMethod;
        mHeaders = new HttpHeaders();
        mParamKeyValues = new LinkedMultiValueMap<String, Object>();
    }

    @Override
    public void setPriority(Priority priority) {
        this.mPriority = priority;
    }

    @Override
    public Priority getPriority() {
        return mPriority;
    }

    @Override
    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    @Override
    public int getSequence() {
        return this.sequence;
    }

    @Override
    public final int compareTo(BasicServerRequest another) {
        final Priority me = getPriority();
        final Priority it = another.getPriority();
        return me == it ? getSequence() - another.getSequence() : it.ordinal() - me.ordinal();
    }

    @Override
    public String url() {
        if (TextUtils.isEmpty(buildUrl)) {
            StringBuilder urlBuilder = new StringBuilder(url);
            if (!getRequestMethod().allowRequestBody() && mParamKeyValues.size() > 0) {
                StringBuffer paramBuffer = buildCommonParams(getParamKeyValues(), getParamsEncoding());
                if (url.contains("?") && url.contains("=") && paramBuffer.length() > 0)
                    urlBuilder.append("&");
                else if (paramBuffer.length() > 0)
                    urlBuilder.append("?");
                urlBuilder.append(paramBuffer);
            }
            buildUrl = urlBuilder.toString();
        }
        return buildUrl;
    }

    @Override
    public RequestMethod getRequestMethod() {
        return mRequestMethod;
    }

    @Override
    public void setProxy(Proxy proxy) {
        this.mProxy = proxy;
    }

    @Override
    public Proxy getProxy() {
        return mProxy;
    }

    @Override
    public void setSSLSocketFactory(SSLSocketFactory socketFactory) {
        mSSLSocketFactory = socketFactory;
    }

    @Override
    public SSLSocketFactory getSSLSocketFactory() {
        return mSSLSocketFactory;
    }

    @Override
    public void setHostnameVerifier(HostnameVerifier hostnameVerifier) {
        mHostnameVerifier = hostnameVerifier;
    }

    @Override
    public HostnameVerifier getHostnameVerifier() {
        return mHostnameVerifier;
    }

    @Override
    public void setConnectTimeout(int connectTimeout) {
        mConnectTimeout = connectTimeout;
    }

    @Override
    public int getConnectTimeout() {
        return mConnectTimeout;
    }

    @Override
    public void setReadTimeout(int readTimeout) {
        mReadTimeout = readTimeout;
    }

    @Override
    public int getReadTimeout() {
        return mReadTimeout;
    }

    @Override
    public void setContentType(String contentType) {
        this.mContentType = contentType;
    }

    @Override
    public void addHeader(String key, String value) {
        mHeaders.add(key, value);
    }

    @Override
    public void setHeader(String key, String value) {
        mHeaders.set(key, value);
    }

    @Override
    public void removeHeader(String key) {
        mHeaders.remove(key);
    }

    @Override
    public void removeAllHeader() {
        mHeaders.clear();
    }

    @Override
    public Headers headers() {
        return mHeaders;
    }

    @Override
    public String getAcceptLanguage() {
        return defaultAcceptLanguage();
    }

    @Override
    public long getContentLength() {
        CounterOutputStream outputStream = new CounterOutputStream();
        try {
            onWriteRequestBody(outputStream);
        } catch (IOException e) {
            Logger.e(e);
        }
        return outputStream.get();
    }

    @Override
    public String getContentType() {
        StringBuilder contentTypeBuild = new StringBuilder();
        if (getRequestMethod().allowRequestBody() && hasBinary())
            contentTypeBuild.append(NoHttp.MULTIPART_FORM_DATA).append("; boundary=").append(boundary);
        else if (TextUtils.isEmpty(mContentType))
            contentTypeBuild.append(NoHttp.APPLICATION_X_WWW_FORM_URLENCODED).append("; charset=").append(getParamsEncoding());
        else
            contentTypeBuild.append(mContentType);
        return contentTypeBuild.toString();
    }

    @Override
    public String getUserAgent() {
        return UserAgent.instance();
    }

    @Override
    public void add(String key, String value) {
        if (value != null) {
            if (getRequestMethod().allowRequestBody())
                mParamKeyValues.add(key, value);
            else
                mParamKeyValues.set(key, value);
        }
    }

    @Override
    public void set(String key, String value) {
        if (value != null)
            mParamKeyValues.set(key, value);
    }

    @Override
    public void add(String key, int value) {
        add(key, Integer.toString(value));
    }

    @Override
    public void add(String key, long value) {
        add(key, Long.toString(value));
    }

    @Override
    public void add(String key, boolean value) {
        add(key, String.valueOf(value));
    }

    @Override
    public void add(String key, char value) {
        add(key, String.valueOf(value));
    }

    @Override
    public void add(String key, double value) {
        add(key, Double.toString(value));
    }

    @Override
    public void add(String key, float value) {
        add(key, Float.toString(value));
    }

    @Override
    public void add(String key, short value) {
        add(key, Integer.toString(value));
    }

    @Override
    public void add(String key, byte value) {
        add(key, Integer.toString(value));
    }

    @Override
    public void add(String key, Binary binary) {
        mParamKeyValues.add(key, binary);
    }

    @Override
    public void add(String key, List<Binary> binaries) {
        if (binaries != null)
            for (Binary binary : binaries)
                mParamKeyValues.add(key, binary);
        else
            Logger.e("The binaries is null.", "Get message extra JSON error!");
    }

    @Override
    public void set(String key, List<Binary> binaries) {
        mParamKeyValues.remove(key);
        if (binaries != null)
            add(key, binaries);
        else
            Logger.e("The binaries is null.", "Get message extra JSON error!");
    }

    @Override
    public void add(Map<String, String> params) {
        if (params != null) {
            for (Map.Entry<String, String> stringEntry : params.entrySet())
                add(stringEntry.getKey(), stringEntry.getValue());
        }
    }

    @Override
    public void set(Map<String, String> params) {
        if (params != null) {
            mParamKeyValues.clear();
            for (Map.Entry<String, String> stringEntry : params.entrySet())
                set(stringEntry.getKey(), stringEntry.getValue());
        }
    }

    @Override
    public List<Object> remove(String key) {
        return mParamKeyValues.remove(key);
    }

    @Override
    public void removeAll() {
        mParamKeyValues.clear();
    }

    @Override
    public MultiValueMap<String, Object> getParamKeyValues() {
        return mParamKeyValues;
    }

    @Override
    public void setDefineRequestBody(InputStream requestBody, String contentType) {
        if (requestBody == null || contentType == null)
            throw new IllegalArgumentException("The requestBody and contentType must be can't be null");
        if (requestBody instanceof ByteArrayInputStream || requestBody instanceof FileInputStream) {
            this.mRequestBody = requestBody;
            this.mContentType = contentType;
        } else {
            throw new IllegalArgumentException("Can only accept ByteArrayInputStream and FileInputStream type of stream");
        }
    }

    @Override
    public void setDefineRequestBody(String requestBody, String contentType) {
        if (!TextUtils.isEmpty(requestBody)) {
            try {
                mRequestBody = IOUtils.toInputStream(requestBody, getParamsEncoding());
                if (!TextUtils.isEmpty(contentType))
                    mContentType = contentType + "; charset=" + getParamsEncoding();
            } catch (UnsupportedEncodingException e) {
                setDefineRequestBody(IOUtils.toInputStream(requestBody), contentType);
            }
        }
    }

    @Override
    public void setDefineRequestBodyForJson(String jsonBody) {
        if (!TextUtils.isEmpty(jsonBody))
            setDefineRequestBody(jsonBody, NoHttp.APPLICATION_JSON);
    }

    @Override
    public void setDefineRequestBodyForJson(JSONObject jsonBody) {
        if (jsonBody != null)
            setDefineRequestBody(jsonBody.toString(), NoHttp.APPLICATION_JSON);
    }

    @Override
    public void setDefineRequestBodyForXML(String xmlBody) {
        if (!TextUtils.isEmpty(xmlBody))
            setDefineRequestBody(xmlBody, NoHttp.APPLICATION_XML);
    }

    /**
     * @param body string of request body.
     * @deprecated use {@link #setDefineRequestBody(String, String)} instead.
     */
    @Deprecated
    @Override
    public void setRequestBody(String body) {
        if (body != null)
            try {
                setRequestBody(body.getBytes(getParamsEncoding()));
            } catch (UnsupportedEncodingException e) {
                Logger.e("From getParamsEncoding() returns the charset not supported by the system, the requestBody is invalid, please check the method returns getParamsEncoding() value", "Get message extra JSON error!");
            }
    }

    /**
     * @param body byte array of request body.
     * @deprecated use {@link #setDefineRequestBody(String, String)} instead.
     */
    @Deprecated
    @Override
    public void setRequestBody(byte[] body) {
        if (body != null)
            this.mRequestBody = new ByteArrayInputStream(body);
    }

    /**
     * Is there a custom request inclusions.
     *
     * @return Returns true representatives have, return false on behalf of the no.
     */
    protected boolean hasDefineRequestBody() {
        return mRequestBody != null;
    }

    /**
     * To get custom inclusions.
     *
     * @return {@link InputStream}.
     */
    protected InputStream getDefineRequestBody() {
        return mRequestBody;
    }

    @Override
    public void onPreExecute() {
    }

    @Override
    public void onWriteRequestBody(OutputStream writer) throws IOException {
        if (!hasDefineRequestBody() && hasBinary())
            writeFormStreamData(writer);
        else if (!hasDefineRequestBody())
            writeCommonStreamData(writer);
        else
            writeRequestBody(writer);
    }

    /**
     * Send form data.
     *
     * @param writer {@link OutputStream}.
     * @throws IOException write error.
     */
    protected void writeFormStreamData(OutputStream writer) throws IOException {
        Set<String> keys = mParamKeyValues.keySet();
        for (String key : keys) {
            List<Object> values = mParamKeyValues.getValues(key);
            for (Object value : values) {
                if (value != null && value instanceof String) {
                    if (!(writer instanceof CounterOutputStream))
                        Logger.i(TAG, key + "=" + value);
                    writeFormString(writer, key, value.toString());
                } else if (value != null && value instanceof Binary) {
                    if (!(writer instanceof CounterOutputStream))
                        Logger.i(TAG, key + " is Binary");
                    writeFormBinary(writer, key, (Binary) value);
                }
            }
        }
        writer.write(("\r\n" + endBoundary).getBytes());
    }

    /**
     * Send text data in a form.
     *
     * @param writer {@link OutputStream}
     * @param key    equivalent to form the name of the input label, {@code "Content-Disposition: form-data; name=key"}.
     * @param value  equivalent to form the value of the input label.
     * @throws IOException Write the data may be abnormal.
     */
    private void writeFormString(OutputStream writer, String key, String value) throws IOException {
        StringBuilder stringFieldBuilder = new StringBuilder(startBoundary).append("\r\n");

        stringFieldBuilder.append("Content-Disposition: form-data; name=\"").append(key).append("\"\r\n");
        stringFieldBuilder.append("Content-Type: text/plain; charset=").append(getParamsEncoding()).append("\r\n\r\n");

        writer.write(stringFieldBuilder.toString().getBytes(getParamsEncoding()));

        writer.write(value.getBytes(getParamsEncoding()));
        writer.write("\r\n".getBytes());
    }

    /**
     * Send binary data in a form.
     */
    private void writeFormBinary(OutputStream writer, String key, Binary value) throws IOException {
        long contentLength = value.getLength();
        if (contentLength > 0) {// Have content to send
            StringBuilder binaryFieldBuilder = new StringBuilder(startBoundary).append("\r\n");
            binaryFieldBuilder.append("Content-Disposition: form-data; name=\"").append(key).append("\"");
            String filename = value.getFileName();
            if (!TextUtils.isEmpty(filename))
                binaryFieldBuilder.append("; filename=\"").append(filename).append("\"");
            binaryFieldBuilder.append("\r\n");

            binaryFieldBuilder.append("Content-Type: ").append(value.getMimeType()).append("\r\n");
            binaryFieldBuilder.append("Content-Transfer-Encoding: binary\r\n\r\n");

            writer.write(binaryFieldBuilder.toString().getBytes());

            if (writer instanceof CounterOutputStream) {
                ((CounterOutputStream) writer).write(contentLength);
            } else {
                value.onWriteBinary(writer);
            }
            writer.write("\r\n".getBytes());
        }
    }

    /**
     * Send non form data.
     *
     * @param writer {@link OutputStream}.
     * @throws IOException write error.
     */
    protected void writeCommonStreamData(OutputStream writer) throws IOException {
        String requestBody = buildCommonParams(getParamKeyValues(), getParamsEncoding()).toString();
        if (!(writer instanceof CounterOutputStream))
            Logger.i(TAG, "Push RequestBody: " + requestBody);
        writer.write(requestBody.getBytes());
    }

    /**
     * Send request requestBody.
     *
     * @param writer {@link OutputStream}.
     * @throws IOException write error.
     */
    protected void writeRequestBody(OutputStream writer) throws IOException {
        if (hasDefineRequestBody()) {
            if (writer instanceof CounterOutputStream) {
                writer.write(mRequestBody.available());
            } else {
                IOUtils.write(mRequestBody, writer);
                IOUtils.closeQuietly(mRequestBody);
                mRequestBody = null;
            }
        }
    }

    @Override
    public void setRedirectHandler(RedirectHandler redirectHandler) {
        mRedirectHandler = redirectHandler;
    }

    @Override
    public RedirectHandler getRedirectHandler() {
        return mRedirectHandler;
    }

    @Override
    public void setTag(Object tag) {
        this.mTag = tag;
    }

    @Override
    public Object getTag() {
        return this.mTag;
    }

    @Override
    public void setQueue(BlockingQueue<?> queue) {
        blockingQueue = queue;
    }

    @Override
    public boolean inQueue() {
        return blockingQueue != null && blockingQueue.contains(this);
    }

    @Override
    public void start() {
        this.isStart = true;
    }

    @Override
    public boolean isStarted() {
        return isStart;
    }

    @Override
    public void finish() {
        this.isFinished = true;
    }

    @Override
    public boolean isFinished() {
        return isFinished;
    }

    /**
     * Cancel handle.
     *
     * @param cancel true or false.
     * @deprecated use {@link #cancel()} instead.
     */
    @Deprecated
    @Override
    public void cancel(boolean cancel) {
        if (cancel)
            cancel();
    }

    @Override
    public void cancel() {
        if (!isCanceled) {
            isCanceled = true;
            if (hasDefineRequestBody())
                IOUtils.closeQuietly(getDefineRequestBody());

            if (blockingQueue != null)
                blockingQueue.remove(this);

            // cancel file upload
            Set<String> keys = mParamKeyValues.keySet();
            for (String key : keys) {
                List<Object> values = mParamKeyValues.getValues(key);
                for (Object value : values)
                    if (value != null && value instanceof Binary)
                        ((Binary) value).cancel();
            }
        }
    }

    @Override
    public boolean isCanceled() {
        return isCanceled;
    }

    @Override
    public void setCancelSign(Object sign) {
        this.cancelSign = sign;
    }

    @Override
    public void cancelBySign(Object sign) {
        if (cancelSign == sign)
            cancel();
    }

    /**
     * Returns the data "Charset".
     *
     * @return Such as: {@code UTF-8}.
     */
    public String getParamsEncoding() {
        return NoHttp.CHARSET_UTF8;
    }

    /**
     * Is there a Binary data upload ?
     *
     * @return Said true, false said no.
     */
    protected boolean hasBinary() {
        Set<String> keys = mParamKeyValues.keySet();
        for (String key : keys) {
            List<Object> values = mParamKeyValues.getValues(key);
            for (Object value : values) {
                if (value instanceof Binary)
                    return true;
            }
        }
        return false;
    }

    ////////// static module /////////

    /**
     * Split joint non form data.
     *
     * @param paramMap      param map.
     * @param encodeCharset charset.
     * @return string parameter combination, each key value on nails with {@code "&"} space.
     */
    public static StringBuffer buildCommonParams(MultiValueMap<String, Object> paramMap, String encodeCharset) {
        StringBuffer paramBuffer = new StringBuffer();
        Set<String> keySet = paramMap.keySet();
        for (String key : keySet) {
            List<Object> values = paramMap.getValues(key);
            for (Object value : values) {
                if (value != null && value instanceof CharSequence) {
                    paramBuffer.append("&");
                    try {
                        paramBuffer.append(URLEncoder.encode(key, encodeCharset));
                        paramBuffer.append("=");
                        paramBuffer.append(URLEncoder.encode(value.toString(), encodeCharset));
                    } catch (UnsupportedEncodingException e) {
                        Logger.e("Encoding " + encodeCharset + " format is not supported by the system", "Get message extra JSON error!");
                        paramBuffer.append(key);
                        paramBuffer.append("=");
                        paramBuffer.append(value.toString());
                    }
                }
            }
        }
        if (paramBuffer.length() > 0)
            paramBuffer.deleteCharAt(0);
        return paramBuffer;
    }

    /**
     * Accept-Language.
     */
    private static String acceptLanguage;

    /**
     * Create acceptLanguage.
     *
     * @return Returns the client can accept the language types. Such as:zh-CN,zh.
     */
    public static String defaultAcceptLanguage() {
        if (TextUtils.isEmpty(acceptLanguage)) {
            Locale locale = Locale.getDefault();
            String language = locale.getLanguage();
            String country = locale.getCountry();
            StringBuilder acceptLanguageBuilder = new StringBuilder(language);
            if (!TextUtils.isEmpty(country))
                acceptLanguageBuilder.append('-').append(country).append(',').append(language);
            acceptLanguage = acceptLanguageBuilder.toString();
        }
        return acceptLanguage;
    }

    /**
     * Randomly generated boundary mark.
     *
     * @return Random code.
     */
    public static String createBoundary() {
        StringBuffer sb = new StringBuffer("------------------");
        for (int t = 1; t < 12; t++) {
            long time = System.currentTimeMillis() + t;
            if (time % 3L == 0L) {
                sb.append((char) (int) time % '\t');
            } else if (time % 3L == 1L) {
                sb.append((char) (int) (65L + time % 26L));
            } else {
                sb.append((char) (int) (97L + time % 26L));
            }
        }
        return sb.toString();
    }

}