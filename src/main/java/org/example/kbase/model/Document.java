package org.example.kbase.model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "documents")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "project_id", nullable = false)
    private UUID projectId;

    @Column(name = "uploaded_by")
    private UUID ownerId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "file_type", nullable = false)
    private String type;

    @Column(name = "file_size", nullable = false)
    private Long size;

    @Column(name = "storage_key", nullable = false, unique = true)
    private String storageKey;

    @Column(name = "uploaded_at")
    private LocalDateTime uploadedAt;

    @PrePersist
    public void onCreate() {
        if (uploadedAt == null) {
            uploadedAt = LocalDateTime.now();
        }
    }

}
