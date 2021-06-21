package pl.adamd.logic;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.adamd.TaskConfigurationProperties;
import pl.adamd.model.ProjectRepository;
import pl.adamd.model.TaskGroupRepository;
import pl.adamd.model.TaskRepository;

@Configuration
class LogicConfiguration {
    @Bean
    ProjectService projectService(
            final ProjectRepository repository,
            final TaskGroupRepository taskGroupRepository,
            final TaskConfigurationProperties config
    ) {
        return new ProjectService(repository, taskGroupRepository, config);
    }

    @Bean
    TaskGroupService taskGroupService(
            final TaskGroupRepository taskGroupRepository,
            final TaskRepository taskRepository
    ) {
        return new TaskGroupService(taskGroupRepository, taskRepository);
    }

}
