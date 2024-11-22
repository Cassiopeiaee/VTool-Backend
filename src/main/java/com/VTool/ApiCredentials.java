package com.VTool;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApiCredentials {

    @Value("${smenso.api.token}")
    private String apiToken;

    @Value("${smenso.api.url}")
    private String apiUrl;

    public String getApiToken() {
        return apiToken;
    }

    public String getApiUrl() {
        return apiUrl;
    }
}
