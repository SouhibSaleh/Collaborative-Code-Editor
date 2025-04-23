package com.example.lastproject.controller.adminArea;

import com.example.lastproject.service.ProjectService;
import com.example.lastproject.service.SnapshotService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/adminArea/project")
public class ProjectControllerAdmin {
    private ProjectService projectService;
    private SnapshotService snapshotService;
    public ProjectControllerAdmin(ProjectService projectService,
                             SnapshotService snapshotService){
        this.projectService = projectService;
        this.snapshotService = snapshotService;
    }
    @DeleteMapping("/{projectId}")
    public ResponseEntity<?> deleteProject(@PathVariable long projectId){
        projectService.deleteProject(projectId);
        return ResponseEntity.ok("");
    }
}
