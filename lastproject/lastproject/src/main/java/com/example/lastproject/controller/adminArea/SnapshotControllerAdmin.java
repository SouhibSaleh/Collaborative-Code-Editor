package com.example.lastproject.controller.adminArea;

import com.example.lastproject.service.ProcessBuilderService;
import com.example.lastproject.service.SnapshotService;
import com.example.lastproject.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/adminArea/snapshot")

public class SnapshotControllerAdmin {
    private SnapshotService snapshotService;
    private UserService userService;
    private ProcessBuilderService processBuilderService;

    public SnapshotControllerAdmin(SnapshotService snapshotService,
                                   ProcessBuilderService processBuilderService,
                                   UserService userService) {
        this.snapshotService = snapshotService;
        this.processBuilderService = processBuilderService;
        this.userService = userService;
    }
    @PutMapping("/default/{snapshotId}")
    public ResponseEntity<?> setDefaultProject(@PathVariable long snapshotId){

        snapshotService.setDefaultProject(snapshotId);
        return ResponseEntity.ok("");
    }
    @DeleteMapping("/{snapshotId}")
    public ResponseEntity<?>deleteSnapshot(@PathVariable long snapshotId){
        snapshotService.deleteSnapshot(snapshotId);
        return ResponseEntity.ok("");
    }
}
