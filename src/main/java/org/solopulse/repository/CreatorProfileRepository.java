package org.solopulse.repository;

import org.solopulse.entity.Creator_profile;
import org.solopulse.enums.ContentFormat;
import org.solopulse.enums.Platform;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CreatorProfileRepository extends JpaRepository<Creator_profile, Integer>,
        JpaSpecificationExecutor<Creator_profile> {

    // ==================== User-related queries ====================

    @EntityGraph(attributePaths = {"user", "portfolioItems", "platforms", "contentFormats"})
    Optional<Creator_profile> findByUserId(Integer userId);

    boolean existsByUserId(Integer userId);

    // ==================== Social Media Handles ====================

    Optional<Creator_profile> findByInstaHandle(String instaHandle);
    Optional<Creator_profile> findByYoutubeHandle(String youtubeHandle);
    Optional<Creator_profile> findByTwitterHandle(String twitterHandle);
    Optional<Creator_profile> findByLinkedInHandle(String linkedInHandle);
    Optional<Creator_profile> findByFacebookHandle(String facebookHandle);

    boolean existsByInstaHandle(String instaHandle);
    boolean existsByYoutubeHandle(String youtubeHandle);
    boolean existsByTwitterHandle(String twitterHandle);

    // ==================== Paginated Search Queries (Frontend-friendly) ====================

    // Basic pagination
    Page<Creator_profile> findAll(Pageable pageable);

    // Location-based with pagination
    Page<Creator_profile> findByLocation(String location, Pageable pageable);
    Page<Creator_profile> findByLocationContainingIgnoreCase(String location, Pageable pageable);

    // Pricing tier with pagination
    Page<Creator_profile> findByPricingTier(String pricingTier, Pageable pageable);

    // Followers range with pagination
    Page<Creator_profile> findByFollowersCountGreaterThanEqual(Long minFollowers, Pageable pageable);
    Page<Creator_profile> findByFollowersCountBetween(Long minFollowers, Long maxFollowers, Pageable pageable);

    // Engagement rate with pagination
    Page<Creator_profile> findByEngagementRateGreaterThanEqual(Double minEngagementRate, Pageable pageable);
    Page<Creator_profile> findByEngagementRateBetween(Double minRate, Double maxRate, Pageable pageable);

    // ==================== Content Format & Platform Queries ====================

    @Query("SELECT DISTINCT c FROM Creator_profile c JOIN c.contentFormats cf WHERE cf = :format")
    Page<Creator_profile> findByContentFormat(@Param("format") ContentFormat format, Pageable pageable);

    @Query("SELECT DISTINCT c FROM Creator_profile c JOIN c.contentFormats cf WHERE cf IN :formats")
    Page<Creator_profile> findByContentFormatsIn(@Param("formats") List<ContentFormat> formats, Pageable pageable);

    @Query("SELECT DISTINCT c FROM Creator_profile c JOIN c.platforms p WHERE p = :platform")
    Page<Creator_profile> findByPlatform(@Param("platform") Platform platform, Pageable pageable);

    @Query("SELECT DISTINCT c FROM Creator_profile c JOIN c.platforms p WHERE p IN :platforms")
    Page<Creator_profile> findByPlatformsIn(@Param("platforms") List<Platform> platforms, Pageable pageable);

    // ==================== Advanced Search with Pagination ====================

    @Query("SELECT DISTINCT c FROM Creator_profile c " +
            "LEFT JOIN c.platforms p " +
            "LEFT JOIN c.contentFormats cf " +
            "WHERE (:location IS NULL OR LOWER(c.location) LIKE LOWER(CONCAT('%', :location, '%'))) " +
            "AND (:minFollowers IS NULL OR c.followersCount >= :minFollowers) " +
            "AND (:maxFollowers IS NULL OR c.followersCount <= :maxFollowers) " +
            "AND (:minEngagementRate IS NULL OR c.engagementRate >= :minEngagementRate) " +
            "AND (:pricingTier IS NULL OR c.pricingTier = :pricingTier) " +
            "AND (:platform IS NULL OR p IN :platforms) " +
            "AND (:contentFormat IS NULL OR cf IN :contentFormats)")
    Page<Creator_profile> searchCreators(
            @Param("location") String location,
            @Param("minFollowers") Long minFollowers,
            @Param("maxFollowers") Long maxFollowers,
            @Param("minEngagementRate") Double minEngagementRate,
            @Param("pricingTier") String pricingTier,
            @Param("platform") Platform platform,
            @Param("platforms") List<Platform> platforms,
            @Param("contentFormat") ContentFormat contentFormat,
            @Param("contentFormats") List<ContentFormat> contentFormats,
            Pageable pageable);

    // ==================== Optimized queries with EntityGraph (N+1 prevention) ====================

    @EntityGraph(attributePaths = {"portfolioItems", "platforms", "contentFormats"})
    @Query("SELECT c FROM Creator_profile c WHERE c.id = :id")
    Optional<Creator_profile> findByIdWithDetails(@Param("id") Integer id);

    @EntityGraph(attributePaths = {"portfolioItems", "platforms", "contentFormats", "user"})
    @Query("SELECT c FROM Creator_profile c")
    Page<Creator_profile> findAllWithDetails(Pageable pageable);

    @EntityGraph(attributePaths = {"portfolioItems"})
    @Query("SELECT c FROM Creator_profile c WHERE c.location = :location")
    Page<Creator_profile> findByLocationWithPortfolio(@Param("location") String location, Pageable pageable);

    // ==================== Analytics & Statistics ====================

    Long countByLocation(String location);
    Long countByPricingTier(String pricingTier);

    @Query("SELECT COUNT(DISTINCT c) FROM Creator_profile c JOIN c.platforms p WHERE p = :platform")
    Long countByPlatform(@Param("platform") Platform platform);

    @Query("SELECT AVG(c.engagementRate) FROM Creator_profile c WHERE c.location = :location")
    Double getAverageEngagementRateByLocation(@Param("location") String location);

    @Query("SELECT c.pricingTier, COUNT(c) FROM Creator_profile c GROUP BY c.pricingTier")
    List<Object[]> getCreatorDistributionByPricingTier();

    // ==================== Featured/Top Creators ====================

    @Query("SELECT c FROM Creator_profile c ORDER BY c.followersCount DESC")
    Page<Creator_profile> findTopCreatorsByFollowers(Pageable pageable);

    @Query("SELECT c FROM Creator_profile c ORDER BY c.engagementRate DESC")
    Page<Creator_profile> findTopCreatorsByEngagement(Pageable pageable);

    @Query("SELECT c FROM Creator_profile c WHERE c.engagementRate >= :minRate ORDER BY c.followersCount DESC")
    Page<Creator_profile> findFeaturedCreators(@Param("minRate") Double minRate, Pageable pageable);

    // ==================== Search by Bio (for keyword search) ====================

    @Query("SELECT c FROM Creator_profile c WHERE LOWER(c.bio) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Creator_profile> searchByBioKeyword(@Param("keyword") String keyword, Pageable pageable);
}
