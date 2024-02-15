import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class InMemoryHistoryManagerTest {

    @Test
    void addTask() throws Exception {
        TaskManager manager = Managers.getDefault();
        manager.createTask(new Task("Дело 1", "Сделать дело 1"));
        manager.createTask(new Task("Дело 2", "Сделать дело 2"));
        manager.getTaskById(1);
        Task expected = new Task("Дело 1", "Сделать дело 1", TaskStatus.NEW, 1);
        Assertions.assertEquals(expected, manager.getHistory().get(0));
    }

    @Test
    void getHistory() throws Exception{
        TaskManager manager = Managers.getDefault();
        manager.createTask(new Task("Дело 1", "Сделать дело 1"));
        manager.createTask(new Task("Дело 2", "Сделать дело 2"));
        manager.getTaskById(1);
        Assertions.assertEquals(1, manager.getHistory().size());
    }
}