import Managers.Managers;
import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import Tasks.*;
import Managers.*;


public class Main {

    public static void main(String[] args) throws Exception {
        System.out.println("Поехали!");
        TaskManager inMemoryManager = Managers.getDefault();
        FileBackedTaskManager manager = Managers.getFileBackedTaskManager();

    }
}
