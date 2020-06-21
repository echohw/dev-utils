package com.example.devutils.utils.net;

import com.example.devutils.dep.UserAgents;
import com.example.devutils.utils.UserAgentUtils;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Ame on 2020-05-07 16:39.
 */
public class HttpUtils {

    private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    public static OkHttpClient getHttpClient() {
        return new OkHttpClient.Builder().readTimeout(30, TimeUnit.SECONDS).build();
    }

    public static Request getRequest(String url, RequestMethod requestMethod, RequestBody body) {
        Builder builder = new Builder()
            .url(url)
            .method(requestMethod.name(), body)
            .header("User-Agent", UserAgentUtils.random());
        return builder.build();
    }

    private static Optional<byte[]> getRespContent(OkHttpClient httpClient, Request request) throws RuntimeException {
        String url = request.url().toString();
        try {
            Response response = httpClient.newCall(request).execute();
            int code = response.code();
            if (response.isSuccessful()) {
                logger.info("{} request success, code: {}", url, code);
            } else {
                logger.error("{} request fail, code: {}", url, code);
            }
            ResponseBody body = response.body();
            if (body != null) {
                return Optional.of(body.bytes());
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return Optional.empty();
    }

    public static Optional<byte[]> get(String url) {
        OkHttpClient httpClient = getHttpClient();
        Request request = getRequest(url, RequestMethod.GET, null);
        try {
            return getRespContent(httpClient, request);
        } catch (RuntimeException ex) {
            logger.error("response content acquisition failed");
        };
        return Optional.empty();
    }

    public static Optional<byte[]> post(String url, RequestBody requestBody) {
        OkHttpClient httpClient = getHttpClient();
        Request request = getRequest(url, RequestMethod.POST, requestBody);
        try {
            return getRespContent(httpClient, request);
        } catch (RuntimeException ex) {
            logger.error("response content acquisition failed");
        }
        return Optional.empty();
    }
}
