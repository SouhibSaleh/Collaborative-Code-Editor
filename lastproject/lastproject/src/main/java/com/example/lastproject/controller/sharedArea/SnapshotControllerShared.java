package com.example.lastproject.controller.sharedArea;

import com.example.lastproject.model.Snapshot;
import com.example.lastproject.model.requests.RunCodeRequest;
import com.example.lastproject.model.requests.SaveSnapshotRequest;
import com.example.lastproject.model.requests.UpdateSnapShotRequest;
import com.example.lastproject.service.ProcessBuilderService;
import com.example.lastproject.service.ProjectService;
import com.example.lastproject.service.SnapshotService;
import com.example.lastproject.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/sharedArea/snapshot")
public class SnapshotControllerShared {
    private SnapshotService snapshotService;
    private ProjectService projectService;
    private UserService userService;
    private ProcessBuilderService processBuilderService;
    public SnapshotControllerShared(SnapshotService snapshotService,
                              ProcessBuilderService processBuilderService,
                              UserService userService,
                                    ProjectService projectService) {
        this.snapshotService = snapshotService;
        this.processBuilderService = processBuilderService;
        this.userService = userService;
        this.projectService = projectService;
    }
    @PostMapping
    public ResponseEntity<?> saveSnapshot(@RequestBody SaveSnapshotRequest saveSnapShotRequset){
        Snapshot snapshot = new Snapshot();
        snapshot.setCode(saveSnapShotRequset.getCode());
        var tempUser = projectService.getProjectById(saveSnapShotRequset.getProjectId());
        if(tempUser==null)return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        snapshot.setProjectId(tempUser.getId());
        snapshot.setUserId(userService.getCurrentUser().getId());
        snapshot.setComments(saveSnapShotRequset.getComments());
        var result = snapshotService.saveSnapshot(snapshot);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<?> getAllSnapShots(@PathVariable int projectId){
        System.out.println(snapshotService.getProjectSnapshots(projectId));
        return ResponseEntity.ok(snapshotService.getProjectSnapshots(projectId));
    }


    @PostMapping("/run")
    public ResponseEntity<?>runCode(@RequestBody RunCodeRequest runCodeRequest){
        String []arr = runCodeRequest.getArgs().split(" ");
        String result=  processBuilderService.runCode(runCodeRequest.getLanguage(),runCodeRequest.getCode(), arr);
        return ResponseEntity.ok(Map.of("message",result));
    }
}
