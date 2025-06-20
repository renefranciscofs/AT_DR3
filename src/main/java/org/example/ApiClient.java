package org.example;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ApiClient {

    private static final String BASE_URL = "http://localhost:7000";

    public static void main(String[] args) throws IOException {
        criarUsuario();
        listarUsuarios();
        buscarUsuario("rene@example.com");
        consultarStatus();
    }

    public static void criarUsuario() throws IOException {
        String endpoint = BASE_URL + "/usuarios";
        String json = """
                {
                    "nome": "Rene",
                    "email": "rene@example.com",
                    "idade": 24
                }
                """;

        HttpURLConnection conn = (HttpURLConnection) new URL(endpoint).openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = json.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int status = conn.getResponseCode();
        String response = readResponse(conn);

        System.out.println("POST /usuarios");
        System.out.println("Status: " + status);
        System.out.println("Resposta: " + response);
        System.out.println("-----");
    }

    public static void listarUsuarios() throws IOException {
        String endpoint = BASE_URL + "/usuarios";

        HttpURLConnection conn = (HttpURLConnection) new URL(endpoint).openConnection();
        conn.setRequestMethod("GET");

        int status = conn.getResponseCode();
        String response = readResponse(conn);

        System.out.println("GET /usuarios");
        System.out.println("Status: " + status);
        System.out.println("Resposta: " + response);
        System.out.println("-----");
    }

    public static void buscarUsuario(String email) throws IOException {
        String endpoint = BASE_URL + "/usuarios/" + email;

        HttpURLConnection conn = (HttpURLConnection) new URL(endpoint).openConnection();
        conn.setRequestMethod("GET");

        int status = conn.getResponseCode();
        String response = readResponse(conn);

        System.out.println("GET /usuarios/" + email);
        System.out.println("Status: " + status);
        System.out.println("Resposta: " + response);
        System.out.println("-----");
    }

    public static void consultarStatus() throws IOException {
        String endpoint = BASE_URL + "/status";

        HttpURLConnection conn = (HttpURLConnection) new URL(endpoint).openConnection();
        conn.setRequestMethod("GET");

        int status = conn.getResponseCode();
        String response = readResponse(conn);

        System.out.println("GET /status");
        System.out.println("Status: " + status);
        System.out.println("Resposta: " + response);
        System.out.println("-----");
    }

    private static String readResponse(HttpURLConnection conn) throws IOException {
        InputStream is = (conn.getResponseCode() >= 400) ? conn.getErrorStream() : conn.getInputStream();
        if (is == null) return "";
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line).append("\n");
            }
            return response.toString().trim();
        }
    }
}
