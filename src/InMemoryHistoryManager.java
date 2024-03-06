import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Task> history;
    private final Set<Integer> historyId; // Каталог идентификаторов задач, содержащихся в массиве history

    public InMemoryHistoryManager() {
        history = new LinkedHashMap<>();
        historyId = new HashSet<>();
    }

    @Override
    public void addTask(Task task) {
        int id = task.getId();
        if (historyId.contains(id)) {
            history.remove(id);
        } else {
            historyId.add(id);
        }

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
