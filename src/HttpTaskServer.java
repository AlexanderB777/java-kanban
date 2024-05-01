import Managers.FileBackedTaskManager;
import Managers.Managers;
import com.sun.net.httpserver.HttpServer;
import handlers.*;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    public static void main(String[] args) throws Exception {
        FileBackedTaskManager fileBackedTaskManager = Managers.getFileBackedTaskManager();
        fileBackedTaskManager.restore();
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(8080), 0);

        httpServer.createContext("/subtasks", new SubtasksHandler(fileBackedTaskManager));
        httpServer.createContext("/tasks", new TasksHandler(fileBackedTaskManager));
        httpServer.createContext("/epics", new EpicsHandler(fileBackedTaskManager));
        httpServer.createContext("/history", new HistoryHandler(fileBackedTaskManager));
        httpServer.createContext("/prioritized", new PrioritizedHandler(fileBackedTaskManager));

        httpServer.start();



    }

    public void stop(HttpServer httpServer) {
        httpServer.stop(1);
    }


}


