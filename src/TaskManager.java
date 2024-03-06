import java.util.HashMap;
import java.util.List;

public interface TaskManager {
    HashMap<Integer, Task> getTasks();
    HashMap<Integer, Epic> getEpics();
    HashMap<Integer, Subtask> getSubtasks();
    void removeAllEpics();

    void removeAllSubtasks();

    Task getTaskById(int id);

    Epic getEpicById(int id);

    Subtask getSubtaskById(int id);

    void createTask(Task task);

    void createEpic(Epic epic);

    void createSubtask(Subtask subtask);

    void updateTask(Task task);

    void updateSubtask(Subtask subtask);

    void updateEpic(Epic epic);

    void removeTaskById(int id);

    void removeEpicById(int id);

    void removeSubtaskById(int id);

    List<Subtask> getTasksFromEpicById(int id);

    List<Task> getHistory();


}
