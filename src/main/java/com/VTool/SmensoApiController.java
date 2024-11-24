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

    @PostMapping(value = "/project", consumes = MediaType.APPLICATION_XML_VALUE)
    public String createOrUpdateProject(@RequestBody String xmlPayload) {
        return smensoApiService.createOrUpdateProject(xmlPayload);
    }
}
