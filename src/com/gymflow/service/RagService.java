package com.gymflow.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

/**
 * Calls the local Python RAG backend (python api.py must be running).
 * Uses only Java 11 built-in HttpClient — zero external dependencies.
 */
public class RagService {

    private static final String API_URL    = "http://127.0.0.1:8000/chat";
    private static final String HEALTH_URL = "http://127.0.0.1:8000/health";

    // ── Response record ───────────────────────────────────────────────────────
    public static class RagResponse {
        public final String  answer;
        public final boolean inScope;
        public final double  bestSimilarity;

        public RagResponse(String answer, boolean inScope, double bestSimilarity) {
            this.answer         = answer;
            this.inScope        = inScope;
            this.bestSimilarity = bestSimilarity;
        }
    }

    // ── Fresh client per request (avoids poisoned connection pool) ────────────
    private HttpClient newClient() {
        return HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
    }

    // ── Ask ───────────────────────────────────────────────────────────────────
    public RagResponse ask(String query) throws Exception {
        if (query == null || query.isBlank()) {
            throw new IllegalArgumentException("Query must not be empty.");
        }
        if (query.length() > 490) {
            query = query.substring(0, 490);
        }

        String body      = "{\"query\": \"" + escapeJson(query) + "\"}";
        byte[] bodyBytes = body.getBytes(StandardCharsets.UTF_8);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json; charset=utf-8")
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofByteArray(bodyBytes))
                .timeout(Duration.ofSeconds(30))
                .build();

        HttpResponse<String> response = newClient().send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException(
                    "API error: HTTP " + response.statusCode() +
                            " — " + response.body()
            );
        }

        return parseResponse(response.body());
    }

    // ── Health check ──────────────────────────────────────────────────────────
    public boolean isServerRunning() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(HEALTH_URL))
                    .GET()
                    .timeout(Duration.ofSeconds(3))
                    .build();
            HttpResponse<String> response = newClient().send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 200;
        } catch (Exception e) {
            return false;
        }
    }

    // ── JSON parsing ─────────────────────────────────────────────────────────
    private RagResponse parseResponse(String json) {
        String  answer     = extractString(json, "answer");
        boolean inScope    = json.contains("\"in_scope\":true") || json.contains("\"in_scope\": true");
        double  similarity = extractDouble(json, "best_similarity");
        return new RagResponse(answer, inScope, similarity);
    }

    /**
     * Extracts a JSON string value by key.
     * Handles both "key": "value" and "key":"value" (with or without space).
     */
    private String extractString(String json, String key) {
        // Try with space first, then without
        String marker = "\"" + key + "\": \"";
        int start = json.indexOf(marker);
        if (start == -1) {
            marker = "\"" + key + "\":\"";
            start  = json.indexOf(marker);
        }
        if (start == -1) return "";
        start += marker.length();

        StringBuilder sb = new StringBuilder();
        for (int i = start; i < json.length(); i++) {
            char c = json.charAt(i);
            if (c == '\\' && i + 1 < json.length()) {
                char next = json.charAt(++i);
                switch (next) {
                    case '"'  -> sb.append('"');
                    case 'n'  -> sb.append('\n');
                    case 't'  -> sb.append('\t');
                    case 'r'  -> sb.append('\r');
                    case '\\' -> sb.append('\\');
                    default   -> { sb.append('\\'); sb.append(next); }
                }
                continue;
            }
            if (c == '"') break;
            sb.append(c);
        }
        return sb.toString();
    }

    private double extractDouble(String json, String key) {
        // Try with space first, then without
        String marker = "\"" + key + "\": ";
        int start = json.indexOf(marker);
        if (start == -1) {
            marker = "\"" + key + "\":";
            start  = json.indexOf(marker);
        }
        if (start == -1) return 0.0;
        start += marker.length();
        int end = start;
        while (end < json.length() && (Character.isDigit(json.charAt(end)) || json.charAt(end) == '.')) end++;
        try {
            return Double.parseDouble(json.substring(start, end));
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    // ── JSON escaping ─────────────────────────────────────────────────────────
    private String escapeJson(String s) {
        StringBuilder sb = new StringBuilder(s.length() + 16);
        for (char c : s.toCharArray()) {
            switch (c) {
                case '"'  -> sb.append("\\\"");
                case '\\' -> sb.append("\\\\");
                case '\n' -> sb.append("\\n");
                case '\r' -> sb.append("\\r");
                case '\t' -> sb.append("\\t");
                case '\b' -> sb.append("\\b");
                case '\f' -> sb.append("\\f");
                default   -> {
                    if (c < 0x20) {
                        sb.append(String.format("\\u%04x", (int) c));
                    } else {
                        sb.append(c);
                    }
                }
            }
        }
        return sb.toString();
    }
}