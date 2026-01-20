package org.solopulse.dao;

import org.solopulse.entity.User;
import org.solopulse.enums.Roles;
import org.solopulse.repository.UserRepo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class UserDao {

    private final UserRepo userRepo;

    public UserDao(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    // Basic CRUD operations

    public User createUser(User user) {
        return userRepo.save(user);
    }

    public Optional<User> findUserById(Integer id) {
        return userRepo.findById(id);
    }

    public List<User> findAll() {
        return userRepo.findAll();
    }

    public void deleteById(Integer id) {
        userRepo.deleteById(id);
    }

    // Finders

    public Optional<User> findByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    public Optional<User> findByName(String name) {
        return userRepo.findByName(name);
    }

    public boolean existsByEmail(String email) {
        return userRepo.existsByEmail(email);
    }

    public boolean existsByName(String name) {
        return userRepo.existsByName(name);
    }

    public List<User> findByUserRole(Roles role) {
        return userRepo.findByUserRole(role);
    }

    public List<User> findByUserRoleOrderByCreatedAtDesc(Roles role) {
        return userRepo.findByUserRoleOrderByCreatedAtDesc(role);
    }

    public long countByUserRole(Roles role) {
        return userRepo.countByUserRole(role);
    }

    public List<User> searchByName(String name) {
        return userRepo.findByNameContainingIgnoreCase(name);
    }

    // Profile based queries

    public List<User> findUsersWithCreatorProfile() {
        return userRepo.findByCreatorProfileIsNotNull();
    }

    public List<User> findUsersWithBrandProfile() {
        return userRepo.findByBrandProfileIsNotNull();
    }

    public List<User> findUsersWithMarketerProfile() {
        return userRepo.findByMarketerProfileIsNotNull();
    }

    public List<User> findUsersWithAnyProfile() {
        return userRepo.findByProfileIsNotNull();
    }

    public List<User> findUsersWithoutAnyProfile() {
        return userRepo.findUsersWithoutAnyProfile();
    }

    public List<User> findUsersByRoleWithProfiles(Roles role) {
        return userRepo.findUsersByRoleWithProfiles(role);
    }

    // Activity and date based queries

    public List<User> findUsersCreatedAfter(LocalDateTime date) {
        return userRepo.findByCreatedAtAfter(date);
    }

    public List<User> findUsersCreatedBefore(LocalDateTime date) {
        return userRepo.findByCreatedAtBefore(date);
    }

    public List<User> findUsersUpdatedAfter(LocalDateTime date) {
        return userRepo.findByUpdatedAtAfter(date);
    }

    public List<User> findActiveUsers(LocalDateTime thirtyDaysAgo) {
        return userRepo.findActiveUsers(thirtyDaysAgo);
    }

    public List<User> findLatestUpdatedUsers() {
        return userRepo.findTop10ByOrderByUpdatedAtDesc();
    }

    public List<User> findLatestCreatedUsers() {
        return userRepo.findTop10ByOrderByCreatedAtDesc();
    }

    // Statistics

    public List<Object[]> getUserStatisticsByRole() {
        return userRepo.getUserStatisticsByRole();
    }

    // Update operations

    public int updateUserImage(Integer userId, String imageUrl) {
        return userRepo.updateUserImage(userId, imageUrl);
    }

    public void updateUserRole(Integer userId, Roles role) {
        userRepo.updateUserRole(userId, role);
    }

    public void updateUserBio(Integer userId, String bio) {
        userRepo.updateUserBio(userId, bio);
    }

    public void updateUserPassword(Integer userId, String password) {
        userRepo.updateUserPassword(userId, password);
    }
}
