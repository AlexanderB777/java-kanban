import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {
        System.out.println("Поехали!");
        TaskManager inMemoryManager = Managers.getDefault();
        FileBackedTaskManager manager = Managers.getFileBackedTaskManager();

        LocalDateTime time1 = LocalDateTime.of(2024, 4, 20, 12, 0 , 0);
        LocalDateTime time2 = LocalDateTime.of(2024, 4, 15, 12, 0 , 0);
        LocalDateTime time3 = LocalDateTime.of(2024, 4, 10, 12, 0 , 0);
        LocalDateTime time4 = LocalDateTime.of(2024, 4, 5, 12, 0 , 0);

        Duration duration2 = Duration.ofHours(2);
        Duration duration4 = Duration.ofHours(4);
        Duration duration8 = Duration.ofHours(8);
        Duration duration10 = Duration.ofHours(10);

        manager.createTask(new Task("Задача 1", "(1)Описание задачи 1", time1, duration2));
        manager.createTask(new Task("Задача 2", "(2)Описание задачи 1", time3, duration4));
        manager.createTask(new Task("Задача 3", "(3)Описание задачи 1", time1, duration8));
        manager.createEpic(new Epic("Эпик 1", "(4)Описание эпика 1"));
        manager.createEpic(new Epic("Эпик 2", "(5)Описание эпика 2"));
        manager.createEpic(new Epic("Эпик 3", "(6)Описание эпика 3"));
        manager.createSubtask(new Subtask("Субтаск 1", "(7)Описание субтаска 1 эпик(4)", 4, time1, duration8));
        manager.createSubtask(new Subtask("Субтаск 2", "(8)Описание субтаска 2 эпик(6)", 6, time3, duration2));
        manager.createSubtask(new Subtask("Субтаск 3", "(9)Описание субтаска 3 эпик(5)", 5, time4, duration10));
        manager.createSubtask(new Subtask("Субтаск 4", "(10)Описание субтаска 4 эпик(6)", 6, time4, duration2));
        manager.createSubtask(new Subtask("Субтаск 5", "(11)Описание субтаска 5 эпик(4)", 4, time1, duration4));
        manager.createSubtask(new Subtask("Субтаск 6", "(12)Описание субтаска 6 эпик(5)", 5, time3, duration4));
        manager.createSubtask(new Subtask("Субтаск 7", "(13)Описание субтаска 7 эпик(5)", 5, time2, duration10));

        List<Task> list = manager.getPrioritizedTasks();
        for (Task task : list) {
            System.out.println(task);
        }
    }
}
