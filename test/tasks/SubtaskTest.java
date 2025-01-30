package tasks;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {
    @Test
    void subtaskEqualityBasedOnIdTestTest() {
        Subtask subtask = new Subtask("SubTask1", "Description1", Status.NEW,1);
        Subtask subtask2 = new Subtask("SubTask2", "Description2", Status.NEW,1);
        subtask.setId(1);
        subtask2.setId(1);

        assertEquals(subtask.getId(), subtask2.getId(), "id должны быть равны");
        assertEquals(subtask, subtask2,"Задачи с одинаковым id должны быть одинаковыми");

    }

}