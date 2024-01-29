import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TaskManager {
    public static int counter = 0;
    HashMap<Integer, Task> tasks;
    HashMap<Integer, Epic> epics;
    HashMap<Integer, Subtask> subtasks;

    public TaskManager() {
        epics = new HashMap<>();
        subtasks = new HashMap<>();
        tasks = new HashMap<>();
    }

    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    public void removeAllTasks() {
        tasks.clear();
    }

    public void removeAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    public void removeAllSubtasks() {
        subtasks.clear();
        for (Map.Entry<Integer, Epic> entry : epics.entrySet()) {
            Epic epic = entry.getValue();
            epic.checkAndChangeStatus();
        }
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public Subtask getSubTaskById(int id) {
        return subtasks.get(id);
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

    public void updateTask(Task task) {
        int id = task.getId();
        tasks.replace(id, task);
    }

    public void updateSubtask(Subtask subtask) {
        int id = subtask.getId();
        subtasks.replace(id, subtask);
        Epic masterEpic = getEpicById(subtask.masterId);
        masterEpic.subtasks.set(masterEpic.indexById.get(id), subtask);
        masterEpic.checkAndChangeStatus();
    }

    public void updateEpic(Epic epic) {
        int id = epic.getId();
        epics.replace(id, epic);
    }

    public void removeTaskById(int id) {
        tasks.remove(id);
    }

    public void removeEpicById(int id) {
        Epic epic = epics.get(id);
        for (Map.Entry<Integer, Integer> entry : epic.indexById.entrySet()) {
            int subtaskId = entry.getKey();
            subtasks.remove(subtaskId);
        }
        epics.remove(id);
    }

    public void removeSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        Epic masterEpic = epics.get(subtask.masterId);
        masterEpic.subtasks.remove(subtask);
        masterEpic.indexById.remove(id);
        subtasks.remove(id);
    }


    public ArrayList<Subtask> getTasksFromEpicById(int id) {
        Epic epic = (Epic) tasks.get(id);
        return epic.subtasks;
    }

}