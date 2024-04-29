package Tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringJoiner;

public class Epic extends Task {
    public ArrayList<Subtask> subtasks;
    public HashMap<Integer, Integer> indexById;
    private LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description);
        subtasks = new ArrayList<>();
        indexById = new HashMap<>();
        duration = Duration.ZERO;
    }

    public Epic(String name, String description, TaskStatus status) {
        this(name, description);
        this.status = status;
    }

    public Epic(String name, String description, TaskStatus status, int id) {
        this(name, description, status);
        this.id = id;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
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
        if (startTime == null || subtask.startTime.isBefore(startTime)) {
            startTime = subtask.startTime;
        }
        duration = duration.plus(subtask.duration);
        if (endTime == null || subtask.getEndTime().isAfter(endTime)) {
            endTime = subtask.getEndTime();
        }
        subtasks.add(subtask);
        indexById.put(subtask.getId(), subtasks.indexOf(subtask));
        checkAndChangeStatus();
    }

    @Override
    public String toString() {
        StringJoiner stringJoiner = new StringJoiner(",");
        stringJoiner
                .add(TaskTypes.EPIC.name())
                .add(String.valueOf(id))
                .add(name)
                .add(status.name())
                .add(description);
        return stringJoiner.toString();
    }

}