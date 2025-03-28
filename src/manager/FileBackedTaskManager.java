package manager;

import file.ManagerSaveException;
import file.TaskCsvFormatHandler;
import tasks.*;

import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    protected void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(TaskCsvFormatHandler.getHeader() + "\n");
            String tasksString= tasks.values().stream()
                    .map(task -> TaskCsvFormatHandler.toString(task)  + "\n")
                    .collect(Collectors.joining());

            String epicsString= epics.values().stream()
                    .filter(epic -> epic.getStartTime() != null)
                    .map(epic -> TaskCsvFormatHandler.toString(epic)  + "\n")
                    .collect(Collectors.joining());

            String subtasksString= subtasks.values().stream()
                    .map(subtask -> TaskCsvFormatHandler.toString(subtask)  + "\n")
                    .collect(Collectors.joining());

            writer.write(tasksString+epicsString+subtasksString);
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения задач в файл.", e);
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        final FileBackedTaskManager manager = new FileBackedTaskManager(file);
        try {
            final String[] lines = Files.readString(file.toPath()).split("\n");
            int loadedId = 0;
            for (int i = 1; i < lines.length; i++) {
                String line = lines[i];
                Task task = TaskCsvFormatHandler.fromString(line);
                switch (task.getType()) {
                   case Type.TASK -> manager.createTask(task);
                   case Type.EPIC -> manager.createEpic((Epic) task);
                   case Type.SUBTASK -> manager.createSubtask((Subtask) task);
                }
                if (loadedId < task.getId()) {
                    loadedId = task.getId();
                }
            }
            manager.generatedId = loadedId;
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка загрузки данных из файла " + file.getName() + ".", e);
        }
        return manager;
    }

    @Override
    public Task createTask(Task task) {
        Task t = super.createTask(task);

        saveToFile();
        return t;
    }

    @Override
    public Epic createEpic(Epic epic) {
        Epic e = super.createEpic(epic);
        saveToFile();
        return e;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        Subtask s = super.createSubtask(subtask);
        saveToFile();
        return s;
    }

    @Override
    public Task updateTask(Task task) {
        Task t = super.updateTask(task);
        saveToFile();
        return t;
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        saveToFile();
    }

    @Override
    public Subtask updateSubtask(Subtask subtask) {
        Subtask s = super.updateSubtask(subtask);
        saveToFile();
        return s;
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        saveToFile();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        saveToFile();
    }

    @Override
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
        saveToFile();
    }

    @Override
    public List<Task> getHistory() {
        return super.getHistory();
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
        saveToFile();
    }


    @Override
    public void deleteAllEpics() {
        epics.clear();
        subtasks.clear();
        saveToFile();
    }


    @Override
    public void deleteAllSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubtaskIds().clear();
            updateEpic(epic);
        }
        saveToFile();
    }
}