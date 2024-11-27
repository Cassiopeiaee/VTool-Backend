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

    @PostMapping(value = "/project/template/{templateId}", consumes = MediaType.APPLICATION_XML_VALUE)
    public String createProjectWithTemplate(@PathVariable String templateId, @RequestBody String xmlPayload) {
        return smensoApiService.createProjectWithTemplate(templateId, xmlPayload);
    }

    @GetMapping("/project/{id}")
    public String getProjectById(@PathVariable String id) {
        return smensoApiService.getProjectById(id);
    }
}
