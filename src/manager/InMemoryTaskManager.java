package manager;

import tasks.*;

import java.util.*;


public class InMemoryTaskManager implements TaskManager {

    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, Subtask> subtasks = new HashMap<>();
    protected final TreeSet<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));

    private final HistoryManager historyManager = Managers.getDefaultHistoryManager();
    protected int generatedId = 0;


    private int generateId() {
        return ++generatedId;
    }


    @Override
    public void addToPrioritizedTasks(Task task) {
        if (task.getStartTime() == null) {
            return;
        }
        prioritizedTasks.add(task);
    }

    public boolean isIntersect(Task task) {
        return getPrioritizedTasks().stream()
                .anyMatch(task1 -> !task1.getEndTime().isBefore(task.getStartTime()) &&
                        !task1.getStartTime().isAfter(task.getEndTime()));
    }


    @Override
    public Task createTask(Task task) {
        if (isIntersect(task)) {
            throw new IllegalArgumentException(task.getName() + " пересекается с другой задачей");
        }
        if (Objects.nonNull(task.getId())) {
            tasks.put(task.getId(), task);
        } else {
            int newId = generateId();
            task.setId(newId);
            tasks.put(task.getId(), task);
            addToPrioritizedTasks(task);
        }
        return task;
    }

    @Override
    public Task updateTask(Task task) {
        if (Objects.nonNull(task.getId())) {
            return task;
        }
        if (!tasks.containsKey(task.getId())) {
            return task;
        }
        tasks.put(task.getId(), task);
        return task;
    }


    @Override
    public Epic createEpic(Epic epic) {
        if (Objects.nonNull(epic.getId())) {
            epics.put(epic.getId(), epic);
        } else {
            int newId = generateId();
            epic.setId(newId);
            epics.put(epic.getId(), epic);
        }
        return epic;
    }


    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            if (epic.getEpicSubtasks().isEmpty()) {
                epic.setStatus(Status.NEW);
            } else {
                int countDone = 0;
                int countNew = 0;
                for (Subtask s : epic.getEpicSubtasks()) {
                    //Subtask s = subtasks.get(num);
                    if (s.getStatus() == Status.DONE) {
                        countDone++;
                    }
                    if (s.getStatus() == Status.NEW) {
                        countNew++;
                    }
                    if (s.getStatus() == Status.IN_PROGRESS) {
                        epic.setStatus(Status.IN_PROGRESS);
                        return;
                    }
                }
                if (countDone == epic.getEpicSubtasks().size()) {
                    epic.setStatus(Status.DONE);
                } else if (countNew == epic.getEpicSubtasks().size()) {
                    epic.setStatus(Status.NEW);
                } else {
                    epic.setStatus(Status.IN_PROGRESS);
                }
            }
        }
    }


    @Override
    public Subtask createSubtask(Subtask subtask) {
        if (isIntersect(subtask)) {
            throw new IllegalArgumentException(subtask.getName() + " пересекается с другой задачей");
        }
        if (Objects.nonNull(subtask.getId())) {
            subtasks.put(subtask.getId(), subtask);
            Epic epic = epics.get(subtask.getEpicId());
            epic.getEpicSubtasks().add(subtask);
            updateEpic(epic);
            epic.updateEpicTime();
            addToPrioritizedTasks(subtask);
        } else {
            int newId = generateId();
            subtask.setId(newId);
            subtasks.put(subtask.getId(), subtask);
            Epic epic = epics.get(subtask.getEpicId());
            epic.getEpicSubtasks().add(subtask);
            updateEpic(epic);
            epic.updateEpicTime();
            addToPrioritizedTasks(subtask);
            createEpic(epic);
        }
        return subtask;
    }


    @Override
    public Subtask updateSubtask(Subtask subtask) {
        if (!Objects.nonNull(subtask.getId())) {
            return subtask;
        }
        if (!subtasks.containsKey(subtask.getId())) {
            return subtask;
        }
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        updateEpic(epic);
        epic.updateEpicTime();
        return subtask;
    }


    @Override
    public void deleteTask(int id) {
        if (tasks.get(id) != null) {
            tasks.remove(id);
            historyManager.remove(id);
        }
    }


    @Override
    public void deleteEpic(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            for (Subtask s : epic.getEpicSubtasks()) {
                subtasks.remove(s.getId());
            }
            epic.getEpicSubtasks().clear();
            epics.remove(id);
            historyManager.remove(id);
        }
    }


    @Override
    public void deleteSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            Epic epic = epics.get(subtask.getEpicId());
            epic.getEpicSubtasks().remove(subtasks.get(id));
            updateEpic(epic);
            subtasks.remove(id);
            historyManager.remove(id);
            epic.updateEpicTime();
        }
    }


    @Override
    public void deleteAllTasks() {
        tasks.clear();
    }


    @Override
    public void deleteAllEpics() {
        epics.clear();
        subtasks.clear();
    }


    @Override
    public void deleteAllSubtasks() {
        subtasks.clear();
        epics.values()
                .forEach(epic -> {
                    epic.getEpicSubtasks().clear();
                    updateEpic(epic);
                });
    }


    @Override
    public Task getTask(int id) {
        Task task = tasks.get(id);
        historyManager.addTask(task);
        return task;
    }


    @Override
    public Epic getEpic(int id) {
        Epic epic = epics.get(id);
        historyManager.addTask(epic);
        return epic;
    }


    @Override
    public Subtask getSubTask(int id) {
        Subtask subtask = subtasks.get(id);
        historyManager.addTask(subtask);
        return subtask;
    }


    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }


    @Override
    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }


    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }


    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public TreeSet<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }
}

