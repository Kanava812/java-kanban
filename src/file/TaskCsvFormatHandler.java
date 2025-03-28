package file;

import tasks.*;

import java.time.Duration;
import java.time.LocalDateTime;

public class TaskCsvFormatHandler {
    public static final String DELIMITER = ",";
    public static final String HEADER = "id,type,name,status,description,start time,duration,epic";

    public static String getHeader() {
        return HEADER;
    }

    public static String toString(Task task) {
        return task.getId() + DELIMITER + task.getType() + DELIMITER + task.getName() + DELIMITER + task.getStatus()
                + DELIMITER + task.getDescription()+ DELIMITER +task.getStartTime() + DELIMITER
                + task.getDuration().toMinutes()+ DELIMITER
                + (task instanceof Subtask ? ((Subtask) task).getEpicId() : "");
    }


    public static Task fromString(String value) {
        String[] parts = value.split(DELIMITER);
        int id = Integer.parseInt(parts[0]);
        Type type = Type.valueOf(parts[1]);
        String name = parts[2];
        Status status = Status.valueOf(parts[3]);
        String description = parts[4];
        LocalDateTime startTime = LocalDateTime.parse(parts[5]);
        Duration duration = Duration.ofMinutes(Long.parseLong(parts[6]));
        switch (type) {
            case TASK:
                return new Task(id, type, name, description, status, startTime, duration);
            case EPIC:
                return new Epic(id, type, name, description, status, startTime, duration);
            case SUBTASK:
                int epicId = Integer.parseInt(parts[7]);
                return new Subtask(id, type, name, description, status, startTime, duration, epicId);
            default:
                throw new IllegalArgumentException("Неправильный тип задачи: " + type);
        }
    }
}
