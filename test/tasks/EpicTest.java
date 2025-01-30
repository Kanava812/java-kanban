package tasks;

import org.junit.jupiter.api.Test;

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
}