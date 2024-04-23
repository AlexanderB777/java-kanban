import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class Task {
    protected int id;
    protected String name;
    protected String description;
    protected TaskStatus status;
    protected Duration duration;
    protected LocalDateTime startTime;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        status = TaskStatus.NEW;
    }

    public Task(String name, String description, TaskStatus status) {
        this(name, description);
        this.status = status;
    }

    public Task(String name, String description, TaskStatus status, int id) {
        this(name, description, status);
        this.id = id;

    }

    public Task(String name, String description, LocalDateTime startTime, Duration duration) {
        this(name, description);
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(String name, String description, TaskStatus status, int id, LocalDateTime startTime, Duration duration) {
        this(name, description, status, id);
        this.startTime = startTime;
        this.duration = duration;
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        StringJoiner stringJoiner = new StringJoiner(",");
        stringJoiner
                .add(TaskTypes.TASK.name())
                .add(String.valueOf(id))
                .add(name)
                .add(status.name())
                .add(description)
                .add(startTime.toString())
                .add(duration.toString());
        return stringJoiner.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Task task = (Task) obj;
        return Objects.equals(this.name, task.name) && Objects.equals(this.description, task.description) &&
                Objects.equals(this.id, task.id) && Objects.equals(this.status, task.status);
    }

    @Override
    public int hashCode() {
        int hash = 13;
        if (name != null) hash += name.hashCode();
        hash *= 31;
        if (description != null) hash += description.hashCode();
        hash *= 31;
        hash += hash * id;
        if (status != null) hash += status.hashCode();
        return hash;
    }

//    @Override
//    public int compareTo(Task o) {
//        if (this.startTime.isBefore(o.startTime)) {
//            return 1;
//        }
//        if (this.startTime.isAfter(o.startTime)) {
//            return -1;
//        }
//        return 0;
//    }
}