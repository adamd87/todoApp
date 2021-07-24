package pl.adamd.controller;

import io.micrometer.core.annotation.Timed;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.adamd.logic.ProjectService;
import pl.adamd.model.Project;
import pl.adamd.model.ProjectStep;
import pl.adamd.model.projection.ProjectWriteModel;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/projects")
class ProjectController {
    private final ProjectService service;

    ProjectController(final ProjectService service) {
        this.service = service;
    }

    @GetMapping
    String showProjects(Model model) {
        model.addAttribute("project", new ProjectWriteModel());
        return "projects";
    }

    @PostMapping
    String addProject(
            @ModelAttribute("project")
            @Valid ProjectWriteModel current,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "projects";
        }
        service.saveProject(current);
        model.addAttribute("project", new ProjectWriteModel());
        model.addAttribute("projects", getProjects());
        model.addAttribute("message", "Project added!");
        return "projects";
    }

    @PostMapping(params = "addStep")
    String addProjectStep(@ModelAttribute("project") ProjectWriteModel current) {
        current.getProjectSteps().add(new ProjectStep());
        return "projects";
    }

    @Timed(value = "project.create.group", histogram = true, percentiles = {0.5, 0.95, 0.99})
    @PostMapping("/{id}")
    String createGroup(
            @ModelAttribute("project") ProjectWriteModel current,
            @PathVariable int id,
            Model model,
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime deadline
    ) {
        try{
        service.createGroup(deadline,id);
            model.addAttribute("message", "Group added!");
        } catch (IllegalStateException | IllegalArgumentException e) {
            model.addAttribute("message", "An error occurred while creating the group!");
        }
        return "projects";
    }

    @ModelAttribute("projects")
    List<Project> getProjects() {
        return service.readAll();
    }
}
