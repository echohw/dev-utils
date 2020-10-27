package com.example.devutils.utils.http;

import com.example.devutils.enums.HttpMethodEnum;
import com.example.devutils.utils.collection.MapUtils;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
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

    private Supplier<OkHttpClient> httpClientSupplier;

    public OkHttpUtils() {
        OkHttpClient httpClient = new OkHttpClient();
        this.httpClientSupplier = () -> httpClient;
    }

    public OkHttpUtils(Supplier<OkHttpClient> httpClientSupplier) {
        this.httpClientSupplier = Objects.requireNonNull(httpClientSupplier);
    }

    private OkHttpClient getHttpClient() {
        return httpClientSupplier.get();
    }

    public static RequestBody buildRequestBody(MediaType contentType, byte[] content) {
        return RequestBody.create(contentType, content);
    }

    public static RequestBody buildFormBody(Map<String, String> formData) {
        FormBody.Builder builder = new FormBody.Builder();
        if (MapUtils.isNotEmpty(formData)) {
            formData.forEach(builder::add);
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
            headers.forEach(builder::addHeader);
        }
        return builder.build();
    }

    public static Response sendRequest(OkHttpClient httpClient, Request request) throws IOException {
        return httpClient.newCall(request).execute();
    }

    public static void sendRequest(OkHttpClient httpClient, Request request, Callback callback) {
        httpClient.newCall(request).enqueue(callback);
    }

    public Response sendRequest(String baseUrl, String method, Map<String, String> queryParams, Map<String, String> headers, RequestBody requestBody) throws IOException {
        OkHttpClient httpClient = getHttpClient();
        HttpUrl.Builder builder = HttpUrl.get(baseUrl).newBuilder();
        if (MapUtils.isNotEmpty(queryParams)) {
            queryParams.forEach(builder::addQueryParameter);
        }
        HttpUrl httpUrl = builder.build();
        Request request = buildRequest(httpUrl, method, requestBody, headers);
        return sendRequest(httpClient, request);
    }

    public Response sendGetRequest(String baseUrl, Map<String, String> queryParams, Map<String, String> headers) throws IOException {
        return sendRequest(baseUrl, HttpMethodEnum.GET.name(), queryParams, headers, null);
    }

    public Response sendPostRequest(String baseUrl, Map<String, String> queryParams, Map<String, String> headers, RequestBody requestBody) throws IOException {
        return sendRequest(baseUrl, HttpMethodEnum.POST.name(), queryParams, headers, requestBody);
    }

    public static byte[] getRespBytes(Response response) throws IOException {
        ResponseBody body = response.body();
        byte[] bytes = new byte[0];
        if (body != null) {
            bytes = body.bytes();
        }
        if (!response.isSuccessful()) {
            String url = response.request().url().toString();
            int code = response.code();
            logger.error("{} request fail, code: {}", url, code);
        }
        return bytes;
    }

    public static String getRespText(Response response) throws IOException {
        ResponseBody body = response.body();
        String respText = "";
        if (body != null) {
            respText = body.string();
        }
        if (!response.isSuccessful()) {
            String url = response.request().url().toString();
            int code = response.code();
            logger.error("{} request fail, code: {}", url, code);
        }
        return respText;
    }

}
