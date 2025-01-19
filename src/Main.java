import manager.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.Task;
import tasks.Subtask;

public class Main {
    public static void main(String[] args) {
        TaskManager manager = new TaskManager();

        Task task1 = new Task("Задача первая", "Описание первой задачи", Status.NEW);
        Task task2 = new Task("Задача вторая","Описание второй задачи",Status.NEW);

        manager.createTask(task1);
        manager.createTask(task2);

        Epic epic1 = new Epic("Первый эпик","Описание первого эпика", Status.NEW);
        Epic epic2 = new Epic("Второй эпик","Описание второго эпика", Status.NEW);

        manager.createEpic(epic1);
        manager.createEpic(epic2);

        Subtask subtask1 = new Subtask("Первая подзадача первого эпика", "Описание первой подзадачи первого эпика", Status.NEW,epic1.getId());
        Subtask subtask2 = new Subtask("Вторая подзадача первого эпика", "Описание второй подзадачи первого эпика", Status.NEW,epic1.getId());
        Subtask subtask3 = new Subtask("Первая подзадача второго эпика", "Описание первой подзадачи второго эпика", Status.NEW,epic2.getId());

        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);
        manager.createSubtask(subtask3);

        System.out.println(task1);
        System.out.println(task2);
        System.out.println(epic1);
        System.out.println(epic2);
        System.out.println(subtask1);
        System.out.println(subtask2);
        System.out.println(subtask3);
        System.out.println();

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

        manager.updateEpic(epic1);
        manager.updateEpic(epic2);

        System.out.println(task1);
        System.out.println(task2);
        System.out.println(epic1);
        System.out.println(epic2);
        System.out.println(subtask1);
        System.out.println(subtask2);
        System.out.println(subtask3);
        System.out.println();

        manager.deleteTask(1);
        manager.deleteEpic(3);

        manager.printTasks();
        manager.printEpics();
        manager.printSubtasks();
    }
}
