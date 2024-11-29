package com.VTool;

import org.springframework.http.HttpMethod;

import java.util.List;


import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

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


    public String fetchAndConvertCsvToXml() {
        try {
            // Abrufen der CSV-Daten
            String csvData = fetchCsvData();

            // Konvertierung von CSV zu XML
            String xmlData = convertCsvToXml(csvData);

            // Validierung der XML-Daten
            validateXml(xmlData);

            return xmlData;
        } catch (Exception e) {
            throw new RuntimeException("Fehler beim Verarbeiten der CSV-Daten: " + e.getMessage(), e);
        }
    }

    private String fetchCsvData() {
        try {
            String apiUrl = "https://bgn-it.smenso.cloud/skyisland/api/Reports/projects/aaf1cdae-9da4-4493-85fb-92fc3fb162b5?view=e813c779-f5ed-4fce-91ca-1ec9f67b0262&filter=active&format=CSV";

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Basic NjA0ZGY5NWEtNjNmZi00YTU3LWJjYTUtNGYxMDlkZjEwN2Y1OnlYaG1PR1M0VjQwZ0FzV1VBYlJvU2h0SXMxRW41Q255");
            headers.setAccept(List.of(MediaType.TEXT_PLAIN));

            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.GET, requestEntity, String.class);

            if (response.getBody() == null || response.getBody().isEmpty()) {
                throw new RuntimeException("Keine CSV-Daten erhalten.");
            }

            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Fehler beim Abrufen der CSV-Daten: " + e.getMessage(), e);
        }
    }

    private String convertCsvToXml(String csvData) {
        try {
            // Bereinigen der CSV-Daten
            String sanitizedCsvData = sanitizeCsv(csvData);
    
            // Baue das XML
            StringBuilder xmlBuilder = new StringBuilder("<Projects>");
    
            // Zeilen in der CSV-Daten
            String[] lines = sanitizedCsvData.split("\n");
            if (lines.length <= 1) {
                throw new RuntimeException("CSV enthält keine Projektdaten.");
            }
    
            // Kopfzeile überspringen und Datenzeilen iterieren
            for (int i = 1; i < lines.length; i++) {
                String[] values = lines[i].split(",", -1); // -1 behält leere Felder bei
    
                xmlBuilder.append("<Project>");
                xmlBuilder.append("<Id>").append(values[0].trim()).append("</Id>");
                xmlBuilder.append("<Title><![CDATA[").append(values[1].trim()).append("]]></Title>");
                xmlBuilder.append("<Start>").append(values[2].trim()).append("</Start>");
                xmlBuilder.append("<End>").append(values[3].trim()).append("</End>");
                xmlBuilder.append("<Description><![CDATA[").append(values[4].trim()).append("]]></Description>");
                xmlBuilder.append("<TypeId>").append(values[5].trim()).append("</TypeId>");
                xmlBuilder.append("<FolderId>").append(values[6].trim()).append("</FolderId>");
                xmlBuilder.append("<LocationId>").append(values[7].trim()).append("</LocationId>");
                xmlBuilder.append("<Private>").append(values[8].trim()).append("</Private>");
                xmlBuilder.append("</Project>");
            }
    
            xmlBuilder.append("</Projects>");
            return xmlBuilder.toString();
        } catch (Exception e) {
            throw new RuntimeException("Fehler bei der Konvertierung von CSV zu XML: " + e.getMessage(), e);
        }
    }

    private String sanitizeCsv(String csvData) {
        // Entferne ungültige XML-Zeichen
        return csvData.replaceAll("[^\\x20-\\x7E]", "") // Nicht-druckbare Zeichen entfernen
                      .replaceAll("&", "&amp;")        // Ersetze & durch &amp;
                      .replaceAll("<", "&lt;")         // Ersetze < durch &lt;
                      .replaceAll(">", "&gt;");        // Ersetze > durch &gt;
    }

    private void validateXml(String xmlData) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.parse(new InputSource(new StringReader(xmlData)));
        } catch (Exception e) {
            throw new RuntimeException("Generiertes XML ist ungültig: " + e.getMessage(), e);
        }
    }
}
