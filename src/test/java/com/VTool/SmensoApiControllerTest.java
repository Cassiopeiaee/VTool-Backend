package com.VTool;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.Mockito;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SmensoApiController.class)
class SmensoApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SmensoApiService smensoApiService;

    @MockBean
    private ProjectReportService projectReportService;

    @MockBean
    private ProjectDataRepository projectDataRepository;

    @BeforeEach
    void setUp() {
        Mockito.reset(smensoApiService, projectReportService, projectDataRepository);
    }

    @Test
    void testFetchAndSaveProject_success() throws Exception {
        String guid = "test-guid";
        String dummyCsv = "Id,Title,Status,Progress\n1,TestProject,Open,50";

        when(smensoApiService.fetchProjectReport(eq(guid), eq("active"), eq("CSV")))
                .thenReturn(dummyCsv);

        // Simuliere, wie parseCsvToProjectData(...) wirklich setzt:
        ProjectData projectData = new ProjectData();
        projectData.setId("1");
        projectData.setTitle("TestProject");
        projectData.setStatus("Open");
        projectData.setProgress(50);
        // Felder, die der Controller als "Nicht verfügbar" oder "" haben will:
        projectData.setCostStatus("");                // => ""
        projectData.setStartDate("Nicht verfügbar");  // => "Nicht verfügbar"
        projectData.setEndDate("Nicht verfügbar");    // => "Nicht verfügbar"
        projectData.setOverallStatus("");             // => ""
        projectData.setBudget("0");                   // => "0"

        when(smensoApiService.parseCsvToProjectData(dummyCsv))
                .thenReturn(List.of(projectData));

        doNothing().when(projectDataRepository).deleteAll();
        when(projectDataRepository.save(any(ProjectData.class))).thenReturn(projectData);

        mockMvc.perform(get("/smenso/report/{guid}", guid))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        "Id,Title,Status,Progress,Cost Status,Start date,End date,Overall Status,Budget\n" +
                                "1,TestProject,Open,50,,Nicht verfügbar,Nicht verfügbar,,0\n"
                ));
    }


    @Test
    void testFetchAndSaveProject_notFound() throws Exception {
        String guid = "test-guid";
        String dummyCsv = "";

        when(smensoApiService.fetchProjectReport(eq(guid), eq("active"), eq("CSV")))
                .thenReturn(dummyCsv);

        // Keine Projekte => parseCsvToProjectData gibt eine leere Liste
        when(smensoApiService.parseCsvToProjectData(dummyCsv)).thenReturn(List.of());

        mockMvc.perform(get("/smenso/report/{guid}", guid))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Kein Projekt gefunden."));
    }

    @Test
    void testFetchAndSaveProject_exception() throws Exception {
        String guid = "test-guid";

        when(smensoApiService.fetchProjectReport(eq(guid), any(), any()))
                .thenThrow(new RuntimeException("Fehler!"));

        mockMvc.perform(get("/smenso/report/{guid}", guid))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Fehler beim Speichern des Projekts: Fehler!"));
    }


    @Test
    void testDownloadProjectAsExcel_success() throws Exception {
        String id = "test-id";
        ProjectData project = new ProjectData();
        project.setId(id);

        when(projectDataRepository.findById(id)).thenReturn(Optional.of(project));

        byte[] excelBytes = new byte[] {1, 2, 3};
        when(smensoApiService.generateExcelForProject(eq(project), eq(id))).thenReturn(excelBytes);

        mockMvc.perform(get("/smenso/download-project/{id}", id))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "form-data; name=\"attachment\"; filename=\"project_test-id.xlsx\""))
                .andExpect(content().bytes(excelBytes));
    }


    @Test
    void testDownloadProjectAsExcel_notFound() throws Exception {
        String id = "unknown-id";
        when(projectDataRepository.findById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/smenso/download-project/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Kein Projekt mit der ID: unknown-id gefunden."));
    }

    @Test
    void testDownloadProjectAsExcel_exception() throws Exception {
        String id = "test-id";
        ProjectData project = new ProjectData();
        project.setId(id);

        when(projectDataRepository.findById(id)).thenReturn(Optional.of(project));
        when(smensoApiService.generateExcelForProject(eq(project), eq(id)))
                .thenThrow(new RuntimeException("Excel-Fehler"));

        mockMvc.perform(get("/smenso/download-project/{id}", id))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Fehler beim Erstellen der Excel-Datei: Excel-Fehler"));
    }

    // ------------------------
    // 3) POST /smenso/create-project-from-template/{templateId}
    // ------------------------
    @Test
    void testCreateProjectFromTemplate_success() throws Exception {
        String templateId = "template-123";
        String xmlPayload = "<xml>...</xml>";

        when(smensoApiService.createProjectFromTemplate(eq(templateId), eq(xmlPayload)))
                .thenReturn("<response>Created</response>");

        mockMvc.perform(post("/smenso/create-project-from-template/{templateId}", templateId)
                        .contentType(MediaType.APPLICATION_XML)
                        .content(xmlPayload))
                .andExpect(status().isOk())
                .andExpect(content().string("<response>Created</response>"));
    }

    @Test
    void testCreateProjectFromTemplate_exception() throws Exception {
        String templateId = "template-123";
        String xmlPayload = "<xml>...</xml>";

        when(smensoApiService.createProjectFromTemplate(eq(templateId), eq(xmlPayload)))
                .thenThrow(new RuntimeException("Fehler!"));

        mockMvc.perform(post("/smenso/create-project-from-template/{templateId}", templateId)
                        .contentType(MediaType.APPLICATION_XML)
                        .content(xmlPayload))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("<error>Fehler beim Erstellen des Projekts: Fehler!</error>"));
    }

    // ------------------------
    // 4) GET /smenso/projects/report
    // ------------------------
    @Test
    void testGetProjectsReport_success() throws Exception {
        String viewId = "some-view";
        String filter = "some-filter";
        String format = "CSV";

        String csvData = "Id,Title\n1,Project 1";
        when(smensoApiService.getProjectsReport(eq(viewId), eq(filter), eq(format)))
                .thenReturn(csvData);

        mockMvc.perform(get("/smenso/projects/report")
                        .param("viewId", viewId)
                        .param("filter", filter)
                        .param("format", format))
                .andExpect(status().isOk())
                .andExpect(content().string(csvData))
                .andExpect(header().string("X-Content-Type-Options", "nosniff"));
    }

    // ------------------------
    // 5) RequestMapping(value = "/**") => forward()
    // ------------------------
    @Test
    void testForward() throws Exception {
        // Da du @RestController benutzt, wird "forward:/index.html"
        // als reiner String im Body zurückgegeben,
        // kein echter Forward. Deshalb prüfen wir auf den Body:
        mockMvc.perform(get("/smenso/something-else"))
                .andExpect(status().isOk())
                .andExpect(content().string("forward:/index.html"));
    }

    // ------------------------
    // 6) POST /smenso/save-project
    // ------------------------
    @Test
    void testSaveProjects_success() throws Exception {
        ProjectData p1 = new ProjectData();
        p1.setId("1");
        p1.setTitle("Title 1");

        ProjectData p2 = new ProjectData();
        p2.setId("2");
        p2.setTitle("Title 2");

        String json = """
            [
                {"id":"1","title":"Title 1"},
                {"id":"2","title":"Title 2"}
            ]
            """;

        // smensoApiService.saveProjectData(...) wirft keine Exception => doNothing
        doNothing().when(smensoApiService).saveProjectData(anyList());

        mockMvc.perform(post("/smenso/save-project")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Projekte erfolgreich gespeichert."));
    }

    @Test
    void testSaveProjects_exception() throws Exception {
        String json = """
            [
                {"id":"1","title":"Title 1"}
            ]
            """;

        doThrow(new RuntimeException("DB-Fail")).when(smensoApiService).saveProjectData(anyList());

        mockMvc.perform(post("/smenso/save-project")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Fehler beim Speichern: DB-Fail"));
    }

}
