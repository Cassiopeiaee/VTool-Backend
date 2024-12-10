package com.VTool;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(
        origins = "http://localhost:4200",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS},
        allowedHeaders = {"Authorization", "Content-Type", "Accept"},
        allowCredentials = "true")


@RestController
@RequestMapping("/smenso")
public class SmensoApiController {

    private final SmensoApiService smensoApiService;
    private final ProjectReportService projectReportService;
    private final ProjectDataRepository projectDataRepository;

    public SmensoApiController(SmensoApiService smensoApiService, ProjectReportService projectReportService, ProjectDataRepository projectDataRepository) {
        this.smensoApiService = smensoApiService;
        this.projectReportService = projectReportService;
        this.projectDataRepository = projectDataRepository;
    }


    @GetMapping("/report/{guid}")
    public ResponseEntity<String> fetchAndSaveProject(@PathVariable String guid) {
        System.out.println("Erhaltene View-ID: " + guid);
    
        try {
            // Abrufen des CSV-Datensatzes
            String csvData = smensoApiService.fetchProjectReport(guid, "active", "CSV");
            System.out.println("Erhaltene CSV-Daten:\n" + csvData);
    
            // Konvertierung von CSV zu Projektdaten
            List<ProjectData> projects = smensoApiService.parseCsvToProjectData(csvData);
    
            // Alle vorherigen Projekte löschen
            projectDataRepository.deleteAll();
    
            // Nur das aktuelle Projekt speichern und zurückgeben
            if (!projects.isEmpty()) {
                ProjectData currentProject = projects.get(0); // Nehmen wir das erste Projekt
                projectDataRepository.save(currentProject);
    
                // Erstelle die CSV-Antwort mit zusätzlichen Feldern
                String csvResponse = String.format(
                    "Id,Title,Status,Progress,Cost Status,Start date,End date,Overall Status,Budget\n" +
                    "%s,%s,%s,%d,%s,%s,%s,%s,%s\n",
                    currentProject.getId(),
                    currentProject.getTitle(),
                    currentProject.getStatus(),
                    currentProject.getProgress(),
                    currentProject.getCostStatus(),
                    currentProject.getStartDate(),
                    currentProject.getEndDate(),
                    currentProject.getOverallStatus() != null ? currentProject.getOverallStatus() : "",
                    currentProject.getBudget() != null ? currentProject.getBudget() : "0"
                );
    
                return ResponseEntity.ok(csvResponse);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                     .body("Kein Projekt gefunden.");
            }
        } catch (Exception e) {
            System.err.println("Fehler beim Abrufen oder Speichern des Projekts: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Fehler beim Speichern des Projekts: " + e.getMessage());
        }
    }
    
    
    
    
    
        @GetMapping("/download-project/{id}")
    public ResponseEntity<byte[]> downloadProjectAsExcel(@PathVariable String id) {
        ProjectData project = projectDataRepository.findById(id).orElse(null);

        if (project == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body(("Kein Projekt mit der ID: " + id + " gefunden.").getBytes());
        }

        try {
            byte[] excelBytes = smensoApiService.generateExcelForProject(project, id);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentDispositionFormData("attachment", "project_" + id + ".xlsx");
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(excelBytes);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(("Fehler beim Erstellen der Excel-Datei: " + e.getMessage()).getBytes());
        }
    }


    @PostMapping(value = "/create-project-from-template/{templateId}", consumes = "application/xml", produces = "application/xml")
    public ResponseEntity<String> createProjectFromTemplate(@PathVariable String templateId, @RequestBody String xmlPayload) {
        try {
            String response = smensoApiService.createProjectFromTemplate(templateId, xmlPayload);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("<error>Fehler beim Erstellen des Projekts: " + e.getMessage() + "</error>");
        }
    }
    

    
    

    @PostMapping(value = "/project", consumes = "application/xml", produces = "application/xml")
    public String createProject(@RequestBody String xmlPayload) {
        return smensoApiService.createProject(xmlPayload);
    }


    @GetMapping(value = "/projects/report", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getProjectsReport(
            @RequestParam String viewId,
            @RequestParam String filter,
            @RequestParam String format) {
        String csvData = smensoApiService.getProjectsReport(viewId, filter, format);
    
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN); 
        headers.set("X-Content-Type-Options", "nosniff"); 
    
        return ResponseEntity.ok()
                .headers(headers)
                .body(csvData);
    }
    

    @RequestMapping(value = "/**")
    public String forward() {
        // Weiterleitung zu Angulars index.html
        return "forward:/index.html";
    }

    private static final Logger logger = LoggerFactory.getLogger(SmensoApiController.class);
    @PostMapping("/save-project")
    public ResponseEntity<Map<String, String>> saveProjects(@RequestBody List<ProjectData> projects) {
        try {
            logger.info("Empfangene Projektdaten: {}", projects);
            smensoApiService.saveProjectData(projects);
            return ResponseEntity.ok(Map.of("message", "Projekte erfolgreich gespeichert."));
        } catch (Exception e) {
            logger.error("Fehler beim Speichern der Projekte", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(Map.of("error", "Fehler beim Speichern: " + e.getMessage()));
        }
    }




    @GetMapping("/api/saved-projects")
    public List<ProjectData> getSavedProjects() {
        return projectDataRepository.findAll(); // Alle gespeicherten Projekte abrufen
    }
}
