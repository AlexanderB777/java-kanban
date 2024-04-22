import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {
    @Test
    void saveTest() throws Exception {
        FileBackedTaskManager manager = Managers.getFileBackedTaskManager();

        LocalDateTime time1 = LocalDateTime.of(2024, 4, 1, 12, 0 , 0);
        LocalDateTime time2 = LocalDateTime.of(2024, 4, 2, 12, 0 , 0);
        LocalDateTime time3 = LocalDateTime.of(2024, 4, 3, 12, 0 , 0);
        LocalDateTime time4 = LocalDateTime.of(2024, 4, 4, 12, 0 , 0);
        LocalDateTime time5 = LocalDateTime.of(2024, 4, 5, 12, 0 , 0);
        LocalDateTime time6 = LocalDateTime.of(2024, 4, 6, 12, 0 , 0);
        LocalDateTime time7 = LocalDateTime.of(2024, 4, 7, 12, 0 , 0);
        LocalDateTime time8 = LocalDateTime.of(2024, 4, 8, 12, 0 , 0);
        LocalDateTime time9 = LocalDateTime.of(2024, 4, 9, 12, 0 , 0);
        LocalDateTime time10 = LocalDateTime.of(2024, 4, 10, 12, 0 , 0);

        Duration duration2 = Duration.ofHours(2);
        Duration duration4 = Duration.ofHours(4);
        Duration duration8 = Duration.ofHours(8);
        Duration duration10 = Duration.ofHours(30);

        manager.createTask(new Task("Задача 1", "(1)Описание задачи 1", time1, duration2));
        manager.createTask(new Task("Задача 2", "(2)Описание задачи 2", time2, duration4));
        manager.createTask(new Task("Задача 3", "(3)Описание задачи 3", time3, duration8));
        manager.createEpic(new Epic("Эпик 1", "(4)Описание эпика 1"));
        manager.createEpic(new Epic("Эпик 2", "(5)Описание эпика 2"));
        manager.createEpic(new Epic("Эпик 3", "(6)Описание эпика 3"));
        manager.createSubtask(new Subtask(
                "Субтаск 1", "(7)Описание субтаска 1 эпик(4)", 4, time4, duration8));
        manager.createSubtask(new Subtask(
                "Субтаск 2", "(8)Описание субтаска 2 эпик(6)", 6, time5, duration2));
        manager.createSubtask(new Subtask(
                "Субтаск 3", "(9)Описание субтаска 3 эпик(5)", 5, time6, duration10));
        manager.createSubtask(new Subtask(
                "Субтаск 4", "(10)Описание субтаска 4 эпик(6)", 6, time7, duration2));
        manager.createSubtask(new Subtask(
                "Субтаск 5", "(11)Описание субтаска 5 эпик(4)", 4, time8, duration4));
        manager.createSubtask(new Subtask(
                "Субтаск 6", "(12)Описание субтаска 6 эпик(5)", 5, time9, duration10));
        manager.createSubtask(new Subtask(
                "Субтаск 7", "(13)Описание субтаска 7 эпик(5)", 5, time10, duration10));

        manager.save();
        String actualContent = Files.readString(Paths.get("src/Task_storage.csv"));
        String expectedContent = Files.readString(Paths.get("src/expected_save_file.csv"));
        assertEquals(expectedContent, actualContent);

    }

    @Test
    void readTest() throws Exception {
        FileBackedTaskManager manager = Managers.getFileBackedTaskManager();

        LocalDateTime time1 = LocalDateTime.of(2024, 4, 1, 12, 0 , 0);
        LocalDateTime time2 = LocalDateTime.of(2024, 4, 2, 12, 0 , 0);
        LocalDateTime time3 = LocalDateTime.of(2024, 4, 3, 12, 0 , 0);
        LocalDateTime time4 = LocalDateTime.of(2024, 4, 4, 12, 0 , 0);
        LocalDateTime time5 = LocalDateTime.of(2024, 4, 5, 12, 0 , 0);
        LocalDateTime time6 = LocalDateTime.of(2024, 4, 6, 12, 0 , 0);
        LocalDateTime time7 = LocalDateTime.of(2024, 4, 7, 12, 0 , 0);
        LocalDateTime time8 = LocalDateTime.of(2024, 4, 8, 12, 0 , 0);
        LocalDateTime time9 = LocalDateTime.of(2024, 4, 9, 12, 0 , 0);
        LocalDateTime time10 = LocalDateTime.of(2024, 4, 10, 12, 0 , 0);

        Duration duration2 = Duration.ofHours(2);
        Duration duration4 = Duration.ofHours(4);
        Duration duration8 = Duration.ofHours(8);
        Duration duration10 = Duration.ofHours(30);

        manager.createTask(new Task("Задача 1", "(1)Описание задачи 1", time1, duration2));
        manager.createTask(new Task("Задача 2", "(2)Описание задачи 2", time2, duration4));
        manager.createTask(new Task("Задача 3", "(3)Описание задачи 3", time3, duration8));
        manager.createEpic(new Epic("Эпик 1", "(4)Описание эпика 1"));
        manager.createEpic(new Epic("Эпик 2", "(5)Описание эпика 2"));
        manager.createEpic(new Epic("Эпик 3", "(6)Описание эпика 3"));
        manager.createSubtask(new Subtask(
                "Субтаск 1", "(7)Описание субтаска 1 эпик(4)", 4, time4, duration8));
        manager.createSubtask(new Subtask(
                "Субтаск 2", "(8)Описание субтаска 2 эпик(6)", 6, time5, duration2));
        manager.createSubtask(new Subtask(
                "Субтаск 3", "(9)Описание субтаска 3 эпик(5)", 5, time6, duration10));
        manager.createSubtask(new Subtask(
                "Субтаск 4", "(10)Описание субтаска 4 эпик(6)", 6, time7, duration2));
        manager.createSubtask(new Subtask(
                "Субтаск 5", "(11)Описание субтаска 5 эпик(4)", 4, time8, duration4));
        manager.createSubtask(new Subtask(
                "Субтаск 6", "(12)Описание субтаска 6 эпик(5)", 5, time9, duration10));
        manager.createSubtask(new Subtask(
                "Субтаск 7", "(13)Описание субтаска 7 эпик(5)", 5, time10, duration10));

        manager.save();
        manager.restore();
        manager.save();
        String actualContent = Files.readString(Paths.get("src/Task_storage.csv"));
        String expectedContent = Files.readString(Paths.get("src/expected_save_file.csv"));
        assertEquals(expectedContent, actualContent);


    }
}