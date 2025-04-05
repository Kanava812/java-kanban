import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import webserver.HttpTaskServer;
import tasks.Status;
import manager.Managers;
import manager.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import adapter.DurationAdapter;
import adapter.LocalDateTimeAdapter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class HttpTaskServerTest {
    TaskManager manager;
    HttpTaskServer taskServer;
    HttpClient client = HttpClient.newHttpClient();
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    @Test
    public void postGetDeleteTest() throws IOException, InterruptedException {
        manager = Managers.getDefaultTaskManager();
        taskServer = new HttpTaskServer(manager);
        taskServer.start();

        URI tasksURL = URI.create("http://localhost:8080/tasks");
        Task task = new Task("Task", "Description 1", Status.NEW, LocalDateTime.now(),
                Duration.ofMinutes(10));
        String jsonTask = gson.toJson(task);
        HttpRequest taskPostRequest = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(jsonTask))
                .uri(tasksURL).build();
        HttpResponse<String> taskPostResponse = client.send(taskPostRequest, HttpResponse.BodyHandlers.ofString());

        Task task2 = new Task("Task2", "Description 2", Status.NEW, LocalDateTime.now().plusMinutes(20),
                Duration.ofMinutes(10));
        String jsonTask2 = gson.toJson(task2);
        HttpRequest taskPostRequest2 = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(jsonTask2))
                .uri(tasksURL).build();
        HttpResponse<String> taskPostResponse2 = client.send(taskPostRequest2, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, taskPostResponse.statusCode(), "Неверный код ответа");
        assertEquals(201, taskPostResponse2.statusCode(), "Неверный код ответа");
        assertNotNull(String.valueOf(manager.getTasks()));
        assertEquals(2, manager.getTasks().size(), "Некорректное количество задач");
        assertEquals("Task", manager.getTasks().getFirst().getName());
        assertEquals("Task2", manager.getTasks().getLast().getName());

        URI epicsURL = URI.create("http://localhost:8080/epics");
        Epic epic = new Epic("Epic", "Description 2");
        String jsonEpic = gson.toJson(epic);
        HttpRequest epicPostRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonEpic)).uri(epicsURL).build();
        HttpResponse<String> epicPostResponse = client.send(epicPostRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, epicPostResponse.statusCode(), "Неверный код ответа");
        assertNotNull(String.valueOf(manager.getEpics()));
        assertEquals(1, manager.getEpics().size(), "Некорректное количество эпиков");
        assertEquals("Epic", manager.getEpics().getFirst().getName());

        URI subtasksURL = URI.create("http://localhost:8080/subtasks");
        Subtask subtask = new Subtask("Subtask", "Description 3", Status.NEW,
                LocalDateTime.now().plusHours(2), Duration.ofMinutes(10), 3);
        String jsonSubtask = gson.toJson(subtask);
        HttpRequest subtaskPostRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonSubtask)).uri(subtasksURL).build();
        HttpResponse<String> subtaskPostResponse = client.send(subtaskPostRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, subtaskPostResponse.statusCode(), "Неверный код ответа");
        assertNotNull(String.valueOf(manager.getSubtasks()));
        assertEquals(1, manager.getSubtasks().size(), "Некорректное количество подзадач");
        assertEquals("Subtask", manager.getSubtasks().getFirst().getName());

        String jsonTasks = gson.toJson(manager.getTasks());
        String jsonEpics = gson.toJson(manager.getEpics());
        String jsonSubtasks = gson.toJson(manager.getSubtasks());

        HttpRequest taskGetRequest = HttpRequest.newBuilder().GET().uri(tasksURL).build();
        HttpRequest epicGetRequest = HttpRequest.newBuilder().GET().uri(epicsURL).build();
        HttpRequest subtaskGetRequest = HttpRequest.newBuilder().GET().uri(subtasksURL).build();
        HttpResponse<String> taskGetResponse = client.send(taskGetRequest, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> epicGetResponse = client.send(epicGetRequest, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> subtaskGetResponse = client.send(subtaskGetRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(jsonTasks, taskGetResponse.body());
        assertEquals(jsonEpics, epicGetResponse.body());
        assertEquals(jsonSubtasks, subtaskGetResponse.body());
        assertEquals(200, taskGetResponse.statusCode());
        assertEquals(200, epicGetResponse.statusCode());
        assertEquals(200, epicGetResponse.statusCode());

        URI tasksDelURL = URI.create(tasksURL + "/1");
        HttpRequest taskDeleteRequest = HttpRequest.newBuilder().DELETE().uri(tasksDelURL).build();
        HttpResponse<String> taskDeleteResponse = client.send(taskDeleteRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, taskDeleteResponse.statusCode(), "Неверный код ответа");
        assertEquals(1, manager.getTasks().size(), "Некорректное количество задач");
        assertEquals("Task2", manager.getTasks().getFirst().getName());

        URI subtasksDelURL = URI.create(subtasksURL + "/4");
        HttpRequest subtaskDeleteRequest = HttpRequest.newBuilder().DELETE().uri(subtasksDelURL).build();
        HttpResponse<String> subtaskDeleteResponse = client.send(subtaskDeleteRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, subtaskDeleteResponse.statusCode(), "Неверный код ответа");
        assertEquals(0, manager.getSubtasks().size(), "Некорректное количество подзадач");

        URI epicsDelURL = URI.create(epicsURL + "/3");
        HttpRequest epicDeleteRequest = HttpRequest.newBuilder().DELETE().uri(epicsDelURL).build();
        HttpResponse<String> epicDeleteResponse = client.send(epicDeleteRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, epicDeleteResponse.statusCode(), "Неверный код ответа");
        assertEquals(0, manager.getEpics().size(), "Некорректное количество эпиков");

        taskServer.stop();
    }
}
