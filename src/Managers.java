public class Managers {

    public static TaskManager getDefault() throws Exception {
        return getInMemoryTaskManager();
    }

    public static FileBackedTaskManager getFileBackedTaskManager() throws Exception {
        return new FileBackedTaskManager();
    }

    public static InMemoryTaskManager getInMemoryTaskManager() throws Exception {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory()  {
        return getInMemoryHistoryManager();
    }

    public static InMemoryHistoryManager getInMemoryHistoryManager() {
        return new InMemoryHistoryManager();
    }

    public static FileBackedHistoryManager getFileBackedHistoryManager() throws Exception {
        return new FileBackedHistoryManager();
    }
}