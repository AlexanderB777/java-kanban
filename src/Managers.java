public class Managers {

    public static TaskManager getDefault() throws Exception {
        return getInMemoryTaskManager();
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
}