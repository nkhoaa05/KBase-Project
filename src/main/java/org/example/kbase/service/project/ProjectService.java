package org.example.kbase.service.project;

import java.util.List;
import java.util.UUID;

import org.example.kbase.common.exception.BadRequestException;
import org.example.kbase.common.exception.ResourceNotFoundException;
import org.example.kbase.dto.request.AddMemberRequest;
import org.example.kbase.dto.request.CreateProjectRequest;
import org.example.kbase.dto.request.RemoveMemberRequest;
import org.example.kbase.dto.request.UpdateProjectRequest;
import org.example.kbase.dto.response.ProjectDetailResponse;
import org.example.kbase.dto.response.ProjectMemberResponse;
import org.example.kbase.dto.response.ProjectResponse;
import org.example.kbase.model.Enum.ProjectRole;
import org.example.kbase.model.Project;
import org.example.kbase.model.ProjectMember;
import org.example.kbase.model.User;
import org.example.kbase.repository.ProjectMemberRepository;
import org.example.kbase.repository.ProjectRepository;
import org.example.kbase.repository.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProjectService implements IProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository memberRepository;
    private final UserRepository userRepository;


    public ProjectService(ProjectRepository projectRepository,
                          ProjectMemberRepository memberRepository,
                          UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.memberRepository = memberRepository;
        this.userRepository = userRepository;
    }


    /*

    LOGIC BUSINESS FOR PROJECT

     */
    @Transactional
    @Override
    public void createProject(CreateProjectRequest request) {
        Project project = initialProject(request);
        User user = getCurrentUser();

        ProjectMember member = new ProjectMember();
        member.setProject(project);
        member.setMember(user);
        member.setRole(ProjectRole.OWNER);
        memberRepository.save(member);
    }

    @Transactional
    @Override
    public void updateProject(UpdateProjectRequest request, UUID projectId) {
        Project exsitingProject = projectRepository.findById(projectId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Project not found: ID " + projectId));

        exsitingProject.setName(request.name());
        exsitingProject.setDescription(request.description());
    }

    @Transactional
    @Override
    public void deleteProject(UUID projectId) {
        if (!projectRepository.existsById(projectId)) throw new ResourceNotFoundException("Project not found: ID " + projectId);
        projectRepository.deleteById(projectId);
    }

    @Override
    public ProjectDetailResponse getProjectById(UUID projectId) {
        Project proj = projectRepository.findById(projectId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Project not found: ID " + projectId)
                );

        List<ProjectMemberResponse> members = memberRepository.findAllByProjectId(projectId)
                .stream()
                .map(member -> new ProjectMemberResponse(
                        member.getId(),
                        member.getProject().getId(),
                        member.getMember().getId(),
                        member.getMember().getEmail(),
                        member.getRole().name()
                ))
                .toList();

        return new ProjectDetailResponse(
                proj.getId(),
                proj.getName(),
                proj.getDescription(),
                members
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


    /*

    LOGIC BUSINESS WITH MEMBER IN PROJECT

     */

    @Transactional
    @Override
    public void addMember(AddMemberRequest request){

        User existingUser = userRepository.findByEmail(request.memberEmail())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Project existingProj = projectRepository.findById(request.projectId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Project not found"));

        if (memberRepository.existsByProjectAndMember(existingProj, existingUser))
        {
            throw new BadRequestException("User already exist in project");
        }

        if (!isAuthorized(existingProj, ProjectRole.OWNER)) {
            throw new AccessDeniedException("Only project owner can invite members");
        }

        ProjectMember newMember = new ProjectMember();
        newMember.setProject(existingProj);
        newMember.setMember(existingUser);
        newMember.setRole(request.role());
        memberRepository.save(newMember);
    }

    @Transactional
    @Override
    public void removeMember(RemoveMemberRequest request) {

        Project existingProject = projectRepository.findById(request.projectId())
                        .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        if (!isAuthorized(existingProject, ProjectRole.OWNER)) {
                throw new AccessDeniedException("Only project owner can remove members");
        }

        ProjectMember member = memberRepository
                    .findByProject_IdAndMember_Id(request.projectId(),
                            request.memberId())
                                .orElseThrow(() -> new ResourceNotFoundException("Member not found in project"));


        if (member.getRole() == ProjectRole.OWNER) {
                throw new BadRequestException("Project owner cannot be removed");
        }

        memberRepository.delete(member);
    }

    /*

    HELPER FUNCTION

     */

    // HELPER FUNCTION RETRIEVE CURRENT USER
    public User getCurrentUser(){

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() ->
                        new BadRequestException("Bad credential"));
    }

    // HELPER AUTHORIZE PROJECT ROLE (SCOPE IN PROJECT)
    public boolean isAuthorized(Project project, ProjectRole requiredRole) {

        User currentUser = getCurrentUser();

        ProjectMember membership = memberRepository
                .findByProject_IdAndMember_Id(project.getId(), currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Member not found in project"));

        return membership.getRole() == requiredRole;
    }

    // HELPER CREATE PROJECT
    public Project initialProject(CreateProjectRequest request){
        Project newProject = new Project(
                request.name(),
                request.description()
        );

        projectRepository.save(newProject);
        return newProject;
    }

}
