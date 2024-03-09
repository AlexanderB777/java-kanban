import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Task> history;

    public InMemoryHistoryManager() {
        history = new LinkedHashMap<>();
    }

    @Override
    public void addTask(Task task) {
        int id = task.getId();
        history.remove(id);
        history.put(id, task);
    }

    @Override
    public List<Task> getHistory() {

        return new ArrayList<>(history.values());
    }

    @Override
    public void removeTask(int id) {
        history.remove(id);
    }

}
