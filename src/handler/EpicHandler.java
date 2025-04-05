package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;
import tasks.Epic;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {
    public EpicHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            String method = httpExchange.getRequestMethod();
            String path = httpExchange.getRequestURI().getPath();
            String[] part = path.split("/");
            switch (method) {
                case "GET":
                    if (part.length == 2 && part[1].equals("epics")) {
                        String epicsJson = gson.toJson(taskManager.getEpics());
                        sendText(httpExchange, epicsJson, 200);
                    } else if (part.length == 3 && part[1].equals("epics")) {
                        int id = Integer.parseInt(part[2]);
                        Optional<Epic> epic = Optional.ofNullable(taskManager.getEpic(id));
                        if (epic.isPresent()) {
                            String epicJson = gson.toJson(epic.get());
                            sendText(httpExchange, epicJson, 200);
                        } else {
                            sendNotFound(httpExchange);
                        }
                    } else if (part.length == 4 && part[1].equals("epics") && part[3].equals("subtasks")) {
                        int id = Integer.parseInt(part[2]);
                        Optional<Epic> epicOpt = Optional.ofNullable(taskManager.getEpic(id));
                        if (epicOpt.isPresent()) {
                            String epicSubtasks = gson.toJson(epicOpt.get().getEpicSubtasks());
                            sendText(httpExchange, epicSubtasks, 200);
                        } else {
                            sendNotFound(httpExchange);
                        }
                    } else {
                        sendBadRequest(httpExchange);
                    }
                    break;

                case "POST":
                    if (part.length == 2 && part[1].equals("epics")) {
                        try {
                            InputStream inputStream = httpExchange.getRequestBody();
                            Epic jsonEpic = gson.fromJson(new InputStreamReader(
                                    inputStream, StandardCharsets.UTF_8), Epic.class);
                            String name = jsonEpic.getName();
                            String description = jsonEpic.getDescription();
                            Epic epic = new Epic(name, description);
                            inputStream.close();
                            taskManager.createEpic(epic);
                            sendText(httpExchange, "Эпик c id" + epic.getId() + " успешно добавлен.", 201);
                        } catch (NullPointerException e) {
                            sendExceptions(httpExchange, e.getMessage());
                        }

                    } else {
                        sendBadRequest(httpExchange);
                    }
                    break;

                case "DELETE":
                    if (part.length == 3 && part[1].equals("epics")) {
                        int id = Integer.parseInt(part[2]);
                        taskManager.deleteEpic(id);
                        sendText(httpExchange, "Эпик c id" + id + " успешно удален.", 200);
                    } else {
                        sendBadRequest(httpExchange);
                    }
                    break;
                default:
                    sendMethodNotAllowed(httpExchange);
            }
        } catch (Exception e) {
            sendExceptions(httpExchange, e.getMessage());
        }
    }
}
