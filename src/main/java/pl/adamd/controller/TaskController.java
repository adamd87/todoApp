package pl.adamd.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.adamd.logic.TaskService;
import pl.adamd.model.Task;
import pl.adamd.model.TaskRepository;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/tasks")
class TaskController {
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    private final TaskRepository repository;
    private final TaskService service;
    private final ApplicationEventPublisher eventPublisher;

    TaskController(final TaskRepository repository, final TaskService service, final ApplicationEventPublisher eventPublisher) {
        this.repository = repository;
        this.service = service;
        this.eventPublisher = eventPublisher;
    }

    /**
     * @Async example:
     */
    @GetMapping(params = {"!sort", "!page", "!size"})
    CompletableFuture<ResponseEntity<List<Task>>> readAllTasks() {
        logger.warn("Exposing all the tasks!");
        return service.findAllAsync().thenApply(ResponseEntity::ok);
//      return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping
    ResponseEntity<List<Task>> readAllTasks(Pageable page) {
        logger.info("Custom pageable");
        return ResponseEntity.ok(repository.findAll(page).getContent());
    }

    @GetMapping("/{id}")
    ResponseEntity<Task> readTaskById(@PathVariable int id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search/done")
    ResponseEntity<List<Task>> readDoneTasks(@RequestParam(defaultValue = "true") boolean state) {
        return ResponseEntity.ok(
                repository.findByDone(state)
        );
    }

    @GetMapping("/search/deadline/now")
    ResponseEntity<List<Task>> readByDeadLineNow() {
        return ResponseEntity.ok(repository.findByDeadline(LocalDateTime.now()));
    }

    @GetMapping("/search/deadline/day_after")
    ResponseEntity<List<Task>> readByDeadLineDayAfter() {
        return ResponseEntity.ok(repository.findByDeadline(LocalDateTime.now().minusDays(1)));

    }

    @GetMapping("/search/deadline/null")
    ResponseEntity<List<Task>> readByDeadLineNotSet() {
        return ResponseEntity.ok(repository.findByDeadline(null));
    }


    @DeleteMapping("/{id}")
    ResponseEntity<Task> deleteTaskById(@PathVariable int id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.noContent().build();
        }
        repository.findById(id)
                .ifPresent(task ->
                        repository.deleteById(id));
        return ResponseEntity.ok().build();
    }

    @PostMapping
    ResponseEntity<Task> createTask(@RequestBody @Valid Task task) {
        Task result = repository.save(task);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @PutMapping("/{id}")
    ResponseEntity<?> updateTask(@PathVariable int id, @RequestBody @Valid Task toUpdate) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.findById(id)
                .ifPresent(task -> {
                    task.updateFrom(toUpdate);
                    repository.save(task);
                });
        return ResponseEntity.noContent().build();
    }

    @Transactional
    @PatchMapping("/{id}")
    public ResponseEntity<?> toggleTask(@PathVariable int id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.findById(id)
                .map(Task::toggle)
                .ifPresent(eventPublisher::publishEvent);
        return ResponseEntity.noContent().build();
    }
}
