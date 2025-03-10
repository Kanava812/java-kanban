package manager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Status;
import tasks.Task;

import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    private HistoryManager historyManager;

    @BeforeEach
    void setUp() {
        historyManager = Managers.getDefaultHistoryManager();
    }

    @Test
    public void getDefaultHistoryManagerTest() {
        HistoryManager historyManager2 = Managers.getDefaultHistoryManager();

        assertNotNull(historyManager, "historyManager не должен возвращать null");
        assertNotEquals(historyManager, historyManager2, "Вызов getDefaultHistoryManager() должен возвращать " +
                "новый экземпляр HistoryManager");
    }

    @Test
    void addTasksToHistory() {
        Task task = new Task(1, "Task 1", "Description 1", Status.NEW);
        Task task2 = new Task(2, "Task 2", "Description 2", Status.NEW);
        historyManager.addTask(task);
        historyManager.addTask(task2);
        List<Task> history = historyManager.getHistory();
        assertEquals(task2, history.get(1));
        assertEquals(2, history.size());

        historyManager.addTask(task);
        historyManager.addTask(task);
        assertEquals(2, history.size(), "В истории только оригинальные просмотры");


    }


    @Test
    public void addTaskTest() {
        TaskManager taskManager = Managers.getDefaultTaskManager();
        Task task1 = new Task("Task 1", "Description 1", Status.NEW);
        Task task2 = new Task("Task 2", "Description 2", Status.DONE);
        Task task3 = new Task("Task 3", "Description 3", Status.IN_PROGRESS);

        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);
        taskManager.getTask(task1.getId());
        taskManager.getTask(task2.getId());
        taskManager.getTask(task3.getId());
        List<Task> history = taskManager.getHistory();

        Assertions.assertEquals(3, history.size());
        Assertions.assertEquals(task1, history.get(0));
        Assertions.assertEquals(task2, history.get(1));
        Assertions.assertEquals(task3, history.get(2));
    }

    @Test
    void removeTaskFromHistoryTest() {
        Task task1 = new Task(1, "Task 1", "Description 1", Status.NEW);
        Task task2 = new Task(2, "Task 2", "Description 2", Status.NEW);

        historyManager.addTask(task1);
        historyManager.addTask(task2);

        historyManager.remove(task1.getId());

        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size());
        assertEquals(task2, history.getFirst());
    }
}