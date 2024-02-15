import java.util.Objects;

public class Task {
    protected int id;
    protected String name;
    protected String description;
    protected TaskStatus status;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        status = TaskStatus.NEW;
    }

    public Task(String name, String description, TaskStatus status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Task(String name, String description, TaskStatus status, int id) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.id = id;
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
    public String toString () {
        return "Task{" + "name='" + name + "'" + ", description.length= " + description.length() + ", id= " +
                id + ", status= " + status + "}";
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
}