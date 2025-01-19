package tasks;

import java.util.Objects;

public  class Task {

    private String name;
    private String description;
    private Integer id;
    private Status status;

    public Task(String name, String description,Status status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public   String getName() { return name; }

    public String getDescription() {
        return description;
    }

    public Integer getId() { return id; }

    public void setId(Integer id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status= " + status +
                '}';
    }
}
