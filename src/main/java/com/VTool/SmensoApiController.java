package com.VTool;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SmensoApiController {

    private final SmensoApiService smensoApiService;

    public SmensoApiController(SmensoApiService smensoApiService) {
        this.smensoApiService = smensoApiService;
    }

    @PostMapping(value = "/smenso/project", consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
    public String createProject(@RequestBody ProjectRequest projectRequest) {
        return smensoApiService.createProject(projectRequest);
    }
}
