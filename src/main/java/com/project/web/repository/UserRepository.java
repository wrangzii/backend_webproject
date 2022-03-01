package com.project.web.repository;

import com.project.web.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    @Query(value = "SELECT * FROM users WHERE enabled = 'true' AND username = ?1 ", nativeQuery = true)
    Optional<User> findByUsername(String username);
    @Query(value = "SELECT * FROM users WHERE enabled = 'true' AND email = ?1 ", nativeQuery = true)
    Optional<User> findByEmail(String email);
    @Query(value = "SELECT * FROM users WHERE enabled = 'true'", nativeQuery = true)
    List<User> getAllUser();
    User findByResetPasswordToken(String confirmationToken);
}

