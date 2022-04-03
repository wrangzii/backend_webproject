package com.project.web.repository;

import com.project.web.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    @Query(value = "SELECT * FROM users WHERE enabled = 'true' AND username = ?1 ", nativeQuery = true)
    Optional<User> findByUsername(String username);

    @Query(value = "SELECT * FROM users WHERE enabled = 'true' AND email = ?1 ", nativeQuery = true)
    Optional<User> findByEmail(String email);

    @Query(value = "SELECT * FROM users WHERE enabled = 'true' AND department_id = ?1 ", nativeQuery = true)
    List<User> findByDepartmentId(Long departmentId);

    @Query(value = "SELECT * FROM users WHERE enabled = 'true'", nativeQuery = true)
    Page<User> findAll(Pageable pageable);

    User findByResetPasswordToken(String confirmationToken);
}
