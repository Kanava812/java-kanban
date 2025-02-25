package manager;

import tasks.Task;

import java.util.*;


public class InMemoryHistoryManager implements HistoryManager {

    private final HashMap<Integer, Node> nodeMap = new HashMap<>();
    private Node first;
    private Node last;

    @Override
    public void addTask(Task task) {
        if (Objects.isNull(task)) return;
        if (nodeMap.containsKey(task.getId())) {
            remove(task.getId());
        }
        linkLast(task);
    }

    public void linkLast(Task task) {
        Node node = new Node(task);

        if (nodeMap.isEmpty()) {
            first = node;

        } else if (last == first) {
            node.prev = first;
            first.next = node;
        } else {
            last.next = node;
            node.prev = last;
        }
        last = node;
        nodeMap.put(task.getId(), node);

    }

    @Override
    public void remove(int id) {
        Node node = nodeMap.remove(id);
        removeNode(node);
    }

    public void removeNode(Node node) {
        if (Objects.isNull(node)) {
            return;
        }

        if (Objects.nonNull(node.prev)) {
            node.prev.next = node.next;
        } else {
            first = node.next;
        }
        if (Objects.nonNull(node.next)) {
            node.next.prev = node.prev;
        } else {
            last = node.prev;
        }
    }

    private List<Task> getTasks() {
        List<Task> result = new ArrayList<>();
        Node node = first;
        while (Objects.nonNull(node)) {
            result.add(node.task);
            node = node.next;

        }
        return result;
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }


    public static class Node {
        Task task;
        Node next;
        Node prev;

        public Node(Task task) {
            this.task = task;
        }
    }
}

