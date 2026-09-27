package org.example.kbase.controller;

import java.util.List;
import java.util.UUID;

import org.example.kbase.common.response.ApiResponse;
import org.example.kbase.dto.request.AddMemberRequest;
import org.example.kbase.dto.request.CreateProjectRequest;
import org.example.kbase.dto.request.RemoveMemberRequest;
import org.example.kbase.dto.request.UpdateProjectRequest;
import org.example.kbase.dto.response.ProjectDetailResponse;
import org.example.kbase.dto.response.ProjectResponse;
import org.example.kbase.service.project.IProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("${api.prefix}/projects")
@Tag(name = "Project APIs")
public class ProjectController {

    private final IProjectService projectService;

    public ProjectController(IProjectService projectService) {
        this.projectService = projectService;
    }

    @Operation(
            summary = "Create a project"
    )
    @PostMapping("")
        public ResponseEntity<ApiResponse<Void>> createProject(@Valid @RequestBody CreateProjectRequest request) {
        projectService.createProject(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                                .body(ApiResponse.success("success", null));
    }

    @Operation(
            summary = "Get project by ID"
    )
    @GetMapping("/{projectId}")
        public ResponseEntity<ApiResponse<ProjectDetailResponse>> getProjectById(@PathVariable UUID projectId) {
        ProjectDetailResponse project = projectService.getProjectById(projectId);
        return ResponseEntity.ok(ApiResponse.success("success", project));
    }

    @Operation(
            summary = "Get all projects"
    )
    @GetMapping("")
        public ResponseEntity<ApiResponse<List<ProjectResponse>>> getAllProject() {
        List<ProjectResponse> projects = projectService.getAllProject();
        return ResponseEntity.ok(ApiResponse.success("success", projects));
    }

        @Operation(
                        summary = "Get projects of the current user"
        )
        @GetMapping("/mine")
        public ResponseEntity<ApiResponse<List<ProjectResponse>>> getMyProjects() {
                List<ProjectResponse> projects = projectService.getAllProjectByCurrentUser();
                return ResponseEntity.ok(ApiResponse.success("success", projects));
        }

    @Operation(
            summary = "Update project information"
    )
    @PatchMapping("/{projectId}")
        public ResponseEntity<ApiResponse<Void>> updateProject(@Valid @PathVariable UUID projectId, @RequestBody UpdateProjectRequest request) {
        projectService.updateProject(request, projectId);
        return ResponseEntity
                .ok(ApiResponse.success("success", null));
    }

    @Operation(
            summary = "Delete a project by ID"
    )
    @DeleteMapping("/{projectId}")
        public ResponseEntity<ApiResponse<Void>> deleteProject(@PathVariable UUID projectId) {
        projectService.deleteProject(projectId);
        return ResponseEntity
                .ok(ApiResponse.success("success", null));
    }

    @Operation(
            summary = "Invite a member to project"
    )
    @PostMapping("/invite")
        public ResponseEntity<ApiResponse<Void>> inviteMember(@Valid @RequestBody AddMemberRequest request){
        projectService.addMember(request);
                return ResponseEntity.ok(ApiResponse.success("success", null));
        }

        @Operation(
                        summary = "Remove a member from project"
        )
        @DeleteMapping("/members")
        public ResponseEntity<ApiResponse<Void>> removeMember(@Valid @RequestBody RemoveMemberRequest request) {
                projectService.removeMember(request);
                return ResponseEntity.ok(ApiResponse.success("success", null));
    }

}

