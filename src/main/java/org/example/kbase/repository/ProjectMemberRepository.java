package org.example.kbase.repository;

import org.example.kbase.model.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProjectMemberRepository extends JpaRepository<ProjectMember, UUID> {

    List<ProjectMember> findAllByProjectIdId(UUID projectId);

    Optional<ProjectMember> findByIdAndProjectIdId(UUID memberId, UUID projectId);

    boolean existsByProjectIdIdAndMemberIdId(UUID projectId, UUID userId);
}
