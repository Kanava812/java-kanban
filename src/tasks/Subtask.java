package tasks;

public class Subtask extends Task{

    private final Integer epicId;

    public Subtask(String name, String description,Status status,int epicId) {
        super(name, description,status);
        this.epicId = epicId;
    }
    public Subtask(Integer id, String name, String description, Status status,Integer epicId) {
        super(id,name, description, status);
        this.epicId = epicId;
    }

    @Override
    public Subtask getShapshot(){
        return new Subtask(this.getId(),this.getName(),this.getDescription(),this.getStatus(), this.getEpicId());
    }
    public Integer getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "id="+getId()+
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status="+getStatus()+
                ", epicId="+epicId+
                "}";
    }
}
