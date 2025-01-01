package com.VTool;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

class SmensoApiServiceTest {

    private SmensoApiService smensoApiService;
    private RestTemplate restTemplateMock;
    private ProjectDataRepository projectDataRepositoryMock;

    @BeforeEach
    void setUp() {
        restTemplateMock = mock(RestTemplate.class);
        projectDataRepositoryMock = mock(ProjectDataRepository.class);
        smensoApiService = new SmensoApiService(restTemplateMock, projectDataRepositoryMock);
    }

    @Test
    void testFetchProjectReport_success() {
        // given
        String guid = "test-guid";
        String filter = "active";
        String format = "CSV";
        String expectedCsv = "Id,Title,Status\n1,Test Project,Open";

        // Wir simulieren, dass der Rest-Call OK (200) ist und byte[] zurückgibt
        byte[] expectedBytes = expectedCsv.getBytes(StandardCharsets.UTF_8);
        ResponseEntity<byte[]> mockResponse = new ResponseEntity<>(expectedBytes, HttpStatus.OK);

        when(restTemplateMock.exchange(
                anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(byte[].class))
        ).thenReturn(mockResponse);

        // when
        String actualCsv = smensoApiService.fetchProjectReport(guid, filter, format);

        // then
        assertEquals(expectedCsv, actualCsv);
        verify(restTemplateMock, times(1))
                .exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(byte[].class));
    }

    @Test
    void testFetchProjectReport_apiError() {
        // given
        when(restTemplateMock.exchange(
                anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(byte[].class))
        ).thenThrow(new RuntimeException("API Error"));

        // when & then
        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> smensoApiService.fetchProjectReport("guid", "filter", "format")
        );

        assertTrue(ex.getMessage().contains("Fehler beim Abrufen"));
    }

    @Test
    void testGenerateExcelForProject_success() throws IOException {
        // given
        ProjectData projectData = new ProjectData();
        projectData.setId("123");
        projectData.setTitle("Test Project");
        // Wichtig: Progress setzen, um NullPointerException zu vermeiden
        projectData.setProgress(50);

        // when
        byte[] excelData = smensoApiService.generateExcelForProject(projectData, "123");

        // then
        assertNotNull(excelData, "Excel byte array should not be null");

        // Optional: Prüfen, ob es sich um ein valides Excel-Format handelt
        try (XSSFWorkbook workbook = new XSSFWorkbook(new ByteArrayInputStream(excelData))) {
            assertEquals("Project Data", workbook.getSheetName(0));
        }
    }

    @Test
    void testCreateProjectFromTemplate_success() {
        // given
        String templateId = "test-template";
        String xmlPayload = "<project><title>Test</title></project>";
        String expectedResponse = "<response>Project Created</response>";

        ResponseEntity<String> mockResponse = new ResponseEntity<>(expectedResponse, HttpStatus.OK);

        when(restTemplateMock.postForEntity(anyString(), any(HttpEntity.class), eq(String.class)))
                .thenReturn(mockResponse);

        // when
        String actualResponse = smensoApiService.createProjectFromTemplate(templateId, xmlPayload);

        // then
        assertEquals(expectedResponse, actualResponse);
        verify(restTemplateMock, times(1))
                .postForEntity(anyString(), any(HttpEntity.class), eq(String.class));
    }

    @Test
    void testGetProjectsReport_success() {
        // given
        String viewId = "test-view";
        String filter = "test-filter";
        String format = "CSV";
        String expectedCsv = "Id,Title,Status\n1,Test Project,Open";
        ResponseEntity<String> mockResponse = new ResponseEntity<>(expectedCsv, HttpStatus.OK);

        when(restTemplateMock.exchange(
                anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class))
        ).thenReturn(mockResponse);

        // when
        String actualCsv = smensoApiService.getProjectsReport(viewId, filter, format);

        // then
        assertEquals(expectedCsv, actualCsv);
        verify(restTemplateMock, times(1))
                .exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class));
    }

    @Test
    void testSaveProjectData_success() {
        // given
        ProjectData p1 = new ProjectData();
        p1.setId("1");
        p1.setTitle("Project 1");

        ProjectData p2 = new ProjectData();
        p2.setId("2");
        p2.setTitle("Project 2");

        List<ProjectData> projectDataList = List.of(p1, p2);

        // when
        smensoApiService.saveProjectData(projectDataList);

        // then
        verify(projectDataRepositoryMock, times(1)).saveAll(projectDataList);
    }

    @Test
    void testParseCsvToProjectData_success() {
        // given
        String csvData = "Id,Title,Status,Progress\n" +
                "1,Test Project,Open,50\n" +
                "2,Another Project,InProgress,75";

        // when
        List<ProjectData> result = smensoApiService.parseCsvToProjectData(csvData);

        // then
        assertEquals(2, result.size());
        assertEquals("1", result.get(0).getId());
        assertEquals("Test Project", result.get(0).getTitle());
        assertEquals("Open", result.get(0).getStatus());
        assertEquals(50, result.get(0).getProgress());

        assertEquals("2", result.get(1).getId());
        assertEquals("Another Project", result.get(1).getTitle());
        assertEquals("InProgress", result.get(1).getStatus());
        assertEquals(75, result.get(1).getProgress());
    }

    @Test
    void testParseCsvToProjectData_noHeaders() {
        // given
        String csvData = "";

        // when & then
        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> smensoApiService.parseCsvToProjectData(csvData)
        );

        // Debug:
        System.out.println("Outer exception message: " + ex.getMessage());
        if (ex.getCause() != null) {
            System.out.println("Cause message: " + ex.getCause().getMessage());
        }

        assertNotNull(ex.getCause(), "Es wird eine Cause erwartet, da wir intern bereits eine Exception geworfen haben.");

        assertTrue(ex.getCause().getMessage().contains("keine Header-Zeile"),
                "Fehlermeldung enthält nicht das erwartete Fragment für fehlende Header in der Ursache.");
    }


}
