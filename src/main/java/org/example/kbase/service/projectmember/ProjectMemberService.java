package org.example.kbase.service.projectmember;

import org.example.kbase.dto.request.UpdateProjectMemberRequest;
import org.example.kbase.dto.response.ProjectMemberResponse;
import org.example.kbase.model.Enum.ProjectRole;
import org.example.kbase.model.Project;
import org.example.kbase.model.ProjectMember;
import org.example.kbase.model.User;
import org.example.kbase.repository.ProjectMemberRepository;
import org.example.kbase.repository.ProjectRepository;
import org.example.kbase.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class ProjectMemberService implements IProjectMemberService {

    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ProjectMemberService(ProjectMemberRepository projectMemberRepository,
                                ProjectRepository projectRepository,
                                UserRepository userRepository) {
        this.projectMemberRepository = projectMemberRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void createMember(UUID projectId, UUID memberId) {
        Project project = findProject(projectId);
        User user = findUser(memberId);

        if (projectMemberRepository.existsByProjectIdIdAndMemberIdId(projectId, memberId)) {
            throw new RuntimeException("User is already a member of this project");
        }

        ProjectMember member = new ProjectMember();
        member.setProjectId(project);
        member.setMemberId(user);
        member.setRole(ProjectRole.OWNER);
        projectMemberRepository.save(member);
    }

    @Override
    @Transactional
    public void addMember(UUID projectId, String memberEmail) {
        Project project = findProject(projectId);
        User user = userRepository.findByEmail(memberEmail)
                .orElseThrow(() -> new RuntimeException("User not found: Email " + memberEmail));

        if (projectMemberRepository.existsByProjectIdIdAndMemberIdId(projectId, user.getId())) {
            throw new RuntimeException("User is already a member of this project");
        }

        ProjectMember member = new ProjectMember();
        member.setProjectId(project);
        member.setMemberId(user);
        member.setRole(ProjectRole.MEMBER);
        projectMemberRepository.save(member);
    }

    @Override
    public List<ProjectMemberResponse> getMembers(UUID projectId) {
        findProject(projectId);
        return projectMemberRepository.findAllByProjectIdId(projectId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public ProjectMemberResponse getMember(UUID projectId, UUID memberId) {
        return toResponse(findMember(projectId, memberId));
    }

    @Override
    @Transactional
    public void updateMember(UUID projectId, UUID memberId, UpdateProjectMemberRequest request) {
        ProjectMember member = findMember(projectId, memberId);
        member.setRole(request.role());
    }

    @Override
    @Transactional
    public void deleteMember(UUID projectId, UUID memberId) {
        projectMemberRepository.delete(findMember(projectId, memberId));
    }

    private Project findProject(UUID projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: ID " + projectId));
    }

    private User findUser(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: ID " + userId));
    }

    private ProjectMember findMember(UUID projectId, UUID memberId) {
        return projectMemberRepository.findByIdAndProjectIdId(memberId, projectId)
                .orElseThrow(() -> new RuntimeException("Project member not found: ID " + memberId));
    }

    private ProjectMemberResponse toResponse(ProjectMember member) {
        return new ProjectMemberResponse(
                member.getId(),
                member.getProjectId().getId(),
                member.getMemberId().getId(),
                member.getRole().name()
        );
    }
}
