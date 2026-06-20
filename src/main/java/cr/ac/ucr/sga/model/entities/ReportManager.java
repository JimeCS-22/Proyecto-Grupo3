package cr.ac.ucr.sga.model.entities;

import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;

public class ReportManager {

    private List<Map<String, Object>> rows; // datos crudos
    private List<Map<String, Object>> filteredRows; // datos filtrados
    private Map<String, Object> metrics; // métricas calculadas

    public ReportManager() {
        this.rows = new ArrayList<>();
        this.filteredRows = new ArrayList<>();
        this.metrics = new HashMap<>();
    }

    // =========================
    // 1. LOAD METRICS
    // =========================
    public void loadMetrics() {
        metrics.clear();

        int total = rows.size();
        long active = rows.stream()
                .filter(r -> "ACTIVE".equals(r.get("status")))
                .count();

        double avgValue = rows.stream()
                .filter(r -> r.get("value") != null)
                .mapToDouble(r -> Double.parseDouble(r.get("value").toString()))
                .average()
                .orElse(0.0);

        metrics.put("total", total);
        metrics.put("active", active);
        metrics.put("avgValue", avgValue);
        metrics.put("generatedAt", LocalDate.now());
    }

    // =========================
    // 2. LOAD ROWS
    // =========================
    public void loadRows(List<Map<String, Object>> input) {
        this.rows.clear();
        this.rows.addAll(input);

        // por defecto también carga filtrados igual
        this.filteredRows = new ArrayList<>(rows);
    }

    // =========================
    // 3. FILTER ROWS
    // =========================
    public void filterRows(String key, Object value) {
        if (key == null || value == null) {
            filteredRows = new ArrayList<>(rows);
            return;
        }

        filteredRows = rows.stream()
                .filter(r -> value.equals(r.get(key)))
                .toList();
    }

    // =========================
    // 4. GENERATE CSV
    // =========================
    public void generateCSV(String path) throws IOException {

        if (filteredRows.isEmpty()) return;

        StringBuilder sb = new StringBuilder();

        // headers
        Set<String> headers = filteredRows.get(0).keySet();
        sb.append(String.join(",", headers)).append("\n");

        // data
        for (Map<String, Object> row : filteredRows) {
            for (String h : headers) {
                sb.append(row.getOrDefault(h, "")).append(",");
            }
            sb.setLength(sb.length() - 1);
            sb.append("\n");
        }

        Files.write(Paths.get(path), sb.toString().getBytes());
    }

    // =========================
    // 5. GENERATE EXCEL (simple CSV-based Excel)
    // =========================
    public void generateExcel(String path) throws IOException {
        // versión real usaría Apache POI
        generateCSV(path); // reutilizamos lógica base
    }

    // =========================
    // 6. GENERATE PDF (placeholder limpio)
    // =========================
    public void generatePDF(String path) throws IOException {

        StringBuilder pdfText = new StringBuilder();

        pdfText.append("REPORT\n\n");

        for (Map<String, Object> row : filteredRows) {
            pdfText.append(row.toString()).append("\n");
        }

        pdfText.append("\nMETRICS:\n");
        pdfText.append(metrics.toString());

        Files.write(Paths.get(path), pdfText.toString().getBytes());
    }

    // =========================
    // GETTERS
    // =========================
    public List<Map<String, Object>> getFilteredRows() {
        return filteredRows;
    }

    public Map<String, Object> getMetrics() {
        return metrics;
    }
}