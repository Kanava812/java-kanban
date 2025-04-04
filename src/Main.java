import file.ManagerSaveException;
import manager.Managers;
import manager.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.Task;
import tasks.Subtask;
import webserver.HttpTaskServer;

import java.io.File;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class Main {

    private static final String FILE_NAME = "src/resourses/storage.csv";


    public static void main(String[] args) {

        File fileStorage = createFile(FILE_NAME);
        TaskManager manager = Managers.getDefaultTaskManager(fileStorage);

        try {
            printInPreviousSprint(manager);
            System.out.printf("Содержимое файла:%n%s", Files.readString(fileStorage.toPath()));
            System.out.println("-".repeat(100));
            System.out.println();
            System.out.printf("Задачи, загруженные из файла: %n%s%n%s%n%s%n", manager.getTasks(), manager.getEpics(),
                    manager.getSubtasks());
            System.out.println("-".repeat(100));
            System.out.println();
            HttpTaskServer httpTaskServer = new HttpTaskServer(manager);
            httpTaskServer.start();
            System.out.println("HTTP-сервер запущен на " + httpTaskServer.PORT + " порту!");
            httpTaskServer.stop();

        } catch (ManagerSaveException | IOException exception) {
            System.out.println("Перезапустите main()");
        }

        fileStorage.delete();
    }


    private static File createFile(String fileName) {
        try {
            if (Files.exists(Paths.get(fileName))) {
                return Paths.get("src/resourses/storage.csv").toFile();
            } else {
                return Files.createFile(Paths.get(fileName)).toFile();
            }
        } catch (IOException e) {
            System.out.println("Файл для хранилища не создан." + e.getMessage());
        }
        throw new UnsupportedOperationException("К сожалению файл не создан");
    }


    private static void printInPreviousSprint(TaskManager manager) {
        Task task1 = new Task("Задача первая", "Описание первой задачи", Status.NEW,
                LocalDateTime.now().minusMinutes(500), Duration.ofMinutes(1));
        Task task2 = new Task("Задача вторая", "Описание второй задачи", Status.NEW,
                LocalDateTime.now().minusMinutes(450), Duration.ofMinutes(3));

        manager.createTask(task1);
        Task task1Modified = manager.createTask(task2);
        Task taskFirst = manager.getTask(task1Modified.getId());
        taskFirst.setStatus(Status.IN_PROGRESS);
        Task taskFirstModified = manager.updateTask(taskFirst);
        Task taskSecond = manager.getTask(taskFirstModified.getId());
        taskSecond.setDescription(taskSecond.getDescription() + " Дополнение 1");
        Task taskSecondModified = manager.updateTask(taskSecond);
        Task taskThird = manager.getTask(taskSecondModified.getId());


        Epic epic1 = new Epic("Первый эпик", "Описание первого эпика");
        Epic epic2 = new Epic("Второй эпик", "Описание второго эпика");


        manager.createEpic(epic1);
        manager.createEpic(epic2);

        Subtask subtask1 = new Subtask("Первая подзадача первого эпика",
                "Описание первой подзадачи первого эпика", Status.NEW, LocalDateTime.now().minusMinutes(400),
                Duration.ofMinutes(1), epic1.getId());
        Subtask subtask2 = new Subtask("Вторая подзадача первого эпика",
                "Описание второй подзадачи первого эпика", Status.NEW, LocalDateTime.now().minusMinutes(350),
                Duration.ofMinutes(10), epic1.getId());
        Subtask subtask3 = new Subtask("Первая подзадача второго эпика",
                "Описание первой подзадачи второго эпика", Status.NEW, LocalDateTime.now().minusMinutes(300),
                Duration.ofMinutes(6), epic2.getId());

        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);
        manager.createSubtask(subtask3);

        printAllTasks(manager);

        manager.getEpic(epic1.getId());
        manager.getEpic(epic2.getId());
        manager.getSubTask(subtask1.getId());
        manager.getSubTask(subtask2.getId());
        manager.getSubTask(subtask2.getId());
        manager.getSubTask(subtask2.getId());
        manager.getTask(task1.getId());
        manager.getTask(task2.getId());
        manager.getTask(task2.getId());
        manager.getTask(task2.getId());
        manager.getSubTask(subtask3.getId());

        task1.setStatus(Status.IN_PROGRESS);
        manager.updateTask(task1);
        task2.setStatus(Status.IN_PROGRESS);
        manager.updateTask(task2);

        subtask1.setStatus(Status.IN_PROGRESS);
        manager.updateSubtask(subtask1);
        subtask2.setStatus(Status.IN_PROGRESS);
        manager.updateSubtask(subtask2);
        subtask3.setStatus(Status.DONE);
        manager.updateSubtask(subtask3);

        printAllTasks(manager);

        Task task3 = new Task("Задача третья", "Описание третьей задачи", Status.NEW,
                LocalDateTime.now().minusMinutes(220), Duration.ofMinutes(15));
        manager.createTask(task3);
        manager.getTask(task3.getId());

        manager.deleteTask(1);
        manager.deleteEpic(4);
        manager.deleteSubtask(6);

        manager.getEpic(epic1.getId());
        manager.getEpic(epic2.getId());
        manager.getSubTask(subtask1.getId());
        manager.getSubTask(subtask2.getId());
        manager.getSubTask(subtask3.getId());
        manager.getTask(task1.getId());
        manager.getTask(task2.getId());

        printAllTasks(manager);

    }


    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Task epic : manager.getEpics()) {
            System.out.println(epic);
            for (Task task : ((Epic) epic).getEpicSubtasks()) {
                System.out.println("--> " + task);
            }


        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getSubtasks()) {
            System.out.println(subtask);
        }

        System.out.println("История(" + manager.getHistory().size() + "):");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
        System.out.println("-".repeat(100));
        System.out.println();
    }
}
