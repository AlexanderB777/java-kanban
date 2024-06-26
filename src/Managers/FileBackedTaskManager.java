package Managers;

import Tasks.*;
import utils.ComparatorById;
import utils.TaskInteractionException;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File file;
    FileBackedHistoryManager historyManager;

    public FileBackedTaskManager() throws Exception {
        super();
        historyManager = Managers.getFileBackedHistoryManager();
        file = new File("src/Managers/Task_storage.csv");
    }

    public void restore() {
        restoreFromList(getListFromFile(file));
    }

    public List<String> getListFromFile(File file) {
        List<String> fromFile = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while (br.ready()) {
                fromFile.add(br.readLine());
            }
            return fromFile;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void restoreFromList(List<String> list) {
        for (String line : list) {
            String[] fromLine = line.split(",");
            switch (fromLine[0]) {
                case ("TASK"):
                    createTaskWithId(new Task(fromLine[2],
                            fromLine[4],
                            TaskStatus.valueOf(fromLine[3]),
                            Integer.parseInt(fromLine[1]),
                            LocalDateTime.parse(fromLine[5]),
                            Duration.parse(fromLine[6])));
                    break;
                case ("SUBTASK"):
                    createSubtaskWithId(new Subtask(fromLine[2],
                            fromLine[4],
                            Integer.parseInt(fromLine[5]),
                            TaskStatus.valueOf(fromLine[3]),
                            Integer.parseInt(fromLine[1]),
                            LocalDateTime.parse(fromLine[6]),
                            Duration.parse(fromLine[7])));
                    break;
                case ("EPIC"):
                    createEpicWithId(new Epic(fromLine[2],
                            fromLine[4],
                            TaskStatus.valueOf(fromLine[3]),
                            Integer.parseInt(fromLine[1])));
                    break;
            }
        }
    }

    public void createTaskWithId(Task task) {
        if (task.getId() == null || task.getId() == 0) {
            throw new IllegalArgumentException("Task should have ID");
        }
        counter = task.getId();
        tasks.put(counter, task);
        if (task.startTime != null) addToPrioritized(task);
        save();
    }

    public void createSubtaskWithId(Subtask subtask) {
        if (subtask.getId() == null || subtask.getId() == 0) {
            throw new IllegalArgumentException("Task should have ID");
        }
        if (!getPrioritizedTasks()
                .stream()
                .allMatch(task1 -> subtask.startTime.isAfter(task1.getEndTime())
                        || task1.startTime.isAfter(subtask.getEndTime()))) return;
        subtask.setId(++counter);
        subtasks.put(counter, subtask);
        Epic masterEpic = epics.get(subtask.getMasterId());
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

    public void createEpicWithId(Epic epic) {
        if (epic.getId() == null || epic.getId() == 0) {
            throw new IllegalArgumentException("Task should have ID");
        }
        epic.setId(++counter);
        epics.put(counter, epic);
    }


    public void save() {
        List<String> allTasks = getAllTasksForFile();
        writeListToFile(allTasks, file);

    }

    public List<String> getAllTasksForFile() {
        List<Task> listWithAllTasks = new ArrayList<>();
        listWithAllTasks.addAll(tasks.values());
        listWithAllTasks.addAll(epics.values());
        listWithAllTasks.addAll(subtasks.values());
        listWithAllTasks.sort(new ComparatorById());

        return listWithAllTasks.stream().map(Object::toString).toList();
    }

    public String getStringForWriteFromList(List<String> list) {
        if (list.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        list.stream().map(string -> "\n" + string).forEachOrdered(sb::append);
        return sb.toString();
    }

    public void writeListToFile(List<String> list, File file) {
        String toWrite = getStringForWriteFromList(list);
        try (FileWriter fw = new FileWriter(file)) {
            fw.write(toWrite);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        save();
    }

    @Override
    public void removeAllEpics() {
        super.removeAllEpics();
        save();
    }

    @Override
    public void removeAllSubtasks() {
        super.removeAllSubtasks();
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void removeTaskById(int id) {
        super.removeTaskById(id);
        save();
    }

    @Override
    public void removeEpicById(int id) {
        super.removeEpicById(id);
        save();
    }

    @Override
    public void removeSubtaskById(int id) {
        super.removeSubtaskById(id);
        save();
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
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}