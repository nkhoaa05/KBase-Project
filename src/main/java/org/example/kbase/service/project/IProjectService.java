package org.example.kbase.service.project;

import java.util.List;
import java.util.UUID;

import org.example.kbase.dto.request.*;
import org.example.kbase.dto.response.ProjectDetailResponse;
import org.example.kbase.dto.response.ProjectResponse;

public interface IProjectService {

    void createProject(CreateProjectRequest request);

    void updateProject(UpdateProjectRequest request, UUID projectId);

    void deleteProject(UUID projectId);

    ProjectDetailResponse getProjectById(UUID projectId);

    List<ProjectResponse> getAllProject();

    void addMember(AddMemberRequest request);

    void removeMember(RemoveMemberRequest request);

}
