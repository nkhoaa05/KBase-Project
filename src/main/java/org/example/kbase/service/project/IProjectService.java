package org.example.kbase.service.project;

import org.example.kbase.dto.request.CreateProjectRequest;
import org.example.kbase.dto.request.UpdateProjectRequest;
import org.example.kbase.dto.response.ProjectResponse;

import java.util.List;
import java.util.UUID;

public interface IProjectService {

    void createProject(CreateProjectRequest request);

    void updateProject(UpdateProjectRequest request, UUID projectId);

    void deleteProject(UUID projectId);

    ProjectResponse getProjectById(UUID projectId);

    List<ProjectResponse> getAllProject();

}
