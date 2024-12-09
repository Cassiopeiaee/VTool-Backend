package com.VTool;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import com.opencsv.CSVParser;
import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.nio.charset.StandardCharsets;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

@Service
public class SmensoApiService {

    private static final Logger logger = LoggerFactory.getLogger(SmensoApiService.class);
    private final RestTemplate restTemplate;
    private final ProjectDataRepository projectDataRepository;

    public SmensoApiService(RestTemplate restTemplate, ProjectDataRepository projectDataRepository) {
        this.restTemplate = restTemplate;
        this.projectDataRepository = projectDataRepository;
    }


    public void saveProjects(List<ProjectData> projectDataList) {
        projectDataRepository.saveAll(projectDataList);
    }





    public String fetchProjectReport(String guid, String filter, String format) {
        String apiUrl = "https://bgn-it.smenso.cloud/skyisland/api/Reports/projects/" + guid 
                        + "?view=e813c779-f5ed-4fce-91ca-1ec9f67b0262&filter=" + filter + "&format=" + format;
    
        logger.info("API URL: {}", apiUrl);
    
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic N2E4NzU5YjItY2NlMC00MTQzLWIzMmYtM2Q4ZTljNzdkY2UxOk1ab0loNDJLQ01yR1VLVmNBSGN3ZHNHWXJkUnU1cGhl");
        headers.set("Accept", "text/csv");
    
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
    
        try {
            // API-Antwort als byte[] abrufen, um Dekodierungsprobleme zu umgehen
            ResponseEntity<byte[]> response = restTemplate.exchange(apiUrl, HttpMethod.GET, requestEntity, byte[].class);
    
            logger.info("Response Status: {}", response.getStatusCode());
            logger.info("Response Headers: {}", response.getHeaders());
    
            if (response.getStatusCode() == HttpStatus.OK) {
                try {
                    // CSV-Daten dekodieren
                    String decodedCsv = new String(response.getBody(), StandardCharsets.UTF_8);
                    logger.info("Decoded CSV Data: {}", decodedCsv);
                    return decodedCsv;
                } catch (Exception e) {
                    logger.error("Error decoding CSV data", e);
                    throw new RuntimeException("Fehler beim Dekodieren der CSV-Daten: " + e.getMessage());
                }
            } else {
                throw new RuntimeException("API returned status: " + response.getStatusCode());
            }
        } catch (Exception e) {
            logger.error("Error during API call", e);
            throw new RuntimeException("Fehler beim Abrufen des Projekts: " + e.getMessage());
        }
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


    public List<Map<String, String>> getProjectsReportAsJson(String csvData) {
        try (BufferedReader reader = new BufferedReader(new StringReader(csvData))) {
            String headerLine = reader.readLine(); // Erste Zeile enthält Header

            if (headerLine == null || headerLine.isEmpty()) {
                throw new RuntimeException("CSV-Daten enthalten keine Header");
            }

            String[] headers = headerLine.split(","); // Header in Felder aufteilen
            List<Map<String, String>> jsonData = new ArrayList<>();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",", -1); // Datenzeile in Felder aufteilen
                Map<String, String> row = new HashMap<>();

                for (int i = 0; i < headers.length; i++) {
                    row.put(headers[i].trim(), i < values.length ? values[i].trim() : ""); // Fehlende Werte behandeln
                }
                jsonData.add(row);
            }

            return jsonData;
        } catch (Exception e) {
            throw new RuntimeException("Fehler beim Verarbeiten der CSV-Daten", e);
        }
    }


    public String fetchSingleProject(String guid) {
        String url = "https://bgn-it.smenso.cloud/skyisland/api/Reports/projects/" + guid
                + "?view=e813c779-f5ed-4fce-91ca-1ec9f67b0262&filter=active&format=CSV";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic N2E4NzU5YjItY2NlMC00MTQzLWIzMmYtM2Q4ZTljNzdkY2UxOk1ab0loNDJLQ01yR1VLVmNBSGN3ZHNHWXJkUnU1cGhl");
        headers.set("Accept", "text/csv");

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                String.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            throw new RuntimeException("Fehler beim Abrufen des Projekts: " + response.getStatusCode());
        }
    }






