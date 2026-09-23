package org.example.kbase.service.project;

import org.example.kbase.dto.request.CreateProjectRequest;
import org.example.kbase.dto.request.UpdateProjectRequest;
import org.example.kbase.dto.response.ProjectResponse;
import org.example.kbase.model.Project;
import org.example.kbase.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class ProjectService implements IProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Transactional
    @Override
    public void createProject(CreateProjectRequest request) {
        Project newProject = new Project(
                request.name(),
                request.description()
        );

        projectRepository.save(newProject);
    }

    @Transactional
    @Override
    public void updateProject(UpdateProjectRequest request, UUID projectId) {
        Project exsitingProject = projectRepository.findById(projectId)
                .orElseThrow(() ->
                        new RuntimeException("Project not found: ID " + projectId));

        exsitingProject.setName(request.name());
        exsitingProject.setDescription(request.description());
    }

    @Transactional
    @Override
    public void deleteProject(UUID projectId) {
        if (!projectRepository.existsById(projectId)) throw new RuntimeException("Project not found: ID " + projectId);
        projectRepository.deleteById(projectId);
    }

    @Override
    public ProjectResponse getProjectById(UUID projectId) {
        Project proj = projectRepository.findById(projectId)
                .orElseThrow(() ->
                        new RuntimeException("Project not found: ID " + projectId)
                );

        return new ProjectResponse(
                proj.getId(),
                proj.getName(),
                proj.getDescription()
        );
    }

    @Override
    public List<ProjectResponse> getAllProject() {
        return projectRepository.findAll()
                .stream()
                .map(proj -> new ProjectResponse(
                                proj.getId(),
                                proj.getName(),
                                proj.getDescription()
                        )
                )
                .toList();

    }
}
