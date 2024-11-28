package com.VTool;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SmensoApiService {

    private final RestTemplate restTemplate;

    public SmensoApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String createProject(String xmlPayload) {
        try {
            
            String apiUrl = "https://bgn-it.smenso.cloud/skyisland/api/Integration/project";

            // Header konfigurieren
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Basic N2E4NzU5YjItY2NlMC00MTQzLWIzMmYtM2Q4ZTljNzdkY2UxOk1ab0loNDJLQ01yR1VLVmNBSGN3ZHNHWXJkUnU1cGhl");
            headers.setContentType(MediaType.APPLICATION_XML);

            // Anfrage erstellen
            HttpEntity<String> requestEntity = new HttpEntity<>(xmlPayload, headers);

            // API-Aufruf
            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, requestEntity, String.class);

            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Fehler beim Erstellen des Projekts: " + e.getMessage(), e);
        }
    }

    public String getProjectById(String projectId) {
        try {
            
            String apiUrl = String.format("https://bgn-it.smenso.cloud/skyisland/api/Integration/project", projectId);

            // Header konfigurieren
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Basic N2E4NzU5YjItY2NlMC00MTQzLWIzMmYtM2Q4ZTljNzdkY2UxOk1ab0loNDJLQ01yR1VLVmNBSGN3ZHNHWXJkUnU1cGhl");

            // Anfrage erstellen
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

            // API-Aufruf
            ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.GET, requestEntity, String.class);

            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Fehler beim Abrufen des Projekts: " + e.getMessage(), e);
        }
    }
}
