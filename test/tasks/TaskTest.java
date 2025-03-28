package tasks;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    @Test
    void taskEqualityBasedOnIdTestTest() {
        Task task = new Task("Task1", "Description1",Status.NEW, LocalDateTime.now().minusMinutes(500),
                Duration.ofMinutes(1));
        Task task2 = new Task("Task2", "Description2", Status.NEW,LocalDateTime.now().minusMinutes(500),
                Duration.ofMinutes(1));
        task.setId(1);
        task2.setId(1);

        assertEquals(task.getId(), task2.getId(), "id должны быть равны");
        assertEquals(task, task2,"Задачи с одинаковым id должны быть одинаковыми");

    }


}