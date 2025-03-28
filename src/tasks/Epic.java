package tasks;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.util.Objects;


public class Epic extends Task {

    private final List<Subtask> epicSubtasks = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(int id, Type type, String name, String description, Status status, LocalDateTime startTime,
                Duration duration) {
        super(id, type, name, description, status, startTime, duration);
    }


    public Epic(String name, String description) {
        super(name, description, Status.NEW);
        this.type = Type.EPIC;
    }


    public void updateEpicTime() {
        if (epicSubtasks.isEmpty()) {
            this.duration = Duration.ZERO;
            this.startTime = null;
            endTime = null;
            return;
        }
        this.startTime = getStartTime();
        endTime = getEndTime();
        this.duration = getDuration();
    }


    public Duration getDuration() {
        return epicSubtasks.stream()
                .filter(Objects::nonNull)
                .map(Subtask::getDuration)
                .reduce(Duration.ZERO, Duration::plus);
    }


    public LocalDateTime getStartTime() {
        return epicSubtasks.stream()
                .filter(Objects::nonNull)
                .map(Subtask::getStartTime)
                .min(LocalDateTime::compareTo)
                .orElse(null);
    }


    public List<Integer> getSubtaskIds() {
        List<Integer> subtaskIds = new ArrayList<>();
        for (Subtask s : epicSubtasks) {
            subtaskIds.add(getSubtaskId(s));
        }
        return subtaskIds;
    }


    public List<Subtask> getEpicSubtasks() {
        return epicSubtasks;
    }


    public Integer getSubtaskId(Subtask subtask) {
        return subtask.getId();
    }


    public LocalDateTime getEndTime() {
        return epicSubtasks.stream()
                .filter(Objects::nonNull)
                .map(Subtask::getEndTime)
                .max(LocalDateTime::compareTo)
                .orElse(null);
    }


    @Override
    public String toString() {
        return "Epic{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", startTime=" + getStartTime() +
                ", duration=" + getDuration() +
                ", endTime=" + getEndTime() +
                ", subtaskIds=" + getSubtaskIds() +
                '}';
    }
}

