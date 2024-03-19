public class Subtask extends Task {
    public final int masterId;
    public Subtask(String name, String description, int masterId) {
        super(name, description);
        this.masterId = masterId;
    }

    public Subtask(String name, String description, int masterId, TaskStatus status) {
        super(name, description);
        this.masterId = masterId;
        this.status = status;
    }

    public Subtask(String name, String description, int masterId, TaskStatus status, int id) {
        super(name, description);
        this.masterId = masterId;
        this.status = status;
        this.id = id;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public int getMasterId() {
        return masterId;
    }

    @Override
    public String toString () {
        return TaskTypes.SUBTASK + "," + id + "," + name + "," + status + "," + description + "," + masterId;
    }
}