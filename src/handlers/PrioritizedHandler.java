package handlers;

import Managers.FileBackedTaskManager;
import Tasks.Task;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.List;

public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {
    public PrioritizedHandler(FileBackedTaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        List<Task> prioritized = taskManager.getPrioritizedTasks();
        String prioritizedJson = JSON_MAPPER.toJson(prioritized);
        sendText(exchange, prioritizedJson, 200);
    }
}
