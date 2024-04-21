import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    public static int counter;
    HashMap<Integer, Task> tasks;
    HashMap<Integer, Epic> epics;
    HashMap<Integer, Subtask> subtasks;
    HistoryManager historyManager;
    TreeSet<Task> prioritizedTasks;
    TaskByStartTimeComparator comp = new TaskByStartTimeComparator();

    public InMemoryTaskManager() throws Exception {
        counter = 0;
        epics = new HashMap<>();
        subtasks = new HashMap<>();
        tasks = new HashMap<>();
        historyManager = Managers.getDefaultHistory();
        prioritizedTasks = new TreeSet<>(comp);
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

    @Override
    public void removeAllTasks() {
        tasks.clear();
    }

    @Override
    public void removeAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void removeAllSubtasks() {
        subtasks.clear();
        for (Map.Entry<Integer, Epic> entry : epics.entrySet()) {
            Epic epic = entry.getValue();
            epic.checkAndChangeStatus();
        }
    }

    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        historyManager.add(task);
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic task = epics.get(id);
        historyManager.add(task);
        return task;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask task = subtasks.get(id);
        historyManager.add(task);
        return subtasks.get(id);
    }

    @Override
    public void createTask(Task task) {
        List<Task> resultOfChecking = getPrioritizedTasks()
                .stream()
                .filter(task1 -> !(task.startTime.isAfter(task1.getEndTime()) || task1.startTime.isAfter(task.getEndTime())))
                .toList();
        if (!resultOfChecking.isEmpty()) return;
        task.setId(++counter);
        tasks.put(counter, task);
        if (task.startTime != null) addToPrioritized(task);
    }

    @Override
    public void createEpic(Epic epic) {
        epic.setId(++counter);
        epics.put(counter, epic);
    }

    @Override
    public void createSubtask(Subtask subtask) {
        List<Task> resultOfChecking = getPrioritizedTasks()
                .stream()
                .filter(task1 -> !(subtask.startTime.isAfter(task1.getEndTime()) || task1.startTime.isAfter(subtask.getEndTime())))
                .toList();
        if (!resultOfChecking.isEmpty()) return;
        subtask.setId(++counter);
        subtasks.put(counter, subtask);
        Epic masterEpic = epics.get(subtask.getMasterId());
        masterEpic.putSubtask(subtask);
        if (subtask.startTime != null) {
            addToPrioritized(subtask);
            addToPrioritized(masterEpic);
        }
    }

    @Override
    public void updateTask(Task task) {
        int id = task.getId();
        tasks.replace(id, task);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        int id = subtask.getId();
        subtasks.replace(id, subtask);
        Epic masterEpic = getEpicById(subtask.masterId);
        masterEpic.subtasks.set(masterEpic.indexById.get(id), subtask);
        masterEpic.checkAndChangeStatus();
    }

    @Override
    public void updateEpic(Epic epic) {
        int id = epic.getId();
        epics.replace(id, epic);
    }

    @Override
    public void removeTaskById(int id) {
        tasks.remove(id);
    }

    @Override
    public void removeEpicById(int id) {
        Epic epic = epics.get(id);
        for (Map.Entry<Integer, Integer> entry : epic.indexById.entrySet()) {
            int subtaskId = entry.getKey();
            subtasks.remove(subtaskId);
        }
        epics.remove(id);
    }

    @Override
    public void removeSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        Epic masterEpic = epics.get(subtask.masterId);
        masterEpic.subtasks.remove(subtask);
        masterEpic.indexById.remove(id);
        subtasks.remove(id);
    }

    @Override
    public ArrayList<Subtask> getTasksFromEpicById(int id) {
        Epic epic = (Epic) tasks.get(id);
        return epic.subtasks;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return prioritizedTasks.stream().toList();
    }

    @Override
    public void addToPrioritized(Task task) {
        prioritizedTasks.add(task);
    }
}

