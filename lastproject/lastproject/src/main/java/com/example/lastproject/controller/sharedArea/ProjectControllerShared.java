package com.example.lastproject.controller.sharedArea;

import com.example.lastproject.model.Project;
import com.example.lastproject.model.requests.ProjectRequest;
import com.example.lastproject.service.ProjectService;
import com.example.lastproject.service.SnapshotService;
import com.example.lastproject.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/sharedArea/project")
public class ProjectControllerShared {
    private ProjectService projectService;
    private SnapshotService snapshotService;
    public ProjectControllerShared(ProjectService projectService,
                                   SnapshotService snapshotService){
        this.projectService = projectService;
        this.snapshotService = snapshotService;
    }
    @GetMapping
    public ResponseEntity<?> getAllProjects(){
        var result = projectService.getAllProjects();
        return ResponseEntity.of(Optional.of(Map.of("message", result)));
    }
    @GetMapping("/{projectId}/snapshot")
    public ResponseEntity<?> getCurrentSnapshot(@PathVariable long projectId){

        return ResponseEntity.ok(snapshotService.getCurrentProjectSnapshot(projectId));
    }
    @PostMapping
    public ResponseEntity<?> saveProject(@RequestBody ProjectRequest projectRequest){

        Project project = new Project();
        project.setUserId(UserService.getCurrentUser().getId());
        project.setName(projectRequest.getName());
        project.setLanguage(projectRequest.getLanguage());
        project.setCreationDate(LocalDateTime.now());

        var result = projectService.addProject(project);
        return ResponseEntity.of(Optional.of(Map.of("message", result)));
    }
}
