package handlers;

import Managers.FileBackedTaskManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import utils.DurationAdapter;
import utils.LocalDateTimeAdapter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

public class BaseHttpHandler {
    FileBackedTaskManager taskManager;
    protected final static Gson JSON_MAPPER = new GsonBuilder()
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .setPrettyPrinting()
            .create();

    public BaseHttpHandler(FileBackedTaskManager taskManager) {
        this.taskManager = taskManager;
    }


    protected void sendText(HttpExchange exchange, String text, int responseCode) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(responseCode, 0);
        exchange.getResponseBody().write(resp);
        exchange.close();
    }

    protected void sendNotFound(HttpExchange exchange) throws IOException {
        String text = "Task Not Found";
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(404, resp.length);
        exchange.getResponseBody().write(resp);
        exchange.close();
    }

    protected void sendHasInteractions(HttpExchange exchange) throws IOException {
        String text = "Task has interactions";
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/text;charset=utf-8");
        exchange.sendResponseHeaders(406, resp.length);
        exchange.getResponseBody().write(resp);
        exchange.close();
    }

    protected void sendBadRequest(HttpExchange exchange, String message) throws IOException {
        byte[] resp = message.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/text;charset=utf-8");
        exchange.sendResponseHeaders(400, resp.length);
        exchange.getResponseBody().write(resp);
        exchange.close();
    }
}