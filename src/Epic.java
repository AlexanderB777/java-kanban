import java.util.ArrayList;
import java.util.HashMap;

public class Epic extends Task{
    public ArrayList<Subtask> subtasks;
    public HashMap<Integer, Integer> indexById;
    public Epic(String name, String description) {
        super(name, description);
        subtasks = new ArrayList<>();
        indexById = new HashMap<>();
    }

    public Epic(String name, String description, TaskStatus status) {
        super(name, description);
        subtasks = new ArrayList<>();
        indexById = new HashMap<>();
        this.status = status;
    }

    public Epic(String name, String description, TaskStatus status, int id) {
        super(name, description);
        subtasks = new ArrayList<>();
        indexById = new HashMap<>();
        this.status = status;
        this.id = id;
    }

    private void setStatus(TaskStatus status) {
        this.status = status;
    }

    public void checkAndChangeStatus() {
        if (subtasks.isEmpty()) { // Если у эпика нет задач, значит он NEW
            setStatus(TaskStatus.NEW);
            return;
        }
        TaskStatus firstStatus = subtasks.get(0).getStatus(); // Берем статус первого элемента
        if (firstStatus == TaskStatus.IN_PROGRESS) { //Если статус одной задачи IN_PROGRESS, то цикл не имеет смысла
            setStatus(TaskStatus.IN_PROGRESS);
            return;
        }
        for (Subtask subtask : subtasks) { // Если есть 2 задачи с разным статусом, то проверка так же не имеет смысла
                if (subtask.getStatus() != status) {
                    setStatus(TaskStatus.IN_PROGRESS);
                    return;
            }
        }
        setStatus(firstStatus); // Если цикл завершился, значит все задачи одного статуса, его и устанавливает для эпика
    }

    public void putSubtask (Subtask subtask) {
        subtasks.add(subtask);
        indexById.put(subtask.getId(), subtasks.indexOf(subtask));
        checkAndChangeStatus();
    }

    @Override
    public String toString () {
        return "Epic{" + "name='" + name + "'" + ", description.length= " + description.length() + ", id= " +
                id + ", status= " + status + ", subtasks= " + subtasks + "}";
    }
}