package org.example.kbase.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.kbase.model.Enum.ProjectRole;

import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "project_members",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_project_member",
                        columnNames = {"project_id", "member_id"}
                )
        }
)
public class ProjectMember {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    private Project projectId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private User memberId;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private ProjectRole role;
}
