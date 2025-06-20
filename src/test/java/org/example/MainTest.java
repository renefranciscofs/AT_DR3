package org.example;

import io.javalin.Javalin;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MainTest {

    private static Javalin app;
    private static int port;

    @BeforeAll
    public static void startServer() {
        app = Main.startApp();
        app.start(0);
        port = app.port();
    }

    @AfterAll
    public static void stopServer() {
        app.stop();
    }

    private String url(String path) {
        return "http://localhost:" + port + path;
    }

    @Test
    @Order(1)
    public void testHelloEndpoint() {
        HttpResponse<String> response = Unirest.get(url("/hello")).asString();
        assertEquals(200, response.getStatus());
        assertEquals("Hello, Javalin!", response.getBody());
    }

    @Test
    @Order(2)
    public void testCriarUsuario() {
        String json = """
                {
                  "nome": "Rene",
                  "email": "rene@example.com",
                  "idade": 24
                }
                """;

        HttpResponse<JsonNode> response = Unirest.post(url("/usuarios"))
            .header("Content-Type", "application/json")
            .body(json)
            .asJson();

        assertEquals(201, response.getStatus());
        assertEquals("rene@example.com", response.getBody().getObject().getString("email"));
    }

    @Test
    @Order(3)
    public void testBuscarUsuarioPorEmail() {
        String json = """
            {
              "nome": "Rene",
              "email": "rene@example.com",
              "idade": 24
            }
            """;
        Unirest.post(url("/usuarios"))
            .header("Content-Type", "application/json")
            .body(json)
            .asJson();

        HttpResponse<JsonNode> response = Unirest.get(url("/usuarios/rene@example.com")).asJson();
        assertEquals(200, response.getStatus());
        assertEquals("Rene", response.getBody().getObject().getString("nome"));
    }

    @Test
    @Order(4)
    public void testListarUsuarios() {
        String json = """
            {
              "nome": "Listagem",
              "email": "listar@example.com",
              "idade": 30
            }
            """;
        Unirest.post(url("/usuarios"))
            .header("Content-Type", "application/json")
            .body(json)
            .asJson();

        HttpResponse<JsonNode> response = Unirest.get(url("/usuarios")).asJson();
        assertEquals(200, response.getStatus());
        assertTrue(response.getBody().getArray().length() > 0);
    }
}
