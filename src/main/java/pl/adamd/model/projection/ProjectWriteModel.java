package pl.adamd.model.projection;

import pl.adamd.model.Project;
import pl.adamd.model.ProjectStep;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ProjectWriteModel {
    @NotBlank(message = "Projects description must not be empty")
    private String description;
    @Valid
    private List<ProjectStep> projectSteps = new ArrayList<>();

    public ProjectWriteModel() {
        projectSteps.add(new ProjectStep());
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public List<ProjectStep> getProjectSteps() {
        return projectSteps;
    }

    public void setProjectSteps(final List<ProjectStep> projectSteps) {
        this.projectSteps = projectSteps;
    }

    public Project toProject(){
        var result = new Project();
        result.setDescription(description);
        projectSteps.forEach(projectStep -> projectStep.setProject(result));
        result.setProjectSteps(new HashSet<>(projectSteps));
        return result;

    }
}
