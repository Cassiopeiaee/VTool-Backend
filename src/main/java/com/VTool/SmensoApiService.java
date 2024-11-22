package com.VTool;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import java.io.StringWriter;


@Service
public class SmensoApiService {

    private final RestTemplate restTemplate;
    private final ApiCredentials apiCredentials;

    public SmensoApiService(RestTemplate restTemplate, ApiCredentials apiCredentials) {
        this.restTemplate = restTemplate;
        this.apiCredentials = apiCredentials;
    }

    public String createProject(ProjectRequest projectRequest) {
        try {
            // API-Endpunkt URL
            String url = apiCredentials.getApiUrl() + "/projects";

            // Header konfigurieren
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Basic " + apiCredentials.getApiToken());
            headers.setContentType(MediaType.APPLICATION_XML);

            // Projekt in XML serialisieren
            String xmlPayload = convertToXml(projectRequest);

            // Anfrage erstellen
            HttpEntity<String> requestEntity = new HttpEntity<>(xmlPayload, headers);

            // API-Aufruf
            ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Fehler bei der Anfrage: " + e.getMessage(), e);
        }
    }

    private String convertToXml(ProjectRequest projectRequest) throws Exception {
        // XML-Serialisierung (mit JAXB)
        JAXBContext context = JAXBContext.newInstance(ProjectRequest.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        StringWriter writer = new StringWriter();
        marshaller.marshal(projectRequest, writer);

        return writer.toString();
    }
}
