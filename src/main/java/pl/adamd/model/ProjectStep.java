package pl.adamd.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "project_steps")
public class ProjectStep {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "Project step's description must not be empty")
    private String description;
    @Column(name = "days_to_deadline")
    private Long daysToDeadline;
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    ProjectStep() {
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

    void setDescription(final String description) {
        this.description = description;
    }

    public Long getDaysToDeadline() {
        return daysToDeadline;
    }

    void setDaysToDeadline(final Long daysToDeadline) {
        this.daysToDeadline = daysToDeadline;
    }

    public Project getProject() {
        return project;
    }

    void setProject(final Project project) {
        this.project = project;
    }
}
