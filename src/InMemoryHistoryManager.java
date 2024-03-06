import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements History {
    private final LinkedList<Task> history;

    public InMemoryHistoryManager() {
        history = new LinkedList<>();
    }

    @Override
    public void addTask(Task task) {
        if (history.size() > 9) {
            history.removeFirst();
        }
        history.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return history;
    }
}
