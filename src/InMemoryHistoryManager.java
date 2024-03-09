import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Node> history;
    //    private final List<Task> listOfTasks;
    private int firstId = -1;
    private int lastId = -1;

    public InMemoryHistoryManager() {
//        listOfTasks = new ArrayList<>();
        history = new HashMap<>();
    }

    public void linkLast(Node node) {
        int id = node.data.getId();
        if (history.isEmpty()) {
            history.put(id, node);
            firstId = id;
        } else {
            Node prevNode = history.get(lastId);
            if (history.containsKey(id)) {
                Node nodeForRemove = history.get(id);
                removeNode(nodeForRemove);
            }
            history.put(id, node);
            prevNode.next = node;
            node.prev = prevNode;

        }
        lastId = id;
    }

    public void removeNode(Node node) {
        if (!history.containsValue(node)) return;
        if (node.next == null) {
            Node prevNode = node.prev;
            prevNode.next = null;
            history.remove(node.data.getId());
            lastId = prevNode.data.getId();
            return;
        } else if (node.prev == null) {
            Node nextNode = node.next;
            nextNode.prev = null;
            history.remove(node.data.getId());
            firstId = nextNode.data.getId();
            return;
        }
        Node prevNode = node.prev;
        Node nextNode = node.next;
        prevNode.next = nextNode;
        nextNode.prev = prevNode;
        history.remove(node.data.getId());

    }

    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();

        Node current = history.get(firstId);
        while (current != null) {
            tasks.add(current.data);
            current = current.next;
        }

        return tasks;
    }

    @Override
    public void add(Task task) {
        Node node = new Node(task);
        linkLast(node);
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void remove(int id) {
        Node nodeToRemove = history.get(id);
        removeNode(nodeToRemove);
    }

    private class Node {

        private Node prev;
        private Node next;
        private Task data;

        public Node(Task task) {
            this.prev = null;
            this.next = null;
            this.data = task;
        }
    }
}
