package com.example.lastproject.controller.userArea;

import com.example.lastproject.model.requests.UpdateSnapShotRequest;
import com.example.lastproject.service.ProcessBuilderService;
import com.example.lastproject.service.SnapshotService;
import com.example.lastproject.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/userArea/snapshot")
public class SnapshotControllerUser {
    private SnapshotService snapshotService;
    private UserService userService;
    private ProcessBuilderService processBuilderService;
    public SnapshotControllerUser(SnapshotService snapshotService,
                                    ProcessBuilderService processBuilderService,
                                    UserService userService) {
        this.snapshotService = snapshotService;
        this.processBuilderService = processBuilderService;
        this.userService = userService;
    }
        @PutMapping
    public ResponseEntity<?> updateSnapshot(@RequestBody UpdateSnapShotRequest updateSnapShotRequest)
    {
        snapshotService.updateSnapshotCode(updateSnapShotRequest);
        return ResponseEntity.ok("");
    }
}
