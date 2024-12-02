package com.VTool;

import java.util.List;
import java.util.Map;
import java.util.*;
import java.io.*;
import org.springframework.stereotype.Service;


@Service
public class ProjectReportService {

    public List<Map<String, String>> getProjectsReportAsJson(String csvData) {
        // Initialisiere die Rückgabeliste
        List<Map<String, String>> jsonData = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new StringReader(csvData))) {
            // Erste Zeile als Header lesen
            String[] headers = reader.readLine().split(",");

            if (headers == null || headers.length == 0) {
                throw new RuntimeException("CSV-Daten enthalten keine Header.");
            }

            // Verarbeite jede nachfolgende Zeile
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                Map<String, String> row = new HashMap<>();

                // Header mit Werten mappen
                for (int i = 0; i < headers.length && i < values.length; i++) {
                    row.put(headers[i].trim(), values[i].trim());
                }

                jsonData.add(row);
            }
        } catch (Exception e) {
            throw new RuntimeException("Fehler beim Verarbeiten der CSV-Daten: " + e.getMessage(), e);
        }

        // Rückgabe der verarbeiteten Daten
        return jsonData;
    }
}

