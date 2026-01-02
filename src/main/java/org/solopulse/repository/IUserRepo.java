package org.solopulse.repository;

import org.solopulse.entity.User;
import org.solopulse.enums.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IUserRepo extends JpaRepository<User, Integer>
{

    Optional<User> findByEmail(String email);

    Optional<User> findByName(String name);

    boolean existsByEmail(String email);

    boolean existsByName(String name);

    List<User> findByUserRole(Roles userRole);

    List<User> findByUserRoleOrderByCreatedAtDesc(Roles userRole);

    boolean exsitsByUserRole(Roles userRole);

    List<User> findByNameContainingIgnoreCase(String name);

    long countByUserRole(Roles userRole);

    List<User> findByBioIsNotNull();

    List<User> findByBioIsNull();

    List<User> findByCreatedAtAfter(LocalDateTime date);

    List<User> findByCreatedAtBefore(LocalDateTime date);

    List<User> findByUpdatedAtAfter(LocalDateTime date);

    List<User> findTop10ByOrderByUpdatedAtDesc();

    List<User> findTop10ByOrderByCreatedAtDesc();

    // Profile based queries

    List<User> findByCreatorProfileIsNotNull();

    List<User> findByBrandProfileIsNotNull();

    List<User> findByMarketerProfileIsNotNull();

    List<User> findByProfileIsNotNull();

    List<User> findByProposalIsNotNull();

    @Query("SELECT u FROM User u WHERE u.creatorProfile IS NULL AND u.brandProfile IS NULL " +
            "AND u.marketerProfile IS NULL AND u.profile IS NULL")
    List<User> findUsersWithoutAnyProfile();

    @Query("SELECT u FROM User u WHERE u.userRole = :role AND " +
            "(u.creatorProfile IS NOT NULL OR u.brandProfile IS NOT NULL OR " +
            "u.marketerProfile IS NOT NULL OR u.profile IS NOT NULL)")
    List<User> findUsersByRoleWithProfiles(@Param("role") Roles role);


    @Query("SELECT u.userRole, COUNT(u) FROM User u GROUP BY u.userRole")
    List<Object[]> getUserStatisticsByRole();


}
