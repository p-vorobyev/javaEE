package ru.voroby.controller;

import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.io.support.ClassicRequestBuilder;
import org.apache.hc.core5.http.message.BasicHeader;
import org.apache.hc.core5.net.URIBuilder;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

public class HttpUtils {

    static ClassicHttpRequest post(String url, String json, BasicHeader... headers) throws URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder(url);
        ClassicHttpRequest request = ClassicRequestBuilder
                .post(uriBuilder.build())
                .setEntity(Optional.ofNullable(json).orElse(""))
                .build();
        List.of(headers).forEach(request::addHeader);
        return request;
    }

    static ClassicHttpRequest post(String url, BasicHeader... headers) throws URISyntaxException {
        return post(url, null, headers);
    }

}
