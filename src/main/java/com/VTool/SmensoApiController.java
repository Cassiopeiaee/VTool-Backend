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

    @GetMapping("/project/{id}")
    public String getProjectById(@PathVariable String id) {
        return smensoApiService.getProjectById(id);
    }
}
