import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileBackedHistoryManager extends InMemoryHistoryManager {

    private File file;

    public FileBackedHistoryManager() {
        super();
        file = new File("src/History.csv");
    }

    public void save() {
        List<String> tasksInStringArray = getTasksInStringArray(getTasks());
        writeListToFile(tasksInStringArray, file);
    }


    public void writeListToFile(List<String> list, File file) {
        String toWrite = getStringForWriteFromList(list);
        try (FileWriter fw = new FileWriter(file)) {
            fw.write(toWrite);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getTasksInStringArray(List<Task> list) {
        List<String> result = new ArrayList<>();
        for (Task task : list) {
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

    public List<Task> getListFromFile() {
        List<Task> result = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            while (bufferedReader.ready()) {
                String[] fromLine = bufferedReader.readLine().split(",");
                Enum<TaskTypes> type = TaskTypes.valueOf(fromLine[0]);
                String name = fromLine[2];
                String description = fromLine[4];
                int id = Integer.parseInt(fromLine[1]);
                switch (type) {
                    case TaskTypes.TASK:
                        result.add(new Task(name, description, TaskStatus.valueOf(fromLine[3]), id));
                        break;
                    case TaskTypes.EPIC:
                        result.add(new Epic(name, description, TaskStatus.valueOf(fromLine[3]), id));
                        break;
                    case TaskTypes.SUBTASK:
                        result.add(new Subtask(name, description,
                                Integer.parseInt(fromLine[5]), TaskStatus.valueOf(fromLine[3]), id));
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + type);
                }

            }
        } catch (IOException e) {
            throw new RuntimeException();
        }



        return result;
    }


    @Override
    public void add(Task task) {
        super.add(task);
        save();
    }

    @Override
    public void remove(int id) {
        super.remove(id);
        save();
    }

    @Override
    public List<Task> getHistory() {
        return getListFromFile();
    }
}

