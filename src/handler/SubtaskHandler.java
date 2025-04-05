package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;
import tasks.Subtask;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;

public class SubtaskHandler extends BaseHttpHandler implements HttpHandler {
    public SubtaskHandler(TaskManager taskManager) {
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
                    if (part.length == 2 && part[1].equals("subtasks")) {
                        String subtasksJson = gson.toJson(taskManager.getSubtasks());
                        sendText(httpExchange, subtasksJson, 200);
                    } else if (part.length == 3 && part[1].equals("subtasks")) {
                        int id = Integer.parseInt(part[2]);
                        Optional<Subtask> subtaskOpt = Optional.ofNullable(taskManager.getSubTask(id));
                        if (subtaskOpt.isPresent()) {
                            String subtaskJson = gson.toJson(subtaskOpt.get());
                            sendText(httpExchange, subtaskJson, 200);
                        } else {
                            sendNotFound(httpExchange);
                        }
                    } else {
                        sendBadRequest(httpExchange);
                    }
                    break;
                case "POST":
                    if (part.length == 2 && part[1].equals("subtasks")) {
                        InputStream inputStream = httpExchange.getRequestBody();
                        Subtask subtask = gson.fromJson(new InputStreamReader(inputStream, StandardCharsets.UTF_8),
                                Subtask.class);
                        inputStream.close();
                        if (Objects.nonNull(subtask.getId())) {
                            try {
                                taskManager.updateSubtask(subtask);
                                sendText(httpExchange, "Подзадача c id" + subtask.getId() + " успешно обновлена.", 201);
                            } catch (IllegalArgumentException e) {
                                sendHasInteractions(httpExchange);
                            }
                        } else {
                            try {
                                taskManager.createSubtask(subtask);
                                sendText(httpExchange, "Подзадача c id" + subtask.getId()
                                        + " успешно добавлена в Эпик с id" + subtask.getEpicId() + ".", 201);
                            } catch (IllegalArgumentException e) {
                                sendHasInteractions(httpExchange);
                            }
                        }
                    } else {
                        sendBadRequest(httpExchange);
                    }
                    break;
                case "DELETE":
                    if (part.length == 3 && part[1].equals("subtasks")) {
                        int id = Integer.parseInt(part[2]);
                        taskManager.deleteSubtask(id);
                        sendText(httpExchange, "Подзадача c id" + id + " успешно удалена.", 200);
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
