package org.solopulse.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.solopulse.enums.Project_status;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "collaboration_projects")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Collaboration_projects {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Integer id;

    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "project_status")
    private Project_status status;

    private String deliverables; // link to deliverables

    private String deadLines;

    private Double price;

    @Column(columnDefinition = "TEXT")
    private String overview;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne
    private Creator_profile creatorProfile;

    @ManyToOne
    @JoinColumn(name = "marketer_profile_id", nullable = false)
    private Marketer_profile marketerProfile;

    @OneToOne
    @JoinColumn(name = "proposal_id", referencedColumnName = "id")
    private Proposals proposal;

    @ManyToOne
    @JoinColumn(name = "brand_profile_id", nullable = false)
    private Brand_profile brandProfile;
}
