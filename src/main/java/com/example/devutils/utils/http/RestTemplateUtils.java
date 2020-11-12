package com.example.devutils.utils.http;

import com.example.devutils.utils.io.StreamUtils;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;
import org.jooq.lambda.Unchecked;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMessage;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.web.client.HttpMessageConverterExtractor;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

/**
 * Created by AMe on 2020-06-26 17:07.
 */
public class RestTemplateUtils {

    private final RestTemplate restTemplate;

    public RestTemplateUtils(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public RestTemplate restTemplate() {
        return restTemplate;
    }

    public <T> T getForObject(String url, Class<T> responseType, HttpHeaders headers, Object... uriVariables) {
        RequestCallback requestCallback = getHttpHeadersRequestCallback(headers);
        ResponseExtractor<T> responseExtractor = getHttpMessageConverterExtractor(responseType);
        return getForObject(url, requestCallback, responseExtractor, uriVariables);
    }

    public <T> T getForObject(String url, RequestCallback requestCallback, ResponseExtractor<T> responseExtractor, Object... uriVariables) {
        return restTemplate().execute(url, HttpMethod.GET, requestCallback, responseExtractor, uriVariables);
    }

    public <T> T getForObject(String url, Class<T> responseType, HttpHeaders headers, Map<String, ?> uriVariables) {
        RequestCallback requestCallback = getHttpHeadersRequestCallback(headers);
        ResponseExtractor<T> responseExtractor = getHttpMessageConverterExtractor(responseType);
        return getForObject(url, requestCallback, responseExtractor, uriVariables);
    }

    public <T> T getForObject(String url, RequestCallback requestCallback, ResponseExtractor<T> responseExtractor, Map<String, ?> uriVariables) {
        return restTemplate().execute(url, HttpMethod.GET, requestCallback, responseExtractor, uriVariables);
    }

    public <T> ResponseEntity<T> getForEntity(String url, Class<T> responseType, HttpHeaders headers, Object... uriVariables) {
        RequestCallback requestCallback = getHttpHeadersRequestCallback(headers);
        ResponseExtractor<ResponseEntity<T>> responseExtractor = getResponseEntityResponseExtractor(getHttpMessageConverterExtractor(responseType));
        return nonNull(getForObject(url, requestCallback, responseExtractor, uriVariables));
    }

    public <T> ResponseEntity<T> getForEntity(String url, Class<T> responseType, HttpHeaders headers, Map<String, ?> uriVariables) {
        RequestCallback requestCallback = getHttpHeadersRequestCallback(headers);
        ResponseExtractor<ResponseEntity<T>> responseExtractor = getResponseEntityResponseExtractor(getHttpMessageConverterExtractor(responseType));
        return nonNull(getForObject(url, requestCallback, responseExtractor, uriVariables));
    }

    public RequestCallback getAcceptHeaderRequestCallback(Class<?> responseType) {
        return restTemplate().acceptHeaderRequestCallback(responseType);
    }

    public RequestCallback getHttpEntityRequestCallback(Object requestBody, Type responseType) {
        return restTemplate().httpEntityCallback(requestBody, responseType);
    }

    public RequestCallback getHttpHeadersRequestCallback(HttpHeaders requestHeaders) {
        return request -> {
            HttpHeaders httpHeaders = request.getHeaders();
            if (requestHeaders != null && !requestHeaders.isEmpty()) {
                requestHeaders.forEach((key, values) -> httpHeaders.put(key, new LinkedList<>(values)));
            }
        };
    }

    public ResponseExtractor<HttpHeaders> getHeadersResponseExtractor() {
        return HttpMessage::getHeaders;
    }

    public <T> ResponseExtractor<T> getHttpMessageConverterExtractor(Class<T> responseType) {
        return (responseType != null && Void.class != responseType) ? new HttpMessageConverterExtractor<>(responseType, restTemplate().getMessageConverters()) : null;
    }

    public <T> ResponseExtractor<ResponseEntity<T>> getResponseEntityResponseExtractor(ResponseExtractor<T> delegate) {
        return response -> {
            T body = Optional.ofNullable(delegate).map(Unchecked.function(extractor -> extractor.extractData(response))).orElse(null);
            return ResponseEntity.status(response.getRawStatusCode()).headers(response.getHeaders()).body(body);
        };
    }

    public ResponseExtractor<byte[]> getByteArrayResponseExtractor() {
        return response -> StreamUtils.readBytes(response.getBody());
    }

    private static <T> T nonNull(@Nullable T result) {
        Assert.state(result != null, "No result");
        return result;
    }
}
