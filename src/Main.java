import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {
        System.out.println("Поехали!");
        TaskManager inMemoryManager = Managers.getDefault();
        FileBackedTaskManager manager = Managers.getFileBackedTaskManager();

    }
}
