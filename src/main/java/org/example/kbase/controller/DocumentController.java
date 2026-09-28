package org.example.kbase.controller;


import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.kbase.common.response.ApiResponse;
import org.example.kbase.model.Document;
import org.example.kbase.service.document.DocumentService;
import org.example.kbase.service.document.IStorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/projects/{projectId}/documents")
@Tag(name = "Document APIs")
public class DocumentController {

    private final DocumentService documentService;
    private final IStorageService storageService;


    // Upload docs
    @Operation(
            summary = "Upload a doc"
    )
    @PostMapping
        public ResponseEntity<ApiResponse<Document>> upload(
            @PathVariable UUID projectId,
            @RequestParam("file") MultipartFile file
    ) {
        Document document =
                documentService.upload(
                        projectId,
                        file
                );

        return ResponseEntity.ok(ApiResponse.success("Document uploaded successfully", document));
    }


    // Get a list of docs
    @Operation(
            summary = "Get a list of docs"
    )
    @GetMapping
        public ResponseEntity<ApiResponse<List<Document>>> list(
            @PathVariable UUID projectId
    ) {

        return ResponseEntity.ok(ApiResponse.success(
                "Documents retrieved successfully",
                documentService.getDocuments(projectId)
        ));
    }

    // Download docs
    @Operation(
            summary = "Download docs"
    )
    @GetMapping("/{documentId}/download")
    public ResponseEntity<Resource> download(
            @PathVariable UUID projectId,
            @PathVariable UUID documentId
    ) {

        Document document =
                documentService.getDocument(projectId, documentId);

        Resource resource =
                storageService.load(
                        document.getStorageKey()
                );

        MediaType contentType = MediaType.APPLICATION_OCTET_STREAM;
        if (document.getType() != null) {
            try {
                contentType = MediaType.parseMediaType(document.getType());
            } catch (IllegalArgumentException ignored) {
                // Use the binary fallback for an invalid client-supplied MIME type.
            }
        }

        String downloadName = document.getName()
                .replaceAll("[\\r\\n\\\"]", "_");

        return ResponseEntity.ok()
                .contentType(contentType)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\""
                                + downloadName
                                + "\""
                )
                .contentLength(document.getSize())
                .body(resource);
    }

    // Delete docs
    @Operation(
            summary = "Delete a doc"
    )
    @DeleteMapping("/{documentId}")
        public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable UUID projectId,
            @PathVariable UUID documentId
    ) {

        documentService.deleteDocument(
                projectId,
                documentId
        );

        return ResponseEntity.ok(ApiResponse.success("Document deleted successfully", null));
    }

}
