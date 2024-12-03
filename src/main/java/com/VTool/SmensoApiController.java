package com.VTool;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@CrossOrigin(origins = "http://localhost:4200") // Fehlerhafter Pfad "http:localhost" korrigiert
@RequestMapping("/smenso")
public class SmensoApiController {

    private final SmensoApiService smensoApiService;
    private final ProjectReportService projectReportService;

    public SmensoApiController(SmensoApiService smensoApiService, ProjectReportService projectReportService) {
        this.smensoApiService = smensoApiService;
        this.projectReportService = projectReportService;
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
        headers.setContentType(MediaType.TEXT_PLAIN); // Content-Type auf text/plain setzen
        headers.set("X-Content-Type-Options", "nosniff"); // Sicherheitseinstellung
    
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

    @GetMapping("/projects/fetch-and-convert")
    public ResponseEntity<String> fetchAndConvertCsvToXml() {
        try {
            String xmlData = smensoApiService.fetchAndConvertCsvToXml();
            return ResponseEntity.ok(xmlData);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Fehler: " + e.getMessage());
        }
    }

    @RequestMapping(value = "/**")
    public String forward() {
        // Weiterleitung zu Angulars index.html
        return "forward:/index.html";
    }
}
