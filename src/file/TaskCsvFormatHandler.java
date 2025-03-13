package file;

import tasks.*;

public class TaskCsvFormatHandler {
    public static final String DELIMITER = ",";
    public static final String HEADER = "id,type,name,status,description,epic";

    public static String getHeader() {
        return HEADER;
    }

    public static String toString(Task task) {
        return task.getId() + DELIMITER + task.getType() + DELIMITER + task.getName() + DELIMITER + task.getStatus()
                + DELIMITER + task.getDescription();
    }

    public static String toString(Epic epic) {
        return epic.getId() + DELIMITER + epic.getType() + DELIMITER + epic.getName() + DELIMITER + epic.getStatus()
                + DELIMITER + epic.getDescription();
    }

    public static String toString(Subtask subtask) {
        return subtask.getId() + DELIMITER + subtask.getType() + DELIMITER + subtask.getName() + DELIMITER
                + subtask.getStatus() + DELIMITER + subtask.getDescription() + DELIMITER + subtask.getEpicId();
    }

    public static Task fromString(String value) {
        String[] parts = value.split(DELIMITER);
        int id = Integer.parseInt(parts[0]);
        Type type = Type.valueOf(parts[1]);
        String name = parts[2];
        Status status = Status.valueOf(parts[3]);
        String description = parts[4];

        switch (type) {
            case TASK:
                return new Task(id, type, name, description, status);
            case EPIC:
                return new Epic(id, type, name, description, status);
            case SUBTASK:
                int epicId = Integer.parseInt(parts[5]);
                return new Subtask(id, type, name, description, status, epicId);
            default:
                throw new IllegalArgumentException("Неправильный тип задачи: " + type);
        }
    }
}
