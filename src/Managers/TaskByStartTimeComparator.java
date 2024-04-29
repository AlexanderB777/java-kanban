package Managers;

import Tasks.Task;

import java.util.Comparator;

public class TaskByStartTimeComparator implements Comparator<Task> {

    @Override
    public int compare(Task o1, Task o2) {
        if (o1.startTime.isBefore(o2.startTime)) return -1;
        if (o1.startTime.isAfter(o2.startTime)) return 1;
        return 0;
    }
}