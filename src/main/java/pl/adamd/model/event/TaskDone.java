package pl.adamd.model.event;

import pl.adamd.model.Task;

import java.time.Clock;

public class TaskDone extends TaskEvent{
    TaskDone(final Task source) {
        super(source.getId(), Clock.systemDefaultZone());
    }
}
