package manager;

import java.io.File;

public final class Managers {
    private Managers() {
    }

    public static TaskManager getDefaultTaskManager() {
        return new InMemoryTaskManager();
    }


    public static TaskManager getDefaultTaskManager(File file) {
        return FileBackedTaskManager.loadFromFile(file);
    }


    public static HistoryManager getDefaultHistoryManager() {
        return new InMemoryHistoryManager();
    }
}
