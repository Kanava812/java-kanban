package tasks;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    @Test
    void taskEqualityBasedOnIdTestTest() {
        Task task = new Task("Task1", "Description1", Status.NEW);
        Task task2 = new Task("Task2", "Description2", Status.NEW);
        task.setId(1);
        task2.setId(1);

        assertEquals(task.getId(), task2.getId(), "id должны быть равны");
        assertEquals(task, task2,"Задачи с одинаковым id должны быть одинаковыми");

    }


}