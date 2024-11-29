package com.VTool;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http:localhost:4200")
@RequestMapping("/smenso")
public class SmensoApiController {

    private final SmensoApiService smensoApiService;

    public SmensoApiController(SmensoApiService smensoApiService) {
        this.smensoApiService = smensoApiService;
    }

    @PostMapping(value = "/project", consumes = "application/xml", produces = "application/xml")
    public String createProject(@RequestBody String xmlPayload) {
        return smensoApiService.createProject(xmlPayload);
    }

    @GetMapping(value = "/projects/report", produces = MediaType.TEXT_PLAIN_VALUE)
    public String getProjectsReport(
            @RequestParam String viewId,
            @RequestParam String filter,
            @RequestParam String format) {
        return smensoApiService.getProjectsReport(viewId, filter, format);
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
