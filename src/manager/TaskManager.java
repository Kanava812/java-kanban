package manager;

import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.*;

public class TaskManager {

    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();

    private int generatedId = 0;

    private int generateId() {
        return ++generatedId;
    }


    public Task createTask(Task task) {
        if (Objects.nonNull(task.getId())) {
            return null;
        }
        int newId = generateId();
        task.setId(newId);
        tasks.put(task.getId(), task);
        return task;
    }


    public Task updateTask(Task task) {
        if (!Objects.nonNull(task.getId())) {
            return task;
        }
        if (!tasks.containsKey(task.getId())) {
            return task;
        }
        tasks.put(task.getId(), task);
        return task;
    }


    public Epic createEpic(Epic epic) {
        if (Objects.nonNull(epic.getId())) {
            return null;
        }
        int newId = generateId();
        epic.setId(newId);
        epics.put(epic.getId(), epic);
        return epic;
    }


    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            if (epic.getSubtaskIds().isEmpty()) {
                epic.setStatus(Status.NEW);
            } else {
                int countDone = 0;
                int countNew = 0;

                for (int num : epic.getSubtaskIds()) {
                    Subtask s = subtasks.get(num);
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
                if (countDone == epic.getSubtaskIds().size()) {
                    epic.setStatus(Status.DONE);
                } else if (countNew == epic.getSubtaskIds().size()) {
                    epic.setStatus(Status.NEW);
                } else {
                    epic.setStatus(Status.IN_PROGRESS);
                }
            }
        }
    }


    public Subtask createSubtask(Subtask subtask) {
        if (Objects.nonNull(subtask.getId())) {
            return null;
        }
        int newId = generateId();
        subtask.setId(newId);
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        epic.getSubtaskIds().add(newId);
        updateEpic(epic);
        return subtask;

    }


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
        return subtask;
    }


    public void deleteTask(int id) {
        if (tasks.get(id) != null) {
            tasks.remove(id);
        }
    }


    public void deleteEpic(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            for (int num : epic.getSubtaskIds()) {
                Subtask value = subtasks.get(num);
                subtasks.remove(value.getId());
            }
            epics.remove(id);
        }
    }


    public void deleteSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            Epic epic = epics.get(subtask.getEpicId());
            epic.getSubtaskIds().remove(id);
            updateEpic(epic);
            subtasks.remove(id);
        }
    }


    public void deleteAllTasks() {
        tasks.clear();
    }


    public void deleteAllEpics() {
        epics.clear();
        subtasks.clear();
    }


    public void deleteAllSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubtaskIds().clear();
            updateEpic(epic);
        }
    }


    public Task getTask(int id) {
        return tasks.get(id);
    }


    public Epic getEpic(int id) {
        return epics.get(id);
    }


    public Subtask getSubTask(int id) {
        return subtasks.get(id);
    }


    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    public List<Integer> getSubTasksByEpicId(int id) {
        return epics.get(id).getSubtaskIds();
    }
}

