package handlers;

import Managers.FileBackedTaskManager;
import Tasks.Task;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.List;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {

    public HistoryHandler(FileBackedTaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        List<Task> history = taskManager.getHistory();
        String historyJson = JSON_MAPPER.toJson(history);
        sendText(exchange, historyJson, 200);
    }
}
