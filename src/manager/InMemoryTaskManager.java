package manager;

import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();

    private final HistoryManager historyManager = Managers.getDefaultHistoryManager();
    private int generatedId = 0;

    private int generateId() {
        return ++generatedId;
    }


    @Override
    public Task createTask(Task task) {
        if (Objects.nonNull(task.getId())) {
            return null;
        }
        int newId = generateId();
        task.setId(newId);
        tasks.put(task.getId(), task);
        return task;
    }


    @Override
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


    @Override
    public Epic createEpic(Epic epic) {
        if (Objects.nonNull(epic.getId())) {
            return null;
        }
        int newId = generateId();
        epic.setId(newId);
        epics.put(epic.getId(), epic);
        return epic;
    }


    @Override
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


    @Override
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
        return subtask;
    }


    @Override
    public void deleteTask(int id) {
        if (tasks.get(id) != null) {
            tasks.remove(id);
        }
    }


    @Override
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


    @Override
    public void deleteSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
           Epic epic = epics.get(subtask.getEpicId());
           epic.getSubtaskIds().remove(subtasks.get(id));
           updateEpic(epic);
           subtasks.remove(id);
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
        for (Epic epic : epics.values()) {
            epic.getSubtaskIds().clear();
            updateEpic(epic);
        }
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
    public List<Subtask> getSubTasksByEpic(Epic epic) {
        List<Subtask> subtaskbyepiclist = new ArrayList<>();
        for (int s : epic.getSubtaskIds()){
          Subtask subtask = subtasks.get(s);
          if (subtask != null) {
              subtaskbyepiclist.add(subtask);
          }
        }
        return subtaskbyepiclist;
    }


    @Override
    public List<Task>getHistory(){
        return historyManager.getHistory();
    }


}

