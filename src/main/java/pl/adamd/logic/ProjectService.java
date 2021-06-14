package pl.adamd.logic;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import pl.adamd.TaskConfigurationProperties;
import pl.adamd.model.*;
import pl.adamd.model.projection.GroupReadModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequestScope
public class ProjectService {

    private ProjectRepository repository;
    private TaskGroupRepository taskGroupRepository;
    private TaskConfigurationProperties config;

    ProjectService(final ProjectRepository repository,
                   final TaskGroupRepository taskGroupRepository,
                   final TaskConfigurationProperties config) {
        this.repository = repository;
        this.taskGroupRepository = taskGroupRepository;
        this.config = config;
    }

    public List<Project> readAll() {
        return repository.findAll();
    }

    public Project saveProject(final Project project) {
        return repository.save(project);
    }

    public GroupReadModel createGroup(LocalDateTime deadline, int projectId) {
        if (
                !config.getTemplate().isAllowMultipleTasks() &&
                taskGroupRepository.existsByDoneIsFalseAndProject_Id(projectId))
        {
            throw new IllegalStateException("Only one undone group from project is allowed");
        }
        TaskGroup result = repository.findById(projectId)
                .map(project -> {
                    var targetGroup = new TaskGroup();
                    targetGroup.setDescription(project.getDescription());
                    targetGroup.setTasks(project.getProjectSteps().stream()
                            .map(projectStep -> new Task(
                                    projectStep.getDescription(),
                                    deadline.plusDays(projectStep.getDaysToDeadline())))
                            .collect(Collectors.toSet())

                    );
                    targetGroup.setProject(project);
                    return taskGroupRepository.save(targetGroup);
                }).orElseThrow(() -> new IllegalArgumentException("Project with given id not found"));
        return new GroupReadModel(result);
    }
}
