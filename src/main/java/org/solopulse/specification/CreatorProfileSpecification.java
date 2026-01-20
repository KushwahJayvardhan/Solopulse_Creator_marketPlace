package org.solopulse.specification;

import jakarta.persistence.criteria.*;
import org.solopulse.entity.Creator_profile;
import org.solopulse.enums.ContentFormat;
import org.solopulse.enums.Platform;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

/**
 * Specification class for dynamic query building for Creator_profile entity.
 * This enables flexible, type-safe filtering without writing custom JPQL queries.
 */
public class CreatorProfileSpecification {

    /**
     * Creates a composite Specification with all provided filters.
     * Null parameters are ignored (won't be added to the query).
     */
    public static Specification<Creator_profile> withFilters(
            String location,
            Long minFollowers,
            Long maxFollowers,
            Double minEngagementRate,
            String pricingTier,
            List<Platform> platforms,
            List<ContentFormat> contentFormats,
            String bioKeyword) {

        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Location filter - case-insensitive partial match
            if (location != null && !location.isBlank()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("location")),
                        "%" + location.toLowerCase() + "%"
                ));
            }

            // Minimum followers filter
            if (minFollowers != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("followersCount"), minFollowers
                ));
            }

            // Maximum followers filter
            if (maxFollowers != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.get("followersCount"), maxFollowers
                ));
            }

            // Minimum engagement rate filter
            if (minEngagementRate != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("engagementRate"), minEngagementRate
                ));
            }

            // Pricing tier filter - exact match
            if (pricingTier != null && !pricingTier.isBlank()) {
                predicates.add(criteriaBuilder.equal(
                        root.get("pricing_tier"), pricingTier
                ));
            }

            // Platforms filter - creator must have at least one of the specified platforms
            if (platforms != null && !platforms.isEmpty()) {
                Join<Object, Object> platformJoin = root.join("platforms", JoinType.INNER);
                predicates.add(platformJoin.in(platforms));
            }

            // Content formats filter - creator must have at least one of the specified formats
            if (contentFormats != null && !contentFormats.isEmpty()) {
                Join<Object, Object> formatJoin = root.join("contentFormats", JoinType.INNER);
                predicates.add(formatJoin.in(contentFormats));
            }

            // Bio keyword search - case-insensitive partial match
            if (bioKeyword != null && !bioKeyword.isBlank()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("bio")),
                        "%" + bioKeyword.toLowerCase() + "%"
                ));
            }

            // Prevent duplicate results when using joins
            query.distinct(true);

            // Combine all predicates with AND
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    /**
     * Filter by location only
     */
    public static Specification<Creator_profile> hasLocation(String location) {
        return (root, query, criteriaBuilder) -> {
            if (location == null || location.isBlank()) {
                return criteriaBuilder.conjunction(); // Always true
            }
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("location")),
                    "%" + location.toLowerCase() + "%"
            );
        };
    }

    /**
     * Filter by minimum followers count
     */
    public static Specification<Creator_profile> hasMinimumFollowers(Long minFollowers) {
        return (root, query, criteriaBuilder) -> {
            if (minFollowers == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.greaterThanOrEqualTo(
                    root.get("followersCount"), minFollowers
            );
        };
    }

    /**
     * Filter by followers range
     */
    public static Specification<Creator_profile> hasFollowersBetween(Long min, Long max) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (min != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("followersCount"), min
                ));
            }

            if (max != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.get("followersCount"), max
                ));
            }

            if (predicates.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    /**
     * Filter by minimum engagement rate
     */
    public static Specification<Creator_profile> hasMinimumEngagementRate(Double minRate) {
        return (root, query, criteriaBuilder) -> {
            if (minRate == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.greaterThanOrEqualTo(
                    root.get("engagementRate"), minRate
            );
        };
    }

    /**
     * Filter by engagement rate range
     */
    public static Specification<Creator_profile> hasEngagementRateBetween(Double min, Double max) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (min != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("engagementRate"), min
                ));
            }

            if (max != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.get("engagementRate"), max
                ));
            }

            if (predicates.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    /**
     * Filter by pricing tier
     */
    public static Specification<Creator_profile> hasPricingTier(String pricingTier) {
        return (root, query, criteriaBuilder) -> {
            if (pricingTier == null || pricingTier.isBlank()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("pricing_tier"), pricingTier);
        };
    }

    /**
     * Filter by platform
     */
    public static Specification<Creator_profile> hasPlatform(Platform platform) {
        return (root, query, criteriaBuilder) -> {
            if (platform == null) {
                return criteriaBuilder.conjunction();
            }

            Join<Object, Object> platformJoin = root.join("platforms", JoinType.INNER);
            query.distinct(true);

            return criteriaBuilder.equal(platformJoin, platform);
        };
    }

    /**
     * Filter by multiple platforms (OR condition)
     */
    public static Specification<Creator_profile> hasPlatforms(List<Platform> platforms) {
        return (root, query, criteriaBuilder) -> {
            if (platforms == null || platforms.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            Join<Object, Object> platformJoin = root.join("platforms", JoinType.INNER);
            query.distinct(true);

            return platformJoin.in(platforms);
        };
    }

    /**
     * Filter by content format
     */
    public static Specification<Creator_profile> hasContentFormat(ContentFormat contentFormat) {
        return (root, query, criteriaBuilder) -> {
            if (contentFormat == null) {
                return criteriaBuilder.conjunction();
            }

            Join<Object, Object> formatJoin = root.join("contentFormats", JoinType.INNER);
            query.distinct(true);

            return criteriaBuilder.equal(formatJoin, contentFormat);
        };
    }

    /**
     * Filter by multiple content formats (OR condition)
     */
    public static Specification<Creator_profile> hasContentFormats(List<ContentFormat> contentFormats) {
        return (root, query, criteriaBuilder) -> {
            if (contentFormats == null || contentFormats.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            Join<Object, Object> formatJoin = root.join("contentFormats", JoinType.INNER);
            query.distinct(true);

            return formatJoin.in(contentFormats);
        };
    }

    /**
     * Filter by bio keyword search
     */
    public static Specification<Creator_profile> hasBioKeyword(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.isBlank()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("bio")),
                    "%" + keyword.toLowerCase() + "%"
            );
        };
    }

    /**
     * Filter by social media handle (searches across all handles)
     */
    public static Specification<Creator_profile> hasSocialMediaHandle(String handle) {
        return (root, query, criteriaBuilder) -> {
            if (handle == null || handle.isBlank()) {
                return criteriaBuilder.conjunction();
            }

            String searchPattern = "%" + handle.toLowerCase() + "%";

            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("instaHandle")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("youtubeHandle")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("twitterHandle")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("linkedInHandle")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("facebookHandle")), searchPattern)
            );
        };
    }

    /**
     * Filter creators with availability calendar URL set
     */
    public static Specification<Creator_profile> hasAvailabilityCalendar() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.isNotNull(root.get("availabilityCalendarUrl"));
    }

    /**
     * Filter creators by user ID
     */
    public static Specification<Creator_profile> belongsToUser(Integer userId) {
        return (root, query, criteriaBuilder) -> {
            if (userId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("user").get("id"), userId);
        };
    }

    /**
     * Complex filter: Featured creators
     * (High engagement rate + good follower count)
     */
    public static Specification<Creator_profile> isFeatured(
            Double minEngagementRate, Long minFollowers) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (minEngagementRate != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("engagementRate"), minEngagementRate
                ));
            }

            if (minFollowers != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("followersCount"), minFollowers
                ));
            }

            if (predicates.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    /**
     * Combine multiple specifications with AND
     */
    public static Specification<Creator_profile> combineWithAnd(
            Specification<Creator_profile>... specifications) {

        Specification<Creator_profile> result = Specification.where(null);

        for (Specification<Creator_profile> spec : specifications) {
            if (spec != null) {
                result = result.and(spec);
            }
        }

        return result;
    }

    /**
     * Combine multiple specifications with OR
     */
    public static Specification<Creator_profile> combineWithOr(
            Specification<Creator_profile>... specifications) {

        Specification<Creator_profile> result = Specification.where(null);

        for (Specification<Creator_profile> spec : specifications) {
            if (spec != null) {
                result = result.or(spec);
            }
        }

        return result;
    }
}
