package com.VTool;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;


@Service
public class ProjectReportService {

    public List<Map<String, String>> convertCsvToJson(String csvData) {
        try {
            List<Map<String, String>> jsonData = new ArrayList<>();
            String[] rows = csvData.split("\n");
            String[] headers = rows[0].split(",");

            for (int i = 1; i < rows.length; i++) {
                String[] values = rows[i].split(",");
                Map<String, String> rowMap = new HashMap<>();
                for (int j = 0; j < headers.length; j++) {
                    rowMap.put(headers[j].trim(), values[j].trim());
                }
                jsonData.add(rowMap);
            }
            return jsonData;
        } catch (Exception e) {
            throw new RuntimeException("Fehler beim Konvertieren der CSV-Daten", e);
        }
    }



        public List<Map<String, String>> getProjectsReportAsJson(String csvData) {
        try {
            String[] lines = csvData.split("\n");
            if (lines.length < 2) {
                throw new RuntimeException("CSV-Daten enthalten keine Datenzeilen");
            }

            // Headers aus der ersten Zeile extrahieren
            String[] headers = lines[0].split(",");

            // Datenzeilen parsen
            return Arrays.stream(lines, 1, lines.length) // Überspringe die Headerzeile
                    .map(line -> parseCsvRow(headers, line))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new RuntimeException("Fehler beim Verarbeiten der CSV-Daten", e);
        }
    }

    private Map<String, String> parseCsvRow(String[] headers, String row) {
        String[] values = row.split(",");
        Map<String, String> parsedRow = new HashMap<>();
        for (int i = 0; i < headers.length; i++) {
            String header = headers[i].trim();
            String value = i < values.length ? values[i].trim() : "";
            parsedRow.put(header, value);
        }
        return parsedRow;
    }
}


