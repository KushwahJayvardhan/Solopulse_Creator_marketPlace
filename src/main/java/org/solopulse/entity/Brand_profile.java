package org.solopulse.entity;

import jakarta.persistence.*;
import lombok.*;
import org.solopulse.enums.Industry;
import org.solopulse.enums.Platform;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "brand_profiles")
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class Brand_profile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Integer id;

    @Column(nullable = false, updatable = false)
    private String brandName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Industry industry;

    @ElementCollection
    @CollectionTable(name = "brand_marketing_goals")
    private  List<String> marketingGoals;

    @Column(columnDefinition = "TEXT")
    private String targetAudience;

    @Column(nullable = false)
    private Integer budget_range_min;

    private Long budget_range_max;

    private Integer pastCampaigns;

    @Enumerated(EnumType.STRING)
    @ElementCollection
    @CollectionTable(name = "brand_preferred_platforms")
    private List<Platform> preferredPlatforms;

    private String brand_guidelines; // link to brand guidelines document

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "brandProfile", cascade = CascadeType.ALL)
    private List<Collaboration_projects> collaborationProjects = new ArrayList<>();

    @ManyToMany
    private List<Marketer_profile> marketers = new ArrayList<>();

}