public String getProjectsReport(String viewId, String filter, String format) {
        try {
            
            String apiUrl = String.format(
                "https://bgn-it.smenso.cloud/skyisland/api/Reports/projects?view=%s&filter=%s&format=%s",
                viewId, filter, format
            );

            // Header konfigurieren
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Basic NjA0ZGY5NWEtNjNmZi00YTU3LWJjYTUtNGYxMDlkZjEwN2Y1OnlYaG1PR1M0VjQwZ0FzV1VBYlJvU2h0SXMxRW41Q255");
            headers.setAccept(List.of(MediaType.TEXT_PLAIN)); 

            // Anfrage erstellen
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

            // API-Aufruf
            ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.GET, requestEntity, String.class);

            
            return response.getBody();
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Client-Fehler: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
        } catch (HttpServerErrorException e) {
            throw new RuntimeException("Server-Fehler: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
        } catch (Exception e) {
            throw new RuntimeException("Allgemeiner Fehler: " + e.getMessage(), e);
        }
    }






    @Transactional
    public void saveProjectData(List<ProjectData> projectDataList) {
        projectDataRepository.saveAll(projectDataList);
    }

    public void saveCsvDataToDatabase(String csvData) {
        List<ProjectData> projectDataList = parseCsvToProjectData(csvData);
        projectDataRepository.saveAll(projectDataList);
    }




    public List<ProjectData> parseCsvToProjectData(String csvData) {
    List<ProjectData> projects = new ArrayList<>();
    try {
        // OpenCSV-Parser erstellen
        CSVReader reader = new CSVReader(new StringReader(csvData));
        String[] headers = reader.readNext(); // Header-Zeile lesen

        if (headers == null) {
            throw new RuntimeException("Die CSV-Daten enthalten keine Header-Zeile.");
        }

        String[] line;
        while ((line = reader.readNext()) != null) {
            // Überspringen von leeren Zeilen
            if (line.length == 0 || line[0].trim().isEmpty()) {
                continue;
            }

            ProjectData project = new ProjectData();

            // Mapping der CSV-Werte zu den Feldern
            project.setId(getValue(headers, line, "Id"));
            project.setTitle(getValue(headers, line, "Title"));
            project.setStatus(getValue(headers, line, "Status"));

            // Progress behandeln
            try {
                project.setProgress(Integer.parseInt(getValue(headers, line, "Progress")));
            } catch (NumberFormatException e) {
                System.err.println("Ungültiger Progress-Wert: " + getValue(headers, line, "Progress"));
                project.setProgress(0); // Standardwert setzen
            }

            project.setCostStatus(getValue(headers, line, "Cost Status"));

            // Start- und Enddate setzen
            String startDate = getValue(headers, line, "Start date");
            String endDate = getValue(headers, line, "End date");

            project.setStartDate((startDate == null || startDate.isEmpty()) ? "Nicht verfügbar" : startDate);
            project.setEndDate((endDate == null || endDate.isEmpty()) ? "Nicht verfügbar" : endDate);

            System.out.println("Geparstes Projekt: " + project);
            projects.add(project);
        }
    } catch (Exception e) {
        throw new RuntimeException("Fehler beim Parsing der CSV-Daten", e);
    }
    return projects;
}

private String getValue(String[] headers, String[] values, String columnName) {
    for (int i = 0; i < headers.length; i++) {
        if (headers[i].equalsIgnoreCase(columnName)) {
            return i < values.length ? values[i].trim() : "";
        }
    }
    return ""; // Standardwert, falls die Spalte nicht gefunden wird
}
    // Helper-Methode zur Bereinigung
    private String sanitizeDate(String dateValue) {
        if (dateValue == null || dateValue.trim().isEmpty() || dateValue.trim().equals("0")) {
            return "Nicht verfügbar";
        }
        return dateValue.trim();
    }

    


    private LocalDate parseDate(String dateStr) {
    if (dateStr == null || dateStr.isEmpty() || dateStr.equals("0")) {
        return null; // Ungültige Werte werden ignoriert
    }

    try {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        return LocalDate.parse(dateStr, formatter);
    } catch (DateTimeParseException e) {
        throw new RuntimeException("Ungültiges Datumsformat: " + dateStr, e);
    }
}

}
