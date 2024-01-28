import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    public static int counter = 0;
    HashMap<Integer, Object> tasks;
    public TaskManager() {
        tasks = new HashMap<>();
    }

    public HashMap<Integer, Object> getTasks() {
        return tasks;
    }

    public void removeAllTasks() {
        tasks.clear();
    }

    public Object getTaskById(int id) {
        return tasks.get(id);
    }

    public void createTask(Task task) {
        task.setId(++counter);
        tasks.put(counter, task);
    }

    public void createEpic(Epic epic) {
        epic.setId(++counter);
        tasks.put(counter, epic);
    }

    public void createSubtask(Subtask subtask) {
        subtask.setId(++counter);
        tasks.put(counter, subtask);
        Epic masterEpic = (Epic) getTaskById(((Subtask) subtask).masterId);
        masterEpic.putSubtask(subtask);
    }

    public void updateTask(Object updatableTask) {
        if (updatableTask.getClass() == Task.class) {
            Task task = (Task) updatableTask;
            int id = task.getId();
            tasks.put(id, updatableTask);
        } else if (updatableTask.getClass() == Subtask.class) {
            Subtask task = (Subtask) updatableTask;
            int id = task.getId();
            tasks.put(id, updatableTask);
            Epic masterEpic = (Epic) getTaskById(task.masterId);
            masterEpic.subtasks.set(masterEpic.indexById.get(id), task);
            masterEpic.checkAndChangeStatus();
        } else if (updatableTask.getClass() == Epic.class) {
            Epic task = (Epic) updatableTask;
            int id = task.getId();
            tasks.put(id, updatableTask);
        }

    }

    public void removeById (int id) {
        if (tasks.get(id).getClass() == Task.class || tasks.get(id).getClass() == Epic.class) {
            tasks.remove(id);
        } else {
            Subtask removable = (Subtask) tasks.get(id);
            int masterId = removable.getMasterId();
            Epic master = (Epic) tasks.get(masterId);
            master.subtasks.remove(removable);
            master.indexById.remove(removable.getId());
            tasks.remove(id);
        }
    }

    public ArrayList<Subtask> getTasksFromEpicById(int id) {
        Epic epic = (Epic) tasks.get(id);
        return epic.subtasks;
    }

}