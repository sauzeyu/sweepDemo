package com.vecentek.back.log;

import lombok.NonNull;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @author EdgeYu
 * @since 2023/04/18 14:42
 **/
public class LoggingClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {
    @Override
    @NonNull
    public ClientHttpResponse intercept(@NonNull HttpRequest httpRequest, byte @NonNull [] bytes, @NonNull ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
        System.out.println("Request URI: " + httpRequest.getURI());
        System.out.println("Request Method: " + httpRequest.getMethod());
        System.out.println("Request Headers: " + httpRequest.getHeaders());
        System.out.println("Request Body: " + new String(bytes));

        ClientHttpResponse response = clientHttpRequestExecution.execute(httpRequest, bytes);

        System.out.println("Response Status Code: " + response.getStatusCode());
        System.out.println("Response Headers: " + response.getHeaders());
        System.out.println("Response Body: " + StreamUtils.copyToString(response.getBody(), Charset.defaultCharset()));

        return response;
    }
}
