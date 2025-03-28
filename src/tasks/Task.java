package tasks;

import java.time.Duration;
import java.util.Objects;
import java.time.LocalDateTime;

public  class Task {

    private final String name;
    private String description;
    private Integer id;
    private Status status;
    protected Type type;
    protected LocalDateTime startTime;
    protected Duration duration;

    public Task(String name, String description, Status status, LocalDateTime startTime, Duration duration) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.type = Type.TASK;
        this.startTime = startTime;
        this.duration = duration;
    }


    public Task(Integer id, String name, String description, Status status, LocalDateTime startTime, Duration duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
    }


    public Task(Integer id, Type type, String name, String description, Status status, LocalDateTime startTime,

                Duration duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.type = type;
        this.startTime = startTime;
        this.duration = duration;
    }


    public Task(String name, String description) {
        this.name = name;
        this.description = description;
    }


    public Task(String name, String description, Status status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }


    public String getName() {
        return name;
    }


    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }


    public Integer getId() {
        return id;
    }


    public void setId(Integer id) {
        this.id = id;
    }


    public Status getStatus() {
        return status;
    }


    public void setStatus(Status status) {
        this.status = status;
    }


    public Type getType() {
        return type;
    }


    public LocalDateTime getStartTime() {
        return startTime;
    }


    public Duration getDuration() {
        return duration;
    }


    public void setDuration(Duration duration) {
        this.duration = duration;
    }


    public LocalDateTime getEndTime() {
        if (startTime == null) return null;
        return startTime.plus(duration);
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id);
    }


    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


    @Override
    public String toString() {
        return "Task{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", startTime=" + getStartTime() +
                ", duration=" + getDuration() +
                ", endTime=" + getEndTime() +
                '}';
    }
}
