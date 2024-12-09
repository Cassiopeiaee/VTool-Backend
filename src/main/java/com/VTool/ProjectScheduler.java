package com.VTool;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.StringReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectScheduler {

    private final ProjectDataRepository projectDataRepository;

    public ProjectScheduler(ProjectDataRepository projectDataRepository) {
        this.projectDataRepository = projectDataRepository;
    }

    @Scheduled(cron = "0 0 1 * * ?") // Jeden Tag um 01:00 Uhr
    public void fetchAndSaveProjects() {
        String csvUrl = "https://bgn-it.smenso.cloud/skyisland/api/Reports/projects?view=e813c779-f5ed-4fce-91ca-1ec9f67b0262&filter=active&format=CSV";

        try {
            // CSV-Daten abrufen
            RestTemplate restTemplate = new RestTemplate();
            String csvData = restTemplate.getForObject(csvUrl, String.class);

            if (csvData != null) {
                // CSV-Daten parsen
                List<ProjectData> projects = parseCsvData(csvData);

                // Projekte in der Datenbank speichern
                saveProjectsToDatabase(projects);
            } else {
                System.err.println("Keine Daten von der API erhalten.");
            }
        } catch (Exception e) {
            System.err.println("Fehler beim Abrufen oder Speichern der Projekte: " + e.getMessage());
        }
    }

    // Methode zum Speichern von Projekten in der Datenbank
    private void saveProjectsToDatabase(List<ProjectData> projects) {
        try {
            projectDataRepository.saveAll(projects);
            System.out.println("Projekte erfolgreich gespeichert.");
        } catch (Exception e) {
            System.err.println("Fehler beim Speichern der Projekte: " + e.getMessage());
        }
    }

    // Methode zum Parsen der CSV-Daten
    private List<ProjectData> parseCsvData(String csvData) {
        List<ProjectData> projects = new ArrayList<>();
    
        try (BufferedReader reader = new BufferedReader(new StringReader(csvData))) {
            String headerLine = reader.readLine(); // Kopfzeile ignorieren
            String line;
    
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
    
                if (values.length < 8) continue; // Ungültige Zeilen überspringen
    
                ProjectData project = new ProjectData(); // Verwendung der korrekten Entitätsklasse
                project.setId(values[0].trim());
                project.setTitle(values[1].trim());
                project.setStatus(values[3].trim()); // Status anpassen, da "Type" entfernt wurde
                try {
                    project.setProgress(Integer.parseInt(values[4].trim()));
                } catch (NumberFormatException e) {
                    project.setProgress(0); // Fallback auf 0 bei ungültigen Werten
                }
                project.setCostStatus(values[5].trim());
                project.setStartDate(values[6].trim()); // Startdatum
                project.setEndDate(values[7].trim());   // Enddatum
    
                projects.add(project);
            }
        } catch (Exception e) {
            System.err.println("Fehler beim Parsen der CSV-Daten: " + e.getMessage());
        }
    
        return projects;
    }
    

    // Hilfsmethode zum Parsen von Datum
    private LocalDate parseDate(String date) {
        try {
            return LocalDate.parse(date);
        } catch (Exception e) {
            System.err.println("Ungültiges Datum: " + date);
            return null;
        }
    }
}
