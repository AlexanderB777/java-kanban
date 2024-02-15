import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class InMemoryTaskManagerTest {

    @Test
    public void shouldReturnTrue() throws Exception {
        TaskManager manager = Managers.getDefault();
        Task expected = new Task("Дело 5", "Сделать дело 5");
        manager.createTask(expected);
        Task actual = manager.getTaskById(1);
        Assertions.assertEquals(expected, actual);
    }
}