package org.solopulse.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.solopulse.enums.Industry;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "profiles")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Inheritance(strategy = InheritanceType.JOINED)
public class Profile {
     @Id
     @GeneratedValue(strategy = GenerationType.AUTO)
     @Column(nullable = false, updatable = false)
     private Integer id;

     @OneToOne
     @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
     private User user;

     @ElementCollection(targetClass = Industry.class)
     @Enumerated(EnumType.STRING)
     @CollectionTable(name = "profile_niches")
     private List<Industry> niches = new ArrayList<>();

     @Column(nullable = false)
     private String location;

     @Column(columnDefinition = "TEXT")
     private String portfolioLinks;

     private Double rating = 0.0;

     private Integer reviewsCount = 0;

     @CreationTimestamp
     @Column(nullable = false, updatable = false)
     private LocalDateTime createdAt;

     @UpdateTimestamp
     private LocalDateTime updatedAt;

     @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL)
     private List<Portfolios> portfolioItems = new ArrayList<>();


}
