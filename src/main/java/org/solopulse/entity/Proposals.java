package org.solopulse.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.solopulse.enums.Project_status;

import java.time.LocalDateTime;

@Entity
@Table(name = "proposals")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Proposals {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Integer id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String deliverables;

    private String deadlines;

    private Double price;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Enumerated(EnumType.STRING)
    private Project_status status;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "proposal", cascade = CascadeType.ALL)
    private Collaboration_projects collaborationProject;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
