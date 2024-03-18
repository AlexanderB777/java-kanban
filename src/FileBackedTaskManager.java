import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FileBackedTaskManager extends InMemoryTaskManager {

    File file;
    FileBackedHistoryManager historyManager;

    public FileBackedTaskManager() throws Exception {
        super();
        historyManager = Managers.getFileBackedHistoryManager();
        file = new File("src/TaskStorage.csv");
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
                    createTask(new Task(fromLine[2], fromLine[4],
                            TaskStatus.valueOf(fromLine[3]), Integer.parseInt(fromLine[1])));
                    break;
                case ("SUBTASK"):
                    createSubtask(new Subtask(fromLine[2], fromLine[4], Integer.parseInt(fromLine[5]),
                            TaskStatus.valueOf(fromLine[3]), Integer.parseInt(fromLine[1])));
                    break;
                case ("EPIC"):
                    createEpic(new Epic(fromLine[2], fromLine[4],
                            TaskStatus.valueOf(fromLine[3]), Integer.parseInt(fromLine[1])));
                    break;
            }
        }
    }


    public void save() {
        List<String> allTasks = getAllTasksForFile();
        writeListToFile(allTasks, file);

    }

    public List<String> getAllTasksForFile() {
        List<String> result = new ArrayList<>();
        List<Object> listWithAllTasks = new ArrayList<>();
        for (Map.Entry entry : tasks.entrySet()) {
            listWithAllTasks.add(entry.getValue());
        }
        for (Map.Entry entry : epics.entrySet()) {
            listWithAllTasks.add(entry.getValue());
        }
        for (Map.Entry entry : subtasks.entrySet()) {
            listWithAllTasks.add(entry.getValue());
        }

        for (Object task : listWithAllTasks) {
            result.add(task.toString());
        }
        return result;
    }

    public String getStringForWriteFromList(List<String> list) {
        StringBuilder sb = new StringBuilder(list.get(0));
        if (list.size() > 1) {
            for (int i = 1; i < list.size(); i++) {
                sb.append("\n").append(list.get(i));
            }
        }
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
