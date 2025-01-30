import manager.HistoryManager;
import manager.InMemoryTaskManager;
import manager.Managers;
import manager.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.Task;
import tasks.Subtask;

public class Main {
    public static void main(String[] args) {
        TaskManager manager = Managers.getDefaultTaskManager();

        printInPreviousSprint(manager);

    }

    private static void printInPreviousSprint(TaskManager manager) {
        Task task1 = new Task("Задача первая", "Описание первой задачи", Status.NEW);
        Task task2 = new Task("Задача вторая", "Описание второй задачи", Status.NEW);

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

        Subtask subtask1 = new Subtask("Первая подзадача первого эпика", "Описание первой подзадачи первого эпика", Status.NEW, epic1.getId());
        Subtask subtask2 = new Subtask("Вторая подзадача первого эпика", "Описание второй подзадачи первого эпика", Status.NEW, epic1.getId());
        Subtask subtask3 = new Subtask("Первая подзадача второго эпика", "Описание первой подзадачи второго эпика", Status.NEW, epic2.getId());

        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);
        manager.createSubtask(subtask3);

        printAllTasks(manager);

        manager.getEpic(epic1.getId());
        manager.getEpic(epic2.getId());
        manager.getSubTask(subtask1.getId());
        manager.getSubTask(subtask2.getId());
        manager.getSubTask(subtask3.getId());
        manager.getTask(task1.getId());
        manager.getTask(task2.getId());


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

        manager.deleteTask(1);
        manager.deleteEpic(3);
        manager.deleteSubtask(5);
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
            for (Task task : manager.getSubTasksByEpic((Epic) epic)) {
                System.out.println("--> " + task);
            }


        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getSubtasks()) {
            System.out.println(subtask);
        }

        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
        System.out.println("-".repeat(100));
        System.out.println();
    }

}
