package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.Status;

import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


class InMemoryTaskManagerTest {

    private TaskManager taskManager;

    @BeforeEach
    void setUp() {
        taskManager = Managers.getDefaultTaskManager();
    }


    @Test
    public void getDefaultTaskManagerTest() {
        TaskManager taskManager2 = Managers.getDefaultTaskManager();

        assertNotNull(taskManager, "taskManager не должен возвращать null");
        assertNotEquals(taskManager, taskManager2, "Вызов getDefaultTaskManager() должен возвращать новый экземпляр TaskManager");
    }


    @Test
    void createTaskTest() {
        Task task = new Task("Task 1", "Description 1", Status.NEW);
        Task task2 = taskManager.createTask(task);

        assertNotNull(task2);
        assertEquals(task.getName(), task2.getName());
        assertEquals(task.getDescription(), task2.getDescription());
        assertEquals(task.getStatus(), task2.getStatus());
        assertEquals(task.getId(), task2.getId());
        assertEquals(taskManager.getTask(task.getId()), taskManager.getTask(task2.getId()),"равенство задач с " +
                "одинаковым id");
    }


    @Test
    void createEpicTest() {
        Epic epic = new Epic("Epic 1", "Description 1");
        Epic epic2 = taskManager.createEpic(epic);

        assertNotNull(epic2);
        assertEquals(epic.getName(), epic2.getName());
        assertEquals(epic.getDescription(), epic2.getDescription());
        assertEquals(epic.getId(), epic2.getId());
        assertEquals(epic.getStatus(), epic2.getStatus());
        assertEquals(taskManager.getTask(epic.getId()), taskManager.getTask(epic2.getId()),"равенство задач с " +
                "одинаковым id");
    }


    @Test
    void createSubtaskTest() {
        Epic epic = new Epic("Epic 1", "Description 1");
        taskManager.createEpic(epic);

        Subtask subtask = new Subtask("Subtask 1", "Description 1",Status.NEW, epic.getId());
        Subtask subtask2 = taskManager.createSubtask(subtask);

        assertNotNull(subtask2);
        assertEquals(subtask.getName(), subtask2.getName());
        assertEquals(subtask.getDescription(), subtask2.getDescription());
        assertEquals(subtask.getStatus(), subtask2.getStatus());
        assertEquals(subtask.getId(), subtask2.getId());
        assertEquals(subtask.getEpicId(), subtask2.getEpicId());
        assertEquals(taskManager.getTask(subtask.getId()), taskManager.getTask(subtask2.getId()),"равенство " +
                "задач с одинаковым id");
    }


    @Test
    void updateEpicStatus() {
        Epic epic = new Epic("Epic 1", "Description 1");
        taskManager.createEpic(epic);

        Subtask subtask1 = new Subtask("Subtask 1", "Description 1",Status.NEW, epic.getId());
        Subtask subtask2 = new Subtask("Subtask 2", "Description 2",Status.NEW, epic.getId());

        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        subtask1.setStatus(Status.DONE);
        taskManager.updateSubtask(subtask1);

        assertEquals(Status.IN_PROGRESS, epic.getStatus(), "Статус эпика IN_PROGRESS пока не все подзадачи выполнены");

        subtask2.setStatus(Status.DONE);
        taskManager.updateSubtask(subtask2);

        assertEquals(Status.DONE, epic.getStatus(), "Статус эпика DONE когда все подзадачи выполнены.");
    }


    @Test
    void addTasksInHistory() {
        Task task = taskManager.createTask(new Task("Task 1", "Description 1", Status.NEW));
        Task epic = taskManager.createTask(new Epic("Epic 1", "Description 1"));
        Task subtask = taskManager.createTask(new Subtask("Subtask 1", "Description 1", Status.NEW, epic.getId()));

        taskManager.getTask(task.getId());
        taskManager.getTask(epic.getId());
        taskManager.getTask(subtask.getId());

        List<Task> history = taskManager.getHistory();

        assertEquals(3, history.size(),"Проверка количества просмотров в истории");
        assertEquals(task, history.get(0),"Проверка порядка сохранения задач в историю просмотров");
        assertEquals(epic, history.get(1));
        assertEquals(subtask, history.get(2));
    }


    @Test
    void historyLimit() {
        for (int i = 1; i <= 12; i++) {
            taskManager.createTask(new Task("Task " + i, "Description " + i, Status.NEW));
            taskManager.getTask(i);
        }

        List<Task> history = taskManager.getHistory();

        assertEquals(10, history.size(),"Проверка максимального размера истории просмотров");
        assertEquals(3, history.get(0).getId(),"Если просмотров больше 10, 1 в истории удаляется");
        assertEquals(12, history.get(9).getId());
    }


