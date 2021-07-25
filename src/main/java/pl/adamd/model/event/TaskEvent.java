package pl.adamd.model.event;

import pl.adamd.model.Task;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.Clock;
import java.time.Instant;


public abstract class TaskEvent {
    public static TaskEvent changed(Task source){
       return source.isDone() ? new TaskDone(source) : new TaskUndone(source);
    }

    private int taskId;
    private Instant occurrence;

    TaskEvent(final int taskId, Clock clock) {
        this.taskId = taskId;
        this.occurrence = Instant.now(clock);
    }

    public int getTaskId() {
        return taskId;
    }

    public Instant getOccurrence() {
        return occurrence;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "taskId=" + taskId +
                ", occurrence=" + occurrence +
                '}';
    }
}
