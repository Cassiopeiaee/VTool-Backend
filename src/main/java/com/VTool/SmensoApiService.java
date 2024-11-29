package com.VTool;

import org.springframework.http.HttpMethod;

import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
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

public String getProjectsReport(String viewId, String filter, String format) {
        try {
            // API-Endpunkt mit Parametern
            String apiUrl = String.format(
                "https://bgn-it.smenso.cloud/skyisland/api/Reports/projects?view=%s&filter=%s&format=%s",
                viewId, filter, format
            );

            // Header konfigurieren
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Basic NjA0ZGY5NWEtNjNmZi00YTU3LWJjYTUtNGYxMDlkZjEwN2Y1OnlYaG1PR1M0VjQwZ0FzV1VBYlJvU2h0SXMxRW41Q255");
            headers.setAccept(List.of(MediaType.TEXT_PLAIN)); // Akzeptiere CSV als Text

            // Anfrage erstellen
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

            // API-Aufruf
            ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.GET, requestEntity, String.class);

            // Rückgabe der CSV-Daten
            return response.getBody();
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Client-Fehler: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
        } catch (HttpServerErrorException e) {
            throw new RuntimeException("Server-Fehler: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
        } catch (Exception e) {
            throw new RuntimeException("Allgemeiner Fehler: " + e.getMessage(), e);
        }
    }

}
