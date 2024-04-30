package utils;

import Tasks.Task;

import java.util.Comparator;

public class ComparatorById implements Comparator<Task> {
    @Override
    public int compare(Task o1, Task o2) {
        return o1.getId() - o2.getId();
    }
}
