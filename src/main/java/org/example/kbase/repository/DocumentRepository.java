package org.example.kbase.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.example.kbase.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<Document, UUID> {

    List<Document> findByProjectId(UUID projectId);

    Optional<Document> findByIdAndProjectId(UUID id, UUID projectId);

}
