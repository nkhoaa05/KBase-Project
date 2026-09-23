package org.example.kbase.controller;

import jakarta.validation.Valid;
import org.example.kbase.common.response.ApiResponse;
import org.example.kbase.dto.request.CreateProjectRequest;
import org.example.kbase.dto.request.UpdateProjectRequest;
import org.example.kbase.dto.response.ProjectResponse;
import org.example.kbase.service.project.IProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/projects")
public class ProjectController {

    private final IProjectService projectService;

    public ProjectController(IProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse> createProject(@Valid @RequestBody CreateProjectRequest request) {
        projectService.createProject(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("success", null));
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<ApiResponse> getProjectById(@PathVariable UUID projectId) {
        ProjectResponse project = projectService.getProjectById(projectId);
        return ResponseEntity.ok(new ApiResponse("success", project));
    }

    @GetMapping("")
    public ResponseEntity<ApiResponse> getAllProject() {
        List<ProjectResponse> projects = projectService.getAllProject();
        return ResponseEntity.ok(new ApiResponse("success", projects));
    }

    @PatchMapping("/{projectId}")
    public ResponseEntity<ApiResponse> updateProject(@Valid @PathVariable UUID projectId, @RequestBody UpdateProjectRequest request) {
        projectService.updateProject(request, projectId);
        return ResponseEntity
                .ok(new ApiResponse("success", null));
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<ApiResponse> deleteProject(@PathVariable UUID projectId) {
        projectService.deleteProject(projectId);
        return ResponseEntity
                .ok(new ApiResponse("success", null));
    }

}

