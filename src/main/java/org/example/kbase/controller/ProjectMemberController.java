package org.example.kbase.controller;

import jakarta.validation.Valid;
import org.example.kbase.common.response.ApiResponse;
import org.example.kbase.dto.request.UpdateProjectMemberRequest;
import org.example.kbase.dto.response.ProjectMemberResponse;
import org.example.kbase.service.projectmember.IProjectMemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/projects/{projectId}/members")
public class ProjectMemberController {

    private final IProjectMemberService projectMemberService;

    public ProjectMemberController(IProjectMemberService projectMemberService) {
        this.projectMemberService = projectMemberService;
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse> createMember(@PathVariable UUID projectId,
                                                    @Valid @RequestBody UUID memberId) {
        projectMemberService.createMember(projectId, memberId);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("success", null));
    }

    @GetMapping("")
    public ResponseEntity<ApiResponse> getMembers(@PathVariable UUID projectId) {
        List<ProjectMemberResponse> members = projectMemberService.getMembers(projectId);
        return ResponseEntity.ok(new ApiResponse("success", members));
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<ApiResponse> getMember(@PathVariable UUID projectId,
                                                 @PathVariable UUID memberId) {
        return ResponseEntity.ok(new ApiResponse("success", projectMemberService.getMember(projectId, memberId)));
    }

    @PatchMapping("/{memberId}")
    public ResponseEntity<ApiResponse> updateMember(@PathVariable UUID projectId,
                                                    @PathVariable UUID memberId,
                                                    @Valid @RequestBody UpdateProjectMemberRequest request) {
        projectMemberService.updateMember(projectId, memberId, request);
        return ResponseEntity.ok(new ApiResponse("success", null));
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<ApiResponse> deleteMember(@PathVariable UUID projectId,
                                                    @PathVariable UUID memberId) {
        projectMemberService.deleteMember(projectId, memberId);
        return ResponseEntity.ok(new ApiResponse("success", null));
    }

    @PostMapping("/invite")
    public ResponseEntity<ApiResponse> inviteMember(@RequestBody String email) {
        return null;
    }
}