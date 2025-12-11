package org.solopulse.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.solopulse.enums.ContentFormat;
import org.solopulse.enums.Platform;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "creator_profiles")
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class Creator_profile {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String instaHandle;

    private String youtubeHandle;

    private String twitterHandle;

    private String linkedInHandle;

    private  String facebookHandle;

    @Column(columnDefinition = "TEXT")
    private String bio;

    private String location;

    private Long followersCount;

    private Double engagementRate;

    @ElementCollection(targetClass = ContentFormat.class)
    @CollectionTable(name= "creator_content_formats")
    @Enumerated(EnumType.STRING)
    private List<ContentFormat> contentFormats = new ArrayList<>();

    private String pricing_tier;

    private String availabilityCalendarUrl;

    @ElementCollection
    @CollectionTable(name = "creator_platforms")
    private List<Platform> platforms = new ArrayList<>();

    @OneToMany(mappedBy = "creatorProfile", cascade = CascadeType.ALL)
    private List<Portfolios> portfolioItems = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "creatorProfile", cascade = CascadeType.ALL)
    private List<Collaboration_projects> collaborationProjects = new ArrayList<>();

    @ManyToMany(mappedBy = "associatedCreators")
    private List<Marketer_profile> associatedMarketers = new ArrayList<>();
}
