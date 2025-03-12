package manager;

import file.TaskCsvFormatHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;
import tasks.Epic;
import tasks.Subtask;
import tasks.Status;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class FileBackedTaskManagerTest {

   private File tempFile;
    FileBackedTaskManager manager;

    @BeforeEach
    void setUp() throws IOException {
        tempFile = File.createTempFile("storage", ".csv");
        manager = new FileBackedTaskManager(tempFile);
    }

    @AfterEach
    void tearDown() {
        tempFile.delete();
    }


    @Test
    public void saveAndLoadEmptyFileTest(){
        manager.saveToFile();

        assertTrue(Files.exists(tempFile.toPath()));
        assertTrue(manager.getTasks().isEmpty());
        assertTrue(manager.getEpics().isEmpty());
        assertTrue(manager.getSubtasks().isEmpty());
    }

    @Test
    public void saveLoadAndRemoveTasksTest() throws Exception {
        Task task1 = new Task("Task 1", "Description 1", Status.NEW);
        Task task2 = new Task("Task 2", "Description 2", Status.NEW);
        Epic epic1 = new Epic("Epic 1", "Description 2");
        manager.createTask(task1);
        manager.createTask(task2);
        manager.createEpic(epic1);
        Subtask subtask1 = new Subtask("SubTask 1", "Description 1", Status.NEW, epic1.getId());
        manager.createSubtask(subtask1);

        assertTrue(Files.exists(tempFile.toPath()));

        List<String> lines = Files.readAllLines(tempFile.toPath());
        assertEquals(5, lines.size(), "Заголовок и 4 задачи");
        assertEquals(TaskCsvFormatHandler.getHeader(), lines.get(0));
        assertTrue(lines.contains(TaskCsvFormatHandler.toString(task1)));
        assertTrue(lines.contains(TaskCsvFormatHandler.toString(task2)));
        assertTrue(lines.contains(TaskCsvFormatHandler.toString(epic1)));
        assertTrue(lines.contains(TaskCsvFormatHandler.toString(subtask1)));

        assertEquals(2, manager.getTasks().size());
        assertEquals(1, manager.getEpics().size());
        assertEquals(1, manager.getSubtasks().size());

        manager.deleteAllTasks();
        manager.deleteAllEpics();
        manager.deleteAllSubtasks();

        assertTrue(manager.getTasks().isEmpty());
        assertTrue(manager.getEpics().isEmpty());
        assertTrue(manager.getSubtasks().isEmpty());
    }
}