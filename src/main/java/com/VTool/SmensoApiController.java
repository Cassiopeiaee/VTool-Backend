package com.VTool;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
    
                String csvResponse = String.format("Id,Title,Status,Progress,Cost Status,Start date,End date\n%s,%s,%s,%d,%s,%s,%s\n",
                    currentProject.getId(),
                    currentProject.getTitle(),
                    currentProject.getStatus(),
                    currentProject.getProgress(),
                    currentProject.getCostStatus(),
                    currentProject.getStartDate(),
                    currentProject.getEndDate()
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
    

    @GetMapping(value = "/projects/report/json", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Map<String, String>> getProjectsReportAsJson(
            @RequestParam String viewId,
            @RequestParam String filter,
            @RequestParam String format) {
        try {
            // CSV-Daten über die Smenso API abrufen
            String csvData = smensoApiService.getProjectsReport(viewId, filter, format);

            // CSV-Daten in JSON umwandeln
            return projectReportService.getProjectsReportAsJson(csvData);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Fehler beim Abrufen und Verarbeiten der Projektdaten",
                    e
            );
        }
    }

    @GetMapping(value = "/projects/report/json/raw", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getProjectsReportAsRawCsv(
            @RequestParam String viewId,
            @RequestParam String filter,
            @RequestParam String format) {
        try {
            // CSV-Daten abrufen
            String csvData = smensoApiService.getProjectsReport(viewId, filter, format);
            return ResponseEntity.ok(csvData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Fehler beim Abrufen der CSV-Daten: " + e.getMessage());
        }
    }


    @RequestMapping(value = "/**")
    public String forward() {
        // Weiterleitung zu Angulars index.html
        return "forward:/index.html";
    }



    @PostMapping("/save-project")
public ResponseEntity<String> saveProject(@RequestBody ProjectData project) {
    try {
        projectDataRepository.save(project); // Speichere das Projekt in der Datenbank
        return ResponseEntity.ok("Projekt erfolgreich gespeichert.");
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body("Fehler beim Speichern des Projekts: " + e.getMessage());
    }
}



    @GetMapping("/api/saved-projects")
    public List<ProjectData> getSavedProjects() {
        return projectDataRepository.findAll(); // Alle gespeicherten Projekte abrufen
    }
}
