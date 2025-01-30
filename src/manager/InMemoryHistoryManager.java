package manager;

import tasks.Task;

import java.util.List;
import java.util.Objects;
import java.util.ArrayList;


public class InMemoryHistoryManager implements HistoryManager {

    private final List<Task> historyStorage = new ArrayList<>();

    @Override
    public void addTask(Task task) {
        if (Objects.isNull(task)) return;

        historyStorage.add(task.getShapshot());

        if (historyStorage.size() > 10) historyStorage.remove(0);
    }


    @Override
    public List<Task> getHistory() {
        return historyStorage;
    }
}
