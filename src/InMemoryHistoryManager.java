import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Node> history;
    private int firstId = -1;
    private int lastId = -1;

    public InMemoryHistoryManager() {
        history = new HashMap<>();
    }

    public void linkLast(Node node) {
        int id = node.data.getId();
        if (history.containsKey(id)) {
            Node nodeForRemove = history.get(id);
            removeNode(nodeForRemove);
        }

        if (history.isEmpty() || firstId == -1) {
            history.put(id, node);
            firstId = id;
            lastId = id;
            return;
        }

        history.put(id, node);
        Node prevNode = history.get(lastId);
        prevNode.next = node;
        node.prev = prevNode;
        lastId = id;
    }

    public void removeNode(Node node) {
        if (node.prev == null && node.next == null) {
            firstId = -1;
            lastId = -1;
            return;
        }
        if (node.next == null) {
            Node prevNode = node.prev;
            prevNode.next = null;
            lastId = prevNode.data.getId();
            return;
        }
        if (node.prev == null) {
            Node nextNode = node.next;
            nextNode.prev = null;
            firstId = nextNode.data.getId();
            return;
        }
        Node prevNode = node.prev;
        Node nextNode = node.next;
        prevNode.next = nextNode;
        nextNode.prev = prevNode;
    }

        public List<Task> getTasks () {
            List<Task> tasks = new ArrayList<>();

            Node current = history.get(firstId);
            while (current != null) {
                tasks.add(current.data);
                current = current.next;
            }

            return tasks;
        }

        @Override
        public void add (Task task){
            Node node = new Node(task);
            linkLast(node);
        }

        @Override
        public List<Task> getHistory () {
            return getTasks();
        }

        @Override
        public void remove ( int id){
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
