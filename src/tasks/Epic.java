package tasks;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    private  List<Integer> subtaskIds = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description, Status.NEW);
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
