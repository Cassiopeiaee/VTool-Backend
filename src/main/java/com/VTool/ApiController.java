package com.VTool;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

    private final ApiService apiService;

    public ApiController(ApiService apiService) {
        this.apiService = apiService;
    }

    @GetMapping("/fetch-data")
    public String fetchDataFromApi(@RequestParam String apiUrl) {
        // Übergibt die URL und gibt die API-Antwort zurück
        return apiService.getApiData(apiUrl);
    }
}

