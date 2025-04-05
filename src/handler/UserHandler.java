package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;

import java.io.IOException;

public class UserHandler extends BaseHttpHandler implements HttpHandler {
    public UserHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            String method = httpExchange.getRequestMethod();
            String path = httpExchange.getRequestURI().getPath();
            String[] part = path.split("/");
            if (method.equals("GET")) {
                if (part.length == 2 && part[1].equals("history")) {
                    String history = gson.toJson(taskManager.getHistory());
                    sendText(httpExchange, history, 200);
                }
                if (part.length == 2 && part[1].equals("prioritized")) {
                    String prioritized = gson.toJson(taskManager.getPrioritizedTasks());
                    sendText(httpExchange, prioritized, 200);
                } else {
                    sendBadRequest(httpExchange);
                }
            } else {
                sendMethodNotAllowed(httpExchange);
            }
        } catch (Exception e) {
            sendExceptions(httpExchange, e.getMessage());
        }
    }
}