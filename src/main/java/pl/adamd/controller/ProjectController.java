package pl.adamd.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.adamd.logic.ProjectService;
import pl.adamd.model.Project;
import pl.adamd.model.ProjectStep;
import pl.adamd.model.projection.ProjectWriteModel;

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
    String addProject(@ModelAttribute("project") ProjectWriteModel current, Model model){
        service.saveProject(current);
        model.addAttribute("project", new ProjectWriteModel());
        model.addAttribute("message", "Project added!");
        return "projects";
    }

    @PostMapping(params = "addStep")
    String addProjectStep(@ModelAttribute("project") ProjectWriteModel current){
        current.getProjectSteps().add(new ProjectStep());
        return "projects";
    }

    @ModelAttribute("projects")
    List<Project> getProjects(){
        return service.readAll();
    }
}
