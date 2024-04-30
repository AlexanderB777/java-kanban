package handlers;

import Managers.FileBackedTaskManager;
import Tasks.Epic;
import Tasks.Subtask;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

public class EpicsHandler extends BaseHttpHandler implements HttpHandler {

    private static final Logger log = Logger.getLogger(TasksHandler.class.getName());

    public EpicsHandler(FileBackedTaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        URI requestURI = exchange.getRequestURI();

        String uri = requestURI.getPath();
        String[] uriArray = uri.split("/");

        switch (requestMethod) {
            case "GET" -> {
                if (uriArray.length == 3) {
                    int id = Integer.parseInt(uriArray[2]);

                    if (!taskManager.getTasks().containsKey(id)) {
                        sendNotFound(exchange);
                    } else {
                        Epic epic = taskManager.getEpicById(id);
                        String epicJson = JSON_MAPPER.toJson(epic);
                        sendText(exchange, epicJson, 200);
                    }

                } else if (uriArray.length == 2) {
                    HashMap<Integer, Epic> epics = taskManager.getEpics();
                    if (epics.isEmpty()) {
                        sendNotFound(exchange);
                        return;
                    }
                    String tasksJson = JSON_MAPPER.toJson(epics);
                    sendText(exchange, tasksJson, 200);
                } else if (uriArray.length == 4) {
                    if (!uriArray[3].equals("subtasks")) {
                        sendBadRequest(exchange, "Ошибка запроса");
                    }
                    int id = Integer.parseInt(uriArray[2]);
                    if (!taskManager.getEpics().containsKey(id)) {
                        sendNotFound(exchange);
                    }
                    List<Subtask> subtasksFromEpic = taskManager.getEpicSubtasks(id);
                    String subtasksFromEpicJson = JSON_MAPPER.toJson(subtasksFromEpic);
                    sendText(exchange, subtasksFromEpicJson, 200);
                }
            }

            case "POST" -> {
                String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                Epic epic = JSON_MAPPER.fromJson(body, Epic.class);
                int id = epic.getId();
                if (taskManager.getEpics().containsKey(id)) {
                    sendBadRequest(exchange, "Такой эпик уже существует");
                }
                taskManager.createEpic(epic);
                exchange.close();
            }

            case "DELETE" -> {
                int id = Integer.parseInt(uriArray[2]);
                if (!taskManager.getEpics().containsKey(id)) {
                    sendNotFound(exchange);
                }
                taskManager.removeEpicById(id);
                sendText(exchange, "Эпик удален", 200);
                exchange.close();

            }
        }
    }
}
