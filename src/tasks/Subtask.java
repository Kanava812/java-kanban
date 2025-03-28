package tasks;
import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {

    private final Integer epicId;

    public Subtask(String name, String description, Status status, LocalDateTime startTime, Duration duration, int epicId) {
        super(name, description, status, startTime, duration);
        this.epicId = epicId;
        this.type = Type.SUBTASK;
    }


    public Subtask(int id, Type type, String name, String description, Status status, LocalDateTime startTime, Duration duration, int epicId) {
        super(id, type,name, description, status, startTime, duration);
        this.epicId = epicId;
    }


    public Integer getEpicId() {
        return epicId;
    }


    @Override
    public String toString() {
        return "Subtask{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", startTime=" + getStartTime() +
                ", duration=" + getDuration() +
                ", endTime=" + getEndTime() +
                ", epicId=" + getEpicId() +
                "}";
    }
}
