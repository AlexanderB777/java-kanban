package Managers;

import Tasks.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
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
        Assertions.assertEquals(expected, manager.getHistory().get(0));
    }

    @Test
    void getHistory() throws Exception{
        TaskManager manager = Managers.getDefault();
        manager.createTask(new Task("Дело 1", "Сделать дело 1"));
        manager.createTask(new Task("Дело 2", "Сделать дело 2"));
        manager.createTask(new Task("Дело 3", "Сделать дело 3"));
        manager.getTaskById(1);
        manager.getTaskById(2);

        Assertions.assertEquals(2, manager.getHistory().size());
    }

    @Test
    void getHistoryShouldReturnArrayListSize5() throws Exception {
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
        Assertions.assertEquals(5, manager.getHistory().size());
        Assertions.assertEquals(List.of(1, 3, 4, 9, 2), manager.getHistory().stream().map(Task::getId).toList());
    }

    @Test
    void getHistoryShouldReturnArrayListSize9() throws Exception {
        TaskManager manager = Managers.getDefault();

        manager.createTask(new Task("Задача 1", "(1)Описание задачи 1"));
        manager.createTask(new Task("Задача 2", "(2)Описание задачи 1"));
        manager.createTask(new Task("Задача 3", "(3)Описание задачи 1"));
        manager.createEpic(new Epic("Эпик 1", "(4)Описание эпика 1"));
        manager.createEpic(new Epic("Эпик 2", "(5)Описание эпика 2"));
        manager.createEpic(new Epic("Эпик 3", "(6)Описание эпика 3"));
        manager.createSubtask(new Subtask("Субтаск 1", "(7)Описание субтаска 1 эпик(4)", 4));
        manager.createSubtask(new Subtask("Субтаск 2", "(8)Описание субтаска 2 эпик(6)", 6));
        manager.createSubtask(new Subtask("Субтаск 3", "(9)Описание субтаска 3 эпик(5)", 5));
        manager.createSubtask(new Subtask("Субтаск 4", "(10)Описание субтаска 4 эпик(6)", 6));
        manager.createSubtask(new Subtask("Субтаск 5", "(11)Описание субтаска 5 эпик(4)", 4));
        manager.createSubtask(new Subtask("Субтаск 6", "(12)Описание субтаска 6 эпик(5)", 5));
        manager.createSubtask(new Subtask("Субтаск 7", "(13)Описание субтаска 7 эпик(5)", 5));

        manager.getTaskById(2);
        manager.getTaskById(2);
        manager.getTaskById(2);
        manager.getTaskById(1);
        manager.getEpicById(4);
        manager.getEpicById(6);
        manager.getSubtaskById(10);
        manager.getSubtaskById(13);
        manager.getTaskById(2);
        manager.getSubtaskById(9);
        manager.getEpicById(6);
        manager.getEpicById(5);
        manager.getSubtaskById(12);

        manager.getHistory().forEach(System.out::println);
        Assertions.assertEquals(List.of(1, 4, 10, 13, 2, 9, 6, 5, 12), manager.getHistory().stream().map(Task::getId).toList());
    }

    @Test
    void calculateEndTIme() {
        LocalDateTime startTime = LocalDateTime.of(2024, 4, 21, 12, 0, 0);
        Duration duration = Duration.ofHours(4);
        LocalDateTime expectedEndDate = LocalDateTime.of(2024, 4, 21, 16, 0, 0);
        Task task = new Task("Дело 1", "Сделать дело 1", TaskStatus.NEW, 1, startTime, duration);

        LocalDateTime actualEndTime = task.getEndTime();

        assertEquals(expectedEndDate, actualEndTime);
    }
}