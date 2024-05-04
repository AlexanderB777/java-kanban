package handlers;

import Managers.FileBackedTaskManager;
import Tasks.Subtask;
import Tasks.Task;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import utils.DurationAdapter;
import utils.LocalDateTimeAdapter;
import utils.SubtaskCreationException;
import utils.TaskInteractionException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.logging.Logger;

public class SubtasksHandler extends BaseHttpHandler implements HttpHandler {
    private static final Logger log = Logger.getLogger(TasksHandler.class.getName());


    public SubtasksHandler(FileBackedTaskManager taskManager) {
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
                if (uriArray.length > 2) {
                    int id = Integer.parseInt(uriArray[2]);

                    if (!taskManager.getSubtasks().containsKey(id)) {
                        sendNotFound(exchange);
                    } else {
                        Subtask subtask = taskManager.getSubtaskById(id);
                        String subtaskJson = JSON_MAPPER.toJson(subtask);
                        sendText(exchange, subtaskJson, 200);
                    }

                } else {
                    HashMap<Integer, Subtask> subtasks = taskManager.getSubtasks();
                    if (subtasks.isEmpty()) {
                        sendNotFound(exchange);
                        return;
                    }
                    String tasksJson = JSON_MAPPER.toJson(subtasks);
                    sendText(exchange, tasksJson, 200);
                }

            }
            case "POST" -> {
                String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                Subtask subtask = JSON_MAPPER.fromJson(body, Subtask.class);
                int id = subtask.getId();

                if (!taskManager.getSubtasks().containsKey(id)) {
                    sendNotFound(exchange);
                }

                if (id != 0) {
                    try {
                        taskManager.updateSubtask(subtask);
                        sendText(exchange, "Subtask Successfully updated", 201);
                    } catch (TaskInteractionException taskInteractionException) {
                        log.severe(taskInteractionException.getMessage());
                        sendHasInteractions(exchange);
                    }


                } else {
                    try {
                        taskManager.createSubtask(subtask);
                        sendText(exchange, "Subtask Successfully created", 201);
                    } catch (TaskInteractionException taskInteractionException) {
                        log.severe(taskInteractionException.getMessage());
                        sendHasInteractions(exchange);
                    } catch (SubtaskCreationException subtaskCreationException) {
                        log.severe(subtaskCreationException.getMessage());
                        sendBadRequest(exchange, subtaskCreationException.getMessage());
                    }


                }
                exchange.close();
            }
            case "DELETE" -> {
                int id = Integer.parseInt(uriArray[2]);
                if (taskManager.getSubtasks().containsKey(id)) {
                    taskManager.removeSubtaskById(id);
                    sendText(exchange, "Subtask successfully deleted", 200);
                } else {
                    sendNotFound(exchange);
                }

            }
        }
    }
}
