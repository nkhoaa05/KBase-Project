package org.example.kbase.service.document;

import java.util.List;
import java.util.UUID;

import org.example.kbase.common.exception.ResourceNotFoundException;
import org.example.kbase.common.utils.CurrentUserUtil;
import org.example.kbase.model.Document;
import org.example.kbase.model.Enum.ProjectRole;
import org.example.kbase.model.Project;
import org.example.kbase.model.ProjectMember;
import org.example.kbase.model.User;
import org.example.kbase.repository.DocumentRepository;
import org.example.kbase.repository.ProjectMemberRepository;
import org.example.kbase.repository.ProjectRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final IStorageService storageService;
    private final CurrentUserUtil currentUserUtil;
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;

    @Transactional
    public Document upload(
            UUID projectId,
            MultipartFile file
    ) {

        User currentUser = currentUserUtil.getCurrentUser();
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        requireMember(project, currentUser);

        validateFile(file);

        String storageKey =
                generateStorageKey(projectId, file);

        try {

            storageService.store(
                    file,
                    storageKey
            );

            Document document = new Document();

            document.setProjectId(projectId);
            document.setOwnerId(currentUser.getId());
            document.setName(
                    file.getOriginalFilename()
            );
            document.setType(
                    file.getContentType()
            );
            document.setSize(
                    file.getSize()
            );
            document.setStorageKey(
                    storageKey
            );

            return documentRepository.save(document);

        } catch (Exception e) {
            try {
                storageService.delete(storageKey);
            } catch (Exception ignored) {
            }

            throw e;
        }
    }

    public List<Document> getDocuments(
            UUID projectId
    ) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        requireMember(project, currentUserUtil.getCurrentUser());

        return documentRepository.findByProjectId(projectId);
    }

    public Document getDocument(UUID projectId, UUID id) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        requireMember(project, currentUserUtil.getCurrentUser());

        return documentRepository.findByIdAndProjectId(id, projectId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Document not found")
                );
    }

    @Transactional
    public void deleteDocument(UUID projectId, UUID id) {

        Document document = getDocument(projectId, id);

        User currentUser = currentUserUtil.getCurrentUser();

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        ProjectMember membership = requireMember(project, currentUser);

        if (!currentUser.getId().equals(document.getOwnerId())
                && membership.getRole() != ProjectRole.OWNER) {
            throw new AccessDeniedException("Only the uploader or project owner can delete the document");
        }

        storageService.delete(
                document.getStorageKey()
        );

        documentRepository.delete(document);
    }

    private void validateFile(
            MultipartFile file
    ) {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException(
                    "File is empty"
            );
        }

        if (file.getOriginalFilename() == null || file.getOriginalFilename().isBlank()) {
            throw new IllegalArgumentException(
                    "File name is missing"
            );
        }
    }

    private String generateStorageKey(
            UUID projectId,
            MultipartFile file
    ) {

        String filename =
                file.getOriginalFilename();

                String extension = "";
                int dotIndex = filename.lastIndexOf('.');
                if (dotIndex >= 0 && dotIndex < filename.length() - 1) {
                        String candidate = filename.substring(dotIndex + 1);
                        if (candidate.matches("[A-Za-z0-9]{1,20}")) {
                                extension = "." + candidate;
                        }
                }

        return "projects/"
                + projectId
                + "/documents/"
                + UUID.randomUUID()
                + extension;
    }

        private ProjectMember requireMember(Project project, User user) {
                return projectMemberRepository.findByProject_IdAndMember_Id(project.getId(), user.getId())
                                .orElseThrow(() -> new AccessDeniedException("User is not a member of the project"));
        }

}
