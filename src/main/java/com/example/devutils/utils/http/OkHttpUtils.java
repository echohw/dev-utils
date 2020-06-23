package com.example.devutils.utils.http;

import com.example.devutils.dep.HttpMethods;
import com.example.devutils.utils.collection.MapUtils;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.MultipartBody.Part;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by AMe on 2020-06-23 19:44.
 */
public class OkHttpUtils {

    private static final Logger logger = LoggerFactory.getLogger(OkHttpUtils.class);

    public static OkHttpClient getHttpClient() {
        return new OkHttpClient();
    }

    public static RequestBody buildRequestBody(MediaType contentType, byte[] content) {
        return RequestBody.create(contentType, content);
    }

    public static RequestBody buildFormBody(Map<String, String> formData) {
        FormBody.Builder builder = new FormBody.Builder();
        if (MapUtils.isNotEmpty(formData)) {
            for (Entry<String, String> entry : formData.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }
        }
        return builder.build();
    }

    public static RequestBody buildMultipartBody(MediaType mediaType, List<Part> parts) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(mediaType);
        parts.forEach(builder::addPart);
        return builder.build();
    }

    public static Request buildRequest(String url, String method, RequestBody body, Map<String, String> headers) {
        return buildRequest(HttpUrl.get(url), method, body, headers);
    }

    public static Request buildRequest(HttpUrl url, String method, RequestBody body, Map<String, String> headers) {
        Builder builder = new Builder()
            .url(url)
            .method(method, body);
        if (MapUtils.isNotEmpty(headers)) {
            for (Entry<String, String> entry : headers.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        return builder.build();
    }

    public static Response sendRequest(OkHttpClient httpClient, Request request) throws IOException {
        return httpClient.newCall(request).execute();
    }

    public static void sendRequest(OkHttpClient httpClient, Request request, Callback callback) {
        httpClient.newCall(request).enqueue(callback);
    }

    public static Response sendRequest(String baseUrl, String method, Map<String, String> queryParams, Map<String, String> headers, RequestBody requestBody) throws IOException {
        OkHttpClient httpClient = getHttpClient();
        HttpUrl.Builder builder = HttpUrl.get(baseUrl).newBuilder();
        if (MapUtils.isNotEmpty(queryParams)) {
            for (Entry<String, String> entry : queryParams.entrySet()) {
                builder.addQueryParameter(entry.getKey(), entry.getValue());
            }
        }
        HttpUrl httpUrl = builder.build();
        Request request = buildRequest(httpUrl, method, requestBody, headers);
        return sendRequest(httpClient, request);
    }

    public static Response sendGetRequest(String baseUrl, Map<String, String> queryParams, Map<String, String> headers) throws IOException {
        return sendRequest(baseUrl, HttpMethods.GET, queryParams, headers, null);
    }

    public static Response sendPostRequest(String baseUrl, Map<String, String> queryParams, Map<String, String> headers, RequestBody requestBody) throws IOException {
        return sendRequest(baseUrl, HttpMethods.POST, queryParams, headers, requestBody);
    }

    public static byte[] getRespBytes(Response response) throws IOException {
        if (response.isSuccessful()) {
            ResponseBody body = response.body();
            if (body != null) {
                return body.bytes();
            }
        } else {
            String url = response.request().url().toString();
            int code = response.code();
            logger.error("{} request fail, code: {}", url, code);
        }
        return new byte[0];
    }

    public static String getRespText(Response response) throws IOException {
        if (response.isSuccessful()) {
            ResponseBody body = response.body();
            if (body != null) {
                return body.string();
            }
        } else {
            String url = response.request().url().toString();
            int code = response.code();
            logger.error("{} request fail, code: {}", url, code);
        }
        return "";
    }

}
