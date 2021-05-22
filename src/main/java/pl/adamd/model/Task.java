package pl.adamd.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @NotBlank(message = "Task's description must not be empty")
    private String description;
    private boolean done;
    private LocalDateTime deadline;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(column = @Column(name = "created_on"), name = "createdOn"),
            @AttributeOverride(column = @Column(name = "updated_on"), name = "updatedOn")
    })
    private Audit audit = new Audit();

    Task() {
    }

    public int getId() {
        return id;
    }

    void setId(final int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    void setDeadline(final LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(final boolean done) {
        this.done = done;
    }

    public void updateFrom(final Task source) {
        description = source.description;
        done = source.done;
        deadline = source.deadline;
    }

}
