package org.example.kbase.service.projectmember;

import org.example.kbase.dto.request.UpdateProjectMemberRequest;
import org.example.kbase.dto.response.ProjectMemberResponse;

import java.util.List;
import java.util.UUID;

public interface IProjectMemberService {

    void createMember(UUID projectId, UUID memberId);

    void addMember(UUID projectId, String memberEmail);

    List<ProjectMemberResponse> getMembers(UUID projectId);

    ProjectMemberResponse getMember(UUID projectId, UUID memberId);

    void updateMember(UUID projectId, UUID memberId, UpdateProjectMemberRequest request);

    void deleteMember(UUID projectId, UUID memberId);
}
