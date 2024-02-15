import java.util.List;

public interface History {
    void addTask(Task task);

    List<Task> getHistory();
}
