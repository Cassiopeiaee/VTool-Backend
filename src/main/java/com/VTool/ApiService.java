package com.VTool;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ApiService {

    private final RestTemplate restTemplate;

    public ApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getApiData(String url) {
        // API-Aufruf mit GET-Methode
        return restTemplate.getForObject(url, String.class);
    }
}

