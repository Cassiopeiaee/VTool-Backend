package com.VTool;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.io.BufferedReader;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
            ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.GET, requestEntity, String.class);
    
            logger.info("Response Status: {}", response.getStatusCode());
            logger.info("Response Body: {}", response.getBody());
    
            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
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
            headers.setAccept(List.of(MediaType.TEXT_PLAIN)); // Akzeptiere CSV als Text

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
        saveProjectData(projectDataList);
    }

    private List<ProjectData> parseCsvToProjectData(String csvData) {
        try (BufferedReader reader = new BufferedReader(new StringReader(csvData))) {
            String headerLine = reader.readLine(); // Erste Zeile enthält Header

            if (headerLine == null || headerLine.isEmpty()) {
                throw new RuntimeException("CSV-Daten enthalten keine Header");
            }

            String[] headers = headerLine.split(","); // Header in Felder aufteilen
            List<ProjectData> projectDataList = new ArrayList<>();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",", -1); // Datenzeile in Felder aufteilen
                ProjectData projectData = new ProjectData();

                projectData.setId(values[getIndex(headers, "Id")]);
                projectData.setTitle(values[getIndex(headers, "Title")]);
                projectData.setType(values[getIndex(headers, "Type")]);
                projectData.setDescription(values[getIndex(headers, "Description")]);
                projectData.setStatus(values[getIndex(headers, "Status")]);
                projectData.setStartDate(parseDate(values[getIndex(headers, "Start Date")]));
                projectData.setEndDate(parseDate(values[getIndex(headers, "End Date")]));

                projectDataList.add(projectData);
            }

            return projectDataList;
        } catch (Exception e) {
            throw new RuntimeException("Fehler beim Verarbeiten der CSV-Daten", e);
        }
    }

    private int getIndex(String[] headers, String headerName) {
        for (int i = 0; i < headers.length; i++) {
            if (headers[i].trim().equalsIgnoreCase(headerName)) {
                return i;
            }
        }
        throw new IllegalArgumentException("Header '" + headerName + "' nicht gefunden");
    }

    private LocalDate parseDate(String date) {
        return date.isEmpty() ? null : LocalDate.parse(date, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
    }

    private LocalDateTime parseDateTime(String dateTime) {
        return dateTime.isEmpty() ? null : LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss"));
    }

    private Double parseDouble(String number) {
        return number.isEmpty() ? null : Double.parseDouble(number);
    }
}
