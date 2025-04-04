package webserver;

import com.sun.net.httpserver.HttpServer;
import handler.*;
import manager.Managers;
import manager.TaskManager;
import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    public static final int PORT = 8080;
    static TaskManager taskManager = Managers.getDefaultTaskManager();
    HttpServer httpServer;

    public HttpTaskServer(TaskManager taskManager) {
        HttpTaskServer.taskManager = taskManager;
    }

    public static void main(String[] args) throws IOException {
        HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    public void start() throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TaskHandler(taskManager));
        httpServer.createContext("/epics", new EpicHandler(taskManager));
        httpServer.createContext("/subtasks", new SubtaskHandler(taskManager));

        httpServer.createContext("/history", new UserHandler(taskManager));
        httpServer.createContext("/prioritized", new UserHandler(taskManager));
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(0);
    }
}
