package com.example.lastproject.service;

import com.example.lastproject.model.Snapshot;
import com.example.lastproject.model.enums.Role;
import com.example.lastproject.model.requests.UpdateSnapShotRequest;
import com.example.lastproject.repo.ProjectRepo;
import com.example.lastproject.repo.SnapshotRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SnapshotService {
    private SnapshotRepo snapshotRepo;
    private ProjectRepo projectRepo;
    public SnapshotService(SnapshotRepo snapshotRepo,
                           ProjectRepo projectRepo){
        this.snapshotRepo = snapshotRepo;
        this.projectRepo = projectRepo;
    }
    public Snapshot saveSnapshot(Snapshot snapshot){
       return snapshotRepo.save(snapshot);
    }
    public List<Snapshot> getProjectSnapshots(int projectId){
        List<Snapshot>l1= snapshotRepo.findAll();



        List<Snapshot>result = new ArrayList<Snapshot>();
        for(var x:l1){

            if(x.getProjectId()==projectId&&(x.getUserId()==UserService.getCurrentUser().getId()||UserService.getCurrentUser().getRole()== Role.Admin)){

                result.add(x);
            }
        }

        return result;
    }
    public void updateSnapshotCode(UpdateSnapShotRequest updateSnapShotRequest)
    {
        var snapshot=  snapshotRepo.findById(updateSnapShotRequest.getSnapshotId())
                .get();
        snapshot.setCode(updateSnapShotRequest.getCode());
        snapshotRepo.save(snapshot);
    }
    public void setDefaultProject(long snapshotId){
        var snapshotOp = snapshotRepo.findById(snapshotId);
        if(snapshotOp.isPresent()){
            Snapshot snapshot = snapshotOp.get();
            var projectOp = projectRepo.findById(snapshot.getProjectId());
            if(projectOp.isPresent()){
                var project = projectOp.get();
                project.setSnapshotId(snapshot.getId());
                projectRepo.save(project);
            }

        }

    }
    public Snapshot getCurrentProjectSnapshot(long projectId){
        var projectOp = projectRepo.findById(projectId);
        if(projectOp.isPresent()) {
            var project = projectOp.get();
            var snapshotOp = snapshotRepo.findById(project.getSnapshotId());
            if(snapshotOp.isPresent())
                return snapshotOp.get();
            else return null;

        }
        else return null;
    }
    public void deleteSnapshot(long snapshotId){
        snapshotRepo.deleteById(snapshotId);
    }
    public List<Snapshot> allSnaps()
    {
        return snapshotRepo.findAll();
    }

}
