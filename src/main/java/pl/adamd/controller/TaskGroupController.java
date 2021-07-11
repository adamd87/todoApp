package pl.adamd.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.adamd.logic.TaskGroupService;
import pl.adamd.model.Task;
import pl.adamd.model.TaskRepository;
import pl.adamd.model.projection.GroupReadModel;
import pl.adamd.model.projection.GroupWriteModel;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/groups")
class TaskGroupController {
    private static final Logger logger = LoggerFactory.getLogger(TaskGroupController.class);

    private final TaskGroupService groupService;
    private final TaskRepository taskRepository;

    TaskGroupController(
            final TaskGroupService groupService,
            final TaskRepository taskRepository) {
        this.groupService = groupService;
        this.taskRepository = taskRepository;
    }

    @GetMapping
    ResponseEntity<List<GroupReadModel>> readAllTaskGroups() {
        return ResponseEntity.ok(groupService.readAll());
    }

    @GetMapping("/{id}")
    ResponseEntity<List<Task>> readTasksByGroupId(@PathVariable int id) {
        return ResponseEntity.ok(taskRepository.findAllByGroup_Id(id));
    }

    @PostMapping
    ResponseEntity<GroupReadModel> createTaskGroup(@RequestBody @Valid GroupWriteModel toCreate) {
        GroupReadModel result = groupService.createGroup(toCreate);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @Transactional
    @PatchMapping("/{id}")
    public ResponseEntity<?> toggleTaskGroup(@PathVariable int id) {
        groupService.toggleGroup(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<?> handleIllegalArgument(IllegalArgumentException e){
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(IllegalStateException.class)
    ResponseEntity<?> handleIllegalState(IllegalStateException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }

}
