package handlers;

import Managers.FileBackedTaskManager;
import Tasks.Task;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import utils.TaskInteractionException;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.logging.Logger;

public class TasksHandler extends BaseHttpHandler implements HttpHandler {

    private static final Logger log = Logger.getLogger(TasksHandler.class.getName());
    public TasksHandler(FileBackedTaskManager taskManager) {
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

                    if (!taskManager.getTasks().containsKey(id)) {
                        sendNotFound(exchange);
                    } else {
                        Task task = taskManager.getTaskById(id);
                        String taskJson = JSON_MAPPER.toJson(task);
                        sendText(exchange, taskJson, 200);
                    }

                } else {
                    HashMap<Integer, Task> tasks = taskManager.getTasks();
                    if (tasks.isEmpty()) {
                        sendNotFound(exchange);
                        return;
                    }
                    String tasksJson = JSON_MAPPER.toJson(tasks);
                    sendText(exchange, tasksJson, 200);
                }

            }
            case "POST" -> {
                String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                Task task = JSON_MAPPER.fromJson(body, Task.class);
                if (task.getId() != null) {
                    try {
                        taskManager.updateTask(task);
                        sendText(exchange, "Task Successfully updated", 201);
                    } catch (TaskInteractionException taskInteractionException) {
                        log.severe(taskInteractionException.getMessage());
                        sendHasInteractions(exchange);
                    }
                } else {
                    try {
                        taskManager.createTask(task);
                        sendText(exchange, "Task Successfully created", 201);
                    } catch (TaskInteractionException taskInteractionException) {
                        log.severe(taskInteractionException.getMessage());
                        sendHasInteractions(exchange);
                    }


                }
                exchange.close();
            }
            case "DELETE" -> {
                int id = Integer.parseInt(uriArray[2]);
                if (taskManager.getTasks().containsKey(id)) {
                    taskManager.removeTaskById(id);
                    sendText(exchange, "Task successfully deleted", 200);
                } else {
                    sendNotFound(exchange);
                }

            }
        }
    }
}
