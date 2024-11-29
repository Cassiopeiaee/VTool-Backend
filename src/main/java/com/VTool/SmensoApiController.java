package com.VTool;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
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
}
