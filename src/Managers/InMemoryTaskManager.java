package Managers;

import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;
import utils.SubtaskCreationException;
import utils.TaskInteractionException;

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
        epics.values().forEach(epic -> {
            epic.checkAndChangeStatus();
            epic.indexById.clear();
            epic.subtasks.clear();
        });
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
        if (task.getId() != null || task.getId() != 0) {
            throw new IllegalArgumentException("Task should not contain ID");
        }

        if (!getPrioritizedTasks()
                .stream()
                .allMatch(task1 -> task.startTime.isAfter(task1.getEndTime())
                        || task1.startTime.isAfter(task.getEndTime()))) {
            throw new TaskInteractionException(String.format("Task id=%s intercepts with another task!", task.getId()));
        }
        task.setId(++counter);
        tasks.put(counter, task);


        if (task.startTime != null) addToPrioritized(task);
    }


    @Override
    public void createEpic(Epic epic) {
        if (epic.getId() != null || epic.getId() != 0) {
            throw new IllegalArgumentException("Epic should not contain ID");
        }
        epic.setId(++counter);
        epics.put(counter, epic);
    }

    @Override
    public void createSubtask(Subtask subtask) {
        if (subtask.getId() != null) {
            throw new IllegalArgumentException("Task should not contain ID");
        }

        int masterId = subtask.getMasterId();
        if (!epics.containsKey(masterId)) {
            throw new SubtaskCreationException("Subtask couldn't be created without master ID");
        }

        if (!getPrioritizedTasks()
                .stream()
                .allMatch(task1 -> subtask.startTime.isAfter(task1.getEndTime())
                        || task1.startTime.isAfter(subtask.getEndTime()))) {
            throw new TaskInteractionException("Subtask has interactions");
        }
        subtask.setId(++counter);
        subtasks.put(counter, subtask);
        Epic masterEpic = epics.get(masterId);
        if (masterEpic == null) {
            System.out.println("Master epic of subtask with id " + subtask.getId() + " is null");
            return;
        }
        masterEpic.putSubtask(subtask);
        if (subtask.startTime != null) {
            addToPrioritized(subtask);
            addToPrioritized(masterEpic);
        }
    }

    @Override
    public void updateTask(Task task) {
        if (!getPrioritizedTasks()
                .stream()
                .allMatch(task1 -> task.startTime.isAfter(task1.getEndTime())
                        || task1.startTime.isAfter(task.getEndTime()))) {
            throw new TaskInteractionException("Subtask has interactions");
        }
        int id = task.getId();
        tasks.replace(id, task);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (!getPrioritizedTasks()
                .stream()
                .allMatch(task1 -> subtask.startTime.isAfter(task1.getEndTime())
                        || task1.startTime.isAfter(subtask.getEndTime()))) {
            throw new TaskInteractionException("Subtask has interactions");
        }
        int id = subtask.getId();
        subtasks.replace(id, subtask);
        Epic masterEpic = getEpicById(subtask.getMasterId());
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
        epic.indexById.keySet().forEach(subtaskId -> subtasks.remove(subtaskId));
        epics.remove(id);
    }

    @Override
    public void removeSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        Epic masterEpic = epics.get(subtask.getMasterId());
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

    @Override
    public List<Subtask> getEpicSubtasks(int epicId) {
        return epics.get(epicId).subtasks;
    }
}