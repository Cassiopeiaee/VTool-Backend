package com.VTool;

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

    public String createProjectWithTemplate(String templateId, String xmlPayload) {
        try {
            // Endpunkt mit Template-ID
            String apiUrl = String.format("https://bgn-it.smenso.cloud/skyisland/api/Integration/template/project/create/fd8730f2-fd75-41e8-be8d-fd502a4df113", templateId);

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
            throw new RuntimeException("Fehler bei der Anfrage: " + e.getMessage(), e);
        }
    }
}
