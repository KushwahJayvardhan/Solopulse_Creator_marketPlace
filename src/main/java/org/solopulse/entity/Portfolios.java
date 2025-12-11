package org.solopulse.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.hibernate.annotations.CreationTimestamp;
import org.solopulse.enums.ContentFormat;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "portfolios")
@EntityListeners(AuditingEntityListener.class)
public class Portfolios {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private  Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    private  String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String fileUrl;  // video,image, document link

    @Enumerated(EnumType.STRING)
    private ContentFormat contentFormat;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(columnDefinition = "TEXT")
    private  String results;  // outcomes or achievements from the project

    @ManyToOne
    private Creator_profile creatorProfile;
}
