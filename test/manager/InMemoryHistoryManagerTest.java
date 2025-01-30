package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
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
        Task task = new Task("Task 1", "Description 1",Status.NEW);
        Task task2 = new Task("Task 2", "Description 2", Status.NEW);
        historyManager.addTask(task);
        historyManager.addTask(task2);
        List<Task> history = historyManager.getHistory();

        assertEquals(2, history.size());
        assertEquals(task2, history.get(1));
    }

    @Test
    void historyLimit() {
        for (int i = 1; i <= 12; i++) {
            historyManager.addTask(new Task("Task " + i, "Description " + i,Status.NEW));
        }

        List<Task> history = historyManager.getHistory();

        assertEquals(10, history.size());
        assertEquals("Task 4", history.get(1).getName());
        assertEquals("Task 12", history.get(9).getName());
    }
}