    @Test
    void getTasksTest() {
        Task task = new Task("Task 1", "Description 1", Status.NEW);
        Task task2 = new Task("Task 2", "Description 2", Status.NEW);
        taskManager.createTask(task);
        taskManager.createTask(task2);
        assertNotNull(taskManager.getTasks());
        assertNotEquals(0, taskManager.getTasks().size());
        assertEquals(2, taskManager.getTasks().size());
    }


    @Test
    void getEpicsTest() {
        Epic task = new Epic("Task 1", "Description 1");
        Epic task2 = new Epic("Task 2", "Description 2");
        taskManager.createEpic(task);
        taskManager.createEpic(task2);
        assertNotNull(taskManager.getEpics());
        assertNotEquals(0, taskManager.getEpics().size());
        assertEquals(2, taskManager.getEpics().size());
    }


    @Test
    void getSubTasksTest() {
        Epic epic = new Epic("Task 1", "Description 1");
        taskManager.createEpic(epic);
        Subtask task = new Subtask("Task 1", "Description 1", Status.NEW, epic.getId());
        Subtask task2 = new Subtask("Task 2", "Description 2", Status.NEW, epic.getId());
        taskManager.createSubtask(task);
        taskManager.createSubtask(task2);
        assertNotNull(taskManager.getSubtasks());
        assertNotEquals(0, taskManager.getSubtasks().size());
        assertEquals(2, taskManager.getSubtasks().size());
    }


    @Test
    void deleteTaskTest(){
        Task task = new Task("Task 1", "Description 1", Status.NEW);
        Task task2 = new Task("Task 2", "Description 2", Status.NEW);
        taskManager.createTask(task);
        taskManager.createTask(task2);

        taskManager.deleteTask(1);
        assertNull(taskManager.getTask(1));
        assertEquals(1, taskManager.getTasks().size());
    }


    @Test
    void deleteEpicTest() {
        Epic epic = new Epic("Epic 1", "Description 1");
        Epic epic2 = new Epic("Epic 2", "Description 2");
        taskManager.createEpic(epic);
        taskManager.createEpic(epic2);
        Subtask subtask = new Subtask("Task 1", "Description 1", Status.NEW, epic.getId());
        Subtask subtask2 = new Subtask("Task 2", "Description 2", Status.NEW, epic.getId());
        taskManager.createSubtask(subtask);
        taskManager.createSubtask(subtask2);

        taskManager.deleteEpic(1);
        assertNull(taskManager.getEpic(1));
        assertEquals(1, taskManager.getEpics().size());
        assertNull(taskManager.getSubTask(3));
        assertNull(taskManager.getSubTask(4));
        assertEquals(0, taskManager.getSubtasks().size());
    }

    @Test
    void deleteSubTaskTest() {
        Epic epic = new Epic("Epic 1", "Description 1");
        taskManager.createEpic(epic);
        Subtask task = new Subtask("Task 1", "Description 1", Status.NEW, epic.getId());
        Subtask task2 = new Subtask("Task 2", "Description 2", Status.NEW, epic.getId());
        taskManager.createSubtask(task);
        taskManager.createSubtask(task2);

        taskManager.deleteSubtask(3);
        assertNull(taskManager.getSubTask(3));
        assertEquals(1, taskManager.getSubtasks().size());
        assertEquals(1, taskManager.getEpics().size());
    }

    @Test
    void deleteAllTasksTest() {
        Task task = new Task("Task 1", "Description 1", Status.NEW);
        Task task2 = new Task("Task 2", "Description 2", Status.NEW);
        taskManager.createTask(task);
        taskManager.createTask(task2);

        taskManager.deleteAllTasks();
        assertNotNull(taskManager.getTasks());
        assertEquals(0, taskManager.getTasks().size());
    }


    @Test
    void deleteAllEpicsTest() {
        Epic task = new Epic("Epic 1", "Description 1");
        Epic task2 = new Epic("Epic 2", "Description 2");
        taskManager.createEpic(task);
        taskManager.createEpic(task2);

        taskManager.deleteAllEpics();
        assertNotNull(taskManager.getEpics());
        assertEquals(0, taskManager.getEpics().size());
    }


    @Test
    void deleteAllSubTasksTest() {
        Epic epic = new Epic("Epic 1", "Description 1");
        taskManager.createEpic(epic);
        Subtask task = new Subtask("Task 1", "Description 1", Status.NEW, epic.getId());
        Subtask task2 = new Subtask("Task 2", "Description 2", Status.NEW, epic.getId());
        taskManager.createSubtask(task);
        taskManager.createSubtask(task2);

        taskManager.deleteAllSubtasks();
        assertNotNull(taskManager.getSubtasks());
        assertEquals(0, taskManager.getSubtasks().size());
    }

}