package com.bitozen.hms.pm.common.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component("pmRequestUtil")
@PropertySource("classpath:hms.pm.token.properties")
public class RequestUtil {

    private final String HEADER_KEY = "Authorization";

    @Value("${hms.pm.token}")
    private String TRX_TOKEN;

    public HttpEntity<String> getPreFormattedRequestWithToken() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        httpHeaders.add(HEADER_KEY, TRX_TOKEN);

        return new HttpEntity<>(httpHeaders);
    }

}
