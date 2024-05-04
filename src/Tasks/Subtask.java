package Tasks;

import Tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.StringJoiner;

public class Subtask extends Task {
    private final int masterId;
    public Subtask(String name, String description, int masterId) {
        super(name, description);
        this.masterId = masterId;
    }

    public Subtask(String name, String description, int masterId, TaskStatus status, int id) {
        super(name, description);
        this.masterId = masterId;
        this.status = status;
        this.id = id;
    }

    public Subtask(String name, String description, int masterId, LocalDateTime startTime, Duration duration) {
        this(name, description, masterId);
        this.startTime = startTime;
        this.duration = duration;
    }

    public Subtask(String name, String description, int masterId, TaskStatus status, int id, LocalDateTime startTime, Duration duration) {
        this(name, description, masterId, startTime, duration);
        this.id = id;
        this.status = status;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public int getMasterId() {
        return masterId;
    }

    @Override
    public String toString() {
        StringJoiner stringJoiner = new StringJoiner(",");
        stringJoiner
                .add(TaskTypes.SUBTASK.name())
                .add(String.valueOf(id))
                .add(name)
                .add(status.name())
                .add(description)
                .add(String.valueOf(masterId))
                .add(startTime.toString())
                .add(duration.toString());
        return stringJoiner.toString();
    }
}