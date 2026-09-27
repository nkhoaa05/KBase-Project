package org.example.kbase.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.example.kbase.model.Project;
import org.example.kbase.model.ProjectMember;
import org.example.kbase.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectMemberRepository extends JpaRepository<ProjectMember, UUID> {

    List<ProjectMember> findAllByProjectId(UUID projectId);

    boolean existsByProjectAndMember(Project project, User member);

    Optional<ProjectMember> findByProject_IdAndMember_Id(UUID project,
                                                         UUID member);
}
