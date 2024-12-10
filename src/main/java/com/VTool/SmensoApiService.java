package com.VTool;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.opencsv.CSVReader;

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



        public byte[] generateExcelForProject(ProjectData project, String id) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Project Data");

        String[] columns = {
            "Id", "Code", "Type", "Title", "Description", "Benefit", "Goal", "Private",
            "Template", "Archived", "Block times recording", "Status", "Start date", "End date",
            "Budget", "Folder", "Department", "Location", "Project Manager", "Project Manager E-Mail",
            "Status Name", "Status Date", "Overall Status", "Target Date", "Progress",
            "Appointments Status", "Costs Status", "Goals Status", "Explanation", "Next steps",
            "Plan Cost", "Actual Cost", "Plan Duration", "Actual Effort", "Forecast Effort",
            "Recorded Time", "Workflow", "Task Template", "Created On", "Created By",
            "Updated On", "Updated By", "Labels", "LockedFlavors"
        };

        int rowIndex = 0;
        XSSFRow headerRow = sheet.createRow(rowIndex++);

        // Header erstellen
        for (int i = 0; i < columns.length; i++) {
            headerRow.createCell(i).setCellValue(columns[i]);
        }

        // Datenzeile erstellen
        XSSFRow dataRow = sheet.createRow(rowIndex++);
        dataRow.createCell(0).setCellValue(project.getId());
        dataRow.createCell(1).setCellValue(project.getCode());
        dataRow.createCell(2).setCellValue(project.getType());
        dataRow.createCell(3).setCellValue(project.getTitle());
        dataRow.createCell(4).setCellValue(project.getDescription());
        dataRow.createCell(5).setCellValue(project.getBenefit());
        dataRow.createCell(6).setCellValue(project.getGoal());
        dataRow.createCell(7).setCellValue(project.getPrivateField());
        dataRow.createCell(8).setCellValue(project.getTemplate());
        dataRow.createCell(9).setCellValue(project.getArchived());
        dataRow.createCell(10).setCellValue(project.getBlockTimesRecording());
        dataRow.createCell(11).setCellValue(project.getStatus());
        dataRow.createCell(12).setCellValue(project.getStartDate());
        dataRow.createCell(13).setCellValue(project.getEndDate());
        dataRow.createCell(14).setCellValue(project.getBudget());
        dataRow.createCell(15).setCellValue(project.getFolder());
        dataRow.createCell(16).setCellValue(project.getDepartment());
        dataRow.createCell(17).setCellValue(project.getLocation());
        dataRow.createCell(18).setCellValue(project.getProjectManager());
        dataRow.createCell(19).setCellValue(project.getProjectManagerEmail());
        dataRow.createCell(20).setCellValue(project.getStatusName());
        dataRow.createCell(21).setCellValue(project.getStatusDate());
        dataRow.createCell(22).setCellValue(project.getOverallStatus());
        dataRow.createCell(23).setCellValue(project.getTargetDate());
        dataRow.createCell(24).setCellValue(project.getProgress());
        dataRow.createCell(25).setCellValue(project.getAppointmentsStatus());
        dataRow.createCell(26).setCellValue(project.getCostsStatus());
        dataRow.createCell(27).setCellValue(project.getGoalsStatus());
        dataRow.createCell(28).setCellValue(project.getExplanation());
        dataRow.createCell(29).setCellValue(project.getNextSteps());
        dataRow.createCell(30).setCellValue(project.getPlanCost());
        dataRow.createCell(31).setCellValue(project.getActualCost());
        dataRow.createCell(32).setCellValue(project.getPlanDuration());
        dataRow.createCell(33).setCellValue(project.getActualEffort());
        dataRow.createCell(34).setCellValue(project.getForecastEffort());
        dataRow.createCell(35).setCellValue(project.getRecordedTime());
        dataRow.createCell(36).setCellValue(project.getWorkflow());
        dataRow.createCell(37).setCellValue(project.getTaskTemplate());
        dataRow.createCell(38).setCellValue(project.getCreatedOn());
        dataRow.createCell(39).setCellValue(project.getCreatedBy());
        dataRow.createCell(40).setCellValue(project.getUpdatedOn());
        dataRow.createCell(41).setCellValue(project.getUpdatedBy());
        dataRow.createCell(42).setCellValue(project.getLabels());
        dataRow.createCell(43).setCellValue(project.getLockedFlavors());

        // Optional: Spaltenbreite automatisch anpassen
        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);
        workbook.close();
        return bos.toByteArray();
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


    public String createProjectFromTemplate(String templateId, String xmlPayload) {
        try {
            String apiUrl = "https://bgn-it.smenso.cloud/skyisland/api/Integration/template/project/create/" + templateId;

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Basic N2E4NzU5YjItY2NlMC00MTQzLWIzMmYtM2Q4ZTljNzdkY2UxOk1ab0loNDJLQ01yR1VLVmNBSGN3ZHNHWXJkUnU1cGhl"); 
            headers.setContentType(MediaType.APPLICATION_XML);
            headers.setAccept(List.of(MediaType.APPLICATION_XML));

            HttpEntity<String> requestEntity = new HttpEntity<>(xmlPayload, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, requestEntity, String.class);

            return response.getBody();
        } catch (Exception e) {
            logger.error("Fehler beim Erstellen des Projekts aus Vorlage: ", e);
            throw new RuntimeException("Fehler beim Erstellen des Projekts aus der Vorlage: " + e.getMessage(), e);
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
    
                // Mapping der bereits existierenden Felder
                project.setId(getValue(headers, line, "Id"));
                project.setTitle(getValue(headers, line, "Title"));
                project.setStatus(getValue(headers, line, "Status"));
                try {
                    project.setProgress(Integer.parseInt(getValue(headers, line, "Progress")));
                } catch (NumberFormatException e) {
                    System.err.println("Ungültiger Progress-Wert: " + getValue(headers, line, "Progress"));
                    project.setProgress(0); // Standardwert setzen
                }
    
                project.setCostStatus(getValue(headers, line, "Cost Status"));
    
                // Setze Budget als String
                String budgetStr = getValue(headers, line, "Budget");
                project.setBudget(budgetStr != null && !budgetStr.isEmpty() ? budgetStr : "0");
    
                // Setze Overall Status als String
                project.setOverallStatus(getValue(headers, line, "Overall Status"));
    
                String startDate = getValue(headers, line, "Start date");
                String endDate = getValue(headers, line, "End date");
    
                project.setStartDate((startDate == null || startDate.isEmpty()) ? "Nicht verfügbar" : startDate);
                project.setEndDate((endDate == null || endDate.isEmpty()) ? "Nicht verfügbar" : endDate);
    
                // Weitere Felder setzen...
                project.setCode(getValue(headers, line, "Code"));
                project.setType(getValue(headers, line, "Type"));
                project.setDescription(getValue(headers, line, "Description"));
                project.setBenefit(getValue(headers, line, "Benefit"));
                project.setGoal(getValue(headers, line, "Goal"));
                project.setPrivateField(getValue(headers, line, "Private"));
                project.setTemplate(getValue(headers, line, "Template"));
                project.setArchived(getValue(headers, line, "Archived"));
                project.setBlockTimesRecording(getValue(headers, line, "Block times recording"));
                project.setFolder(getValue(headers, line, "Folder"));
                project.setDepartment(getValue(headers, line, "Department"));
                project.setLocation(getValue(headers, line, "Location"));
                project.setProjectManager(getValue(headers, line, "Project Manager"));
                project.setProjectManagerEmail(getValue(headers, line, "Project Manager|E-Mail"));
                project.setStatusName(getValue(headers, line, "Status Name"));
                project.setStatusDate(getValue(headers, line, "Status Date"));
                project.setTargetDate(getValue(headers, line, "Target Date"));
                project.setAppointmentsStatus(getValue(headers, line, "Appointments Status"));
                project.setCostsStatus(getValue(headers, line, "Costs Status"));
                project.setGoalsStatus(getValue(headers, line, "Goals Status"));
                project.setExplanation(getValue(headers, line, "Explanation"));
                project.setNextSteps(getValue(headers, line, "Next steps"));
                project.setPlanCost(getValue(headers, line, "Plan Cost"));
                project.setActualCost(getValue(headers, line, "Actual Cost"));
                project.setPlanDuration(getValue(headers, line, "Plan Duration"));
                project.setActualEffort(getValue(headers, line, "Actual Effort"));
                project.setForecastEffort(getValue(headers, line, "Forecast Effort"));
                project.setRecordedTime(getValue(headers, line, "Recorded Time"));
                project.setWorkflow(getValue(headers, line, "Workflow"));
                project.setTaskTemplate(getValue(headers, line, "Task template"));
                project.setCreatedOn(getValue(headers, line, "Created On"));
                project.setCreatedBy(getValue(headers, line, "Created By"));
                project.setUpdatedOn(getValue(headers, line, "Updated On"));
                project.setUpdatedBy(getValue(headers, line, "Updated By"));
                project.setLabels(getValue(headers, line, "Labels"));
                project.setLockedFlavors(getValue(headers, line, "LockedFlavors"));
    
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
