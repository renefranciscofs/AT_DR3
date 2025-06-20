package org.example;

import io.javalin.Javalin;
import io.javalin.http.HttpStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {

    private static final List<Usuario> usuarios = new ArrayList<>();

    public static void main(String[] args) {
        startApp().start(7000);
    }

    public static Javalin startApp() {
        Javalin app = Javalin.create();

        app.get("/hello", ctx -> ctx.result("Hello, Javalin!"));

        app.get("/status", ctx -> {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode status = mapper.createObjectNode();
            status.put("status", "ok");
            status.put("timestamp", Instant.now().toString());
            ctx.json(status);
        });

        app.post("/echo", ctx -> {
            Map<String, Object> body = ctx.bodyAsClass(Map.class);
            ctx.json(body);
        });

        app.get("/saudacao/{nome}", ctx -> {
            String nome = ctx.pathParam("nome");
            ctx.json(Map.of("mensagem", "Olá, " + nome + "!"));
        });

        app.post("/usuarios", ctx -> {
            Usuario user = ctx.bodyAsClass(Usuario.class);
            if (user.getNome() == null || user.getEmail() == null) {
                ctx.status(HttpStatus.BAD_REQUEST).result("Nome e e-mail são obrigatórios");
                return;
            }
            usuarios.add(user);
            ctx.status(HttpStatus.CREATED).json(user);
        });

        app.get("/usuarios", ctx -> ctx.json(usuarios));

        app.get("/usuarios/{email}", ctx -> {
            String email = ctx.pathParam("email");
            usuarios.stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .ifPresentOrElse(
                    ctx::json,
                    () -> ctx.status(HttpStatus.NOT_FOUND).result("Usuário não encontrado")
                );
        });

        return app;
    }
}
