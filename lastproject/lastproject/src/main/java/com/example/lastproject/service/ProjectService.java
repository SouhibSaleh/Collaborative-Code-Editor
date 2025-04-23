package com.example.lastproject.service;

import com.example.lastproject.model.Project;
import com.example.lastproject.model.Snapshot;
import com.example.lastproject.model.User;
import com.example.lastproject.repo.ProjectRepo;
import com.example.lastproject.repo.SnapshotRepo;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectService {
    private ProjectRepo projectRepo;
    private SnapshotService snapshotService;
    public ProjectService(ProjectRepo projectRepo,
                          SnapshotService snapshotService){
        this.projectRepo = projectRepo;
        this.snapshotService = snapshotService;
    }
    public Project getProjectByName(String name){
       var project =  projectRepo.findByName(name);
       if(project.isPresent()){
           return project.get();
       }
       return null;
    }
    public List<Project>getAllProjects(){
        return projectRepo.findAll();
    }
    public List<Project> getAllUserProjects(long userId){
        var allProjects = projectRepo.findAll();
        var resultList = new ArrayList<Project>();
        for(var x:allProjects){
            if(x.getUserId()==userId){
                resultList.add(x);
            }
        }
        return resultList;
    }
    @Transactional
    public Project addProject(Project project) {
        project.setUserId(getCurrentUser().getId());
        project = projectRepo.save(project);
        Snapshot snapshot = new Snapshot();
        snapshot.setProjectId(project.getId());
        snapshot.setUserId(getCurrentUser().getId());
        snapshot = snapshotService.saveSnapshot(snapshot);
        project.setSnapshotId(snapshot.getId());
        project = projectRepo.save(project);
        return project;
    }
    public void deleteProject(long projectId){
        projectRepo.deleteById(projectId);
    }
    public Project getProjectById(long projectId){
        System.out.println(projectRepo.findAll());
        return projectRepo.findById(projectId).orElse(null);
    }
    private User getCurrentUser(){
        return (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }

}
