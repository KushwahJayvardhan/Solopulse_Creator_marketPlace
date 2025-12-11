package org.solopulse.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.solopulse.enums.Industry;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "marketer_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Marketer_profile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ElementCollection
    @CollectionTable(name = "marketer_services_offered")
    private List<String> servicesOffered = new ArrayList<>();

    private Integer experience_years;

    @Column(columnDefinition = "TEXT")
    private String caseStudiesLinks;

    @ElementCollection(targetClass = Industry.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "marketer_specialties")
    private List<Industry> specialties = new ArrayList<>();

    private Double avgCampaignBudget;

    @ElementCollection
    @CollectionTable(name = "marketer_tools_used")
    private List<String> toolsUsed = new ArrayList<>();

    @ManyToMany
    private List<Creator_profile> associatedCreators = new ArrayList<>();

    @ManyToMany(mappedBy = "marketers")
    private List<Brand_profile>  associatedBrands = new ArrayList<>();

    @OneToMany(mappedBy = "marketerProfile", cascade = CascadeType.ALL)
    private List<Collaboration_projects> managedProjects = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;
}
