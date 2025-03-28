package tasks;

import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
class EpicTest {
    @Test
    public void epicEqualityBasedOnIdTest() {
        Epic epic = new Epic("Epic 1", "Description1");
        Epic epic2 = new Epic("Epic 2", "Description2");

        epic.setId(1);
        epic2.setId(1);

        assertEquals(epic.getId(), epic2.getId(), "id должны быть равны");
        assertEquals(epic, epic2, "Epic с одинаковым id должны быть одинаковыми");
    }


    @Test
    public void epicTimeTest() throws IOException {
        File file = File.createTempFile("storage", ".csv");

        TaskManager manager = Managers.getDefaultTaskManager(file);

        Epic epic1 = new Epic("Epic 1", "Description1");

        manager.createEpic(epic1);

        Subtask subtask1 = new Subtask("Subtask 1", "Description1", Status.NEW,
                LocalDateTime.of(2025, 3, 27, 10, 0),
                Duration.ofMinutes(10), epic1.getId());
        Subtask subtask2 = new Subtask("Subtask 2", "Description2", Status.DONE,
                LocalDateTime.of(2025, 3, 27, 10, 30),
                Duration.ofMinutes(10), epic1.getId());
        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);

        assertEquals(epic1.getStartTime(), LocalDateTime.of(2025, 3, 27, 10, 0));
        assertEquals(epic1.getEndTime(), LocalDateTime.of(2025, 3, 27, 10, 40));
        assertEquals(1, manager.getEpics().size());
        assertEquals(2, manager.getSubtasks().size());

        manager.deleteSubtask(2);

        assertEquals(epic1.getStartTime(), LocalDateTime.of(2025, 3, 27, 10, 30));
        assertEquals(epic1.getEndTime(), LocalDateTime.of(2025, 3, 27, 10, 40));
        assertEquals(epic1.getDuration(), Duration.ofMinutes(10));
    }
}