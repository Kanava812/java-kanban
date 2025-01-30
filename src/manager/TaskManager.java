package manager;

import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public interface TaskManager {
    Task createTask(Task task);

    Task updateTask(Task task);


    Epic createEpic(Epic epic);

    void updateEpic(Epic epic);


    Subtask createSubtask(Subtask subtask);


    Subtask updateSubtask(Subtask subtask);


    void deleteTask(int id);

    void deleteEpic(int id);


    void deleteSubtask(int id);


    void deleteAllTasks();


    void deleteAllEpics();

    void deleteAllSubtasks();


    Task getTask(int id);


    Epic getEpic(int id);


    Subtask getSubTask(int id);


    ArrayList<Task> getTasks();

    ArrayList<Subtask> getSubtasks();

    ArrayList<Epic> getEpics();

    List<Subtask> getSubTasksByEpic(Epic epic);

    List<Task> getHistory();

}

