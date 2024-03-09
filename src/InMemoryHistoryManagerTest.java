import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryHistoryManagerTest {

    @Test
    void addTask() throws Exception {
        TaskManager manager = Managers.getDefault();
        manager.createTask(new Task("Дело 1", "Сделать дело 1"));
        manager.createTask(new Task("Дело 2", "Сделать дело 2"));
        manager.getTaskById(1);
        Task expected = new Task("Дело 1", "Сделать дело 1", TaskStatus.NEW, 1);
        assertEquals(expected, manager.getHistory().get(0));
    }

    @Test
    void getHistory() throws Exception{
        TaskManager manager = Managers.getDefault();
        manager.createTask(new Task("Дело 1", "Сделать дело 1"));
        manager.createTask(new Task("Дело 2", "Сделать дело 2"));
        manager.createTask(new Task("Дело 3", "Сделать дело 3"));
        manager.getTaskById(1);
        manager.getTaskById(2);

        assertEquals(2, manager.getHistory().size());
    }

    @Test
    void getHistory_listAllTasks() throws Exception {
        TaskManager manager = Managers.getDefault();

        manager.createTask(new Task("Задача 1", "(1)Описание задачи 1"));
        manager.createTask(new Task("Задача 2", "(2)Описание задачи 2"));
        manager.createTask(new Task("Задача 3", "(3)Описание задачи 3"));
        manager.createTask(new Task("Задача 4", "(3)Описание задачи 4"));
        manager.createTask(new Task("Задача 5", "(3)Описание задачи 5"));
        manager.createTask(new Task("Задача 6", "(3)Описание задачи 6"));
        manager.createTask(new Task("Задача 7", "(3)Описание задачи 7"));
        manager.createTask(new Task("Задача 8", "(3)Описание задачи 8"));
        manager.createTask(new Task("Задача 9", "(3)Описание задачи 9"));

        manager.getTaskById(2);
        manager.getTaskById(2);
        manager.getTaskById(2);
        manager.getTaskById(1);
        manager.getTaskById(1);
        manager.getTaskById(3);
        manager.getTaskById(4);
        manager.getTaskById(9);
        manager.getTaskById(2);
        manager.getTaskById(9);
        manager.getTaskById(2);

        manager.getHistory().forEach(System.out::println);
        assertEquals(5, manager.getHistory().size());
        assertEquals(List.of(1, 3, 4, 2, 9), manager.getHistory().stream().map(Task::getId).toList());
    }
}