package pl.adamd.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @NotBlank(message = "Projects description must not be empty")
    private String description;
    @Embedded
    private Audit audit = new Audit();
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "project")
    private Set<TaskGroup> taskGroups;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "project")
    private Set<ProjectStep> projectSteps;

    Project() {
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

    public Set<TaskGroup> getTaskGroups() {
        return taskGroups;
    }

    void setTaskGroups(final Set<TaskGroup> taskGroups) {
        this.taskGroups = taskGroups;
    }

    public Set<ProjectStep> getProjectSteps() {
        return projectSteps;
    }

    void setProjectSteps(final Set<ProjectStep> projectSteps) {
        this.projectSteps = projectSteps;
    }
}
