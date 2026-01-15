package org.solopulse.repository;

import org.solopulse.entity.User;
import org.solopulse.enums.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface UserRepo extends JpaRepository<User, Integer>
{

    Optional<User> findById(Integer id);

    Optional<User> findByEmail(String email);

    Optional<User> findByName(String name);

    boolean existsByEmail(String email);

    boolean existsByName(String name);

    List<User> findByUserRole(Roles userRole);

    List<User> findByUserRoleOrderByCreatedAtDesc(Roles userRole);

    //boolean exsitsByUserRole(Roles userRole);

    List<User> findByNameContainingIgnoreCase(String name);

    long countByUserRole(Roles userRole);

    //List<User> findByBioIsNotNull();

    //List<User> findByBioIsNull();

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

    //List<User> findByProposalIsNotNull();

    @Query("SELECT u FROM User u WHERE u.creatorProfile IS NULL AND u.brandProfile IS NULL " +
            "AND u.marketerProfile IS NULL AND u.profile IS NULL")
    List<User> findUsersWithoutAnyProfile();

    @Query("SELECT u FROM User u WHERE u.userRole = :role AND " +
            "(u.creatorProfile IS NOT NULL OR u.brandProfile IS NOT NULL OR " +
            "u.marketerProfile IS NOT NULL OR u.profile IS NOT NULL)")
    List<User> findUsersByRoleWithProfiles(@Param("role") Roles role);

    @Query("SELECT u FROM User u WHERE u.updatedAt >= :thirtyDaysAgo")
    List<User> findActiveUsers(@Param("thirtyDaysAgo") LocalDateTime thirtyDaysAgo);

    @Query("SELECT u.userRole, COUNT(u) FROM User u GROUP BY u.userRole")
    List<Object[]> getUserStatisticsByRole();


    @Modifying
    @Query("UPDATE User u SET u.imageUrl = :imageUrl WHERE u.id = :userId ")
    int updateUserImage(@Param("userId") Integer userId, @Param("imageUrl") String imageUrl);

    @Modifying
    @Query("UPDATE User u SET u.userRole = :role WHERE u.id = :userId")
    int updateUserRole(@Param("userId") Integer userId, @Param("role") Roles role);

    @Modifying
    @Query("UPDATE User u SET u.bio = :bio WHERE u.id = :userId")
    int updateUserBio(@Param("userId") Integer userId, @Param("bio") String bio);

    @Modifying
    @Query("UPDATE User u SET u.password = :password WHERE u.id = :userId")
    int updateUserPassword(@Param("userId") Integer userId, @Param("password") String password);


}
