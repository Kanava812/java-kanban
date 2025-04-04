package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {
    public TaskHandler(TaskManager taskManager) {
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
                    if (part.length == 2 && part[1].equals("tasks")) {
                        String tasksJson = gson.toJson(taskManager.getTasks());
                        sendText(httpExchange, tasksJson, 200);
                    } else if (part.length == 3 && part[1].equals("tasks")) {
                        int id = Integer.parseInt(part[2]);
                        Optional<Task> taskOpt = Optional.ofNullable(taskManager.getTask(id));
                        if (taskOpt.isPresent()) {
                            String taskJson = gson.toJson(taskOpt.get());
                            sendText(httpExchange, taskJson, 200);
                        } else {
                            sendNotFound(httpExchange);
                        }
                    } else {
                        sendExceptions(httpExchange, "Ошибка при обработке запроса.");
                    }
                    break;

                case "POST":
                    if (part.length == 2 && part[1].equals("tasks")) {
                        InputStream inputStream = httpExchange.getRequestBody();
                        Task task = gson.fromJson(new InputStreamReader(inputStream, StandardCharsets.UTF_8), Task.class);
                        inputStream.close();
                        if (Objects.nonNull(task.getId())) {
                            try {
                                taskManager.updateTask(task);
                                sendText(httpExchange, "Задача c id" + task.getId() + " успешно обновлена.", 201);
                            } catch (IllegalArgumentException e) {
                                sendHasInteractions(httpExchange);
                            }
                        } else {
                            try {
                                taskManager.createTask(task);
                                sendText(httpExchange, "Задача c id" + task.getId() + " успешно добавлена.", 201);
                            } catch (IllegalArgumentException e) {
                                sendHasInteractions(httpExchange);
                            }
                        }
                    } else {
                        sendExceptions(httpExchange, "Ошибка при обработке запроса.");
                    }
                    break;

                case "DELETE":
                    if (part.length == 3 && part[1].equals("tasks")) {
                        int id = Integer.parseInt(part[2]);
                        taskManager.deleteTask(id);
                        sendText(httpExchange, "Задача c id" + id + " успешно удалена.", 200);
                    } else {
                        sendExceptions(httpExchange, "Ошибка при обработке запроса.");
                    }
                    break;
            }
        } catch (Exception e) {
            sendExceptions(httpExchange, e.getMessage());
        }
    }
}
