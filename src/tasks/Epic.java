package tasks;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    private  List<Integer> subtaskIds = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description, Status.NEW);
    }

    public Epic(Integer id, String name, String description, Status status,List<Integer>subtaskIds) {
        super(id,name, description, status);
        this.subtaskIds=subtaskIds;
    }

    @Override
    public Epic getShapshot(){
        return new Epic(this.getId(),this.getName(),this.getDescription(),this.getStatus(),this.getSubtaskIds());
    }

    public List<Integer> getSubtaskIds() {
        return subtaskIds;
    }
    @Override
    public String toString() {
        return "Epic{" +
                "id="+getId()+
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status="+getStatus()+
                ", subtaskIds=" + subtaskIds +
                '}';
    }
}
