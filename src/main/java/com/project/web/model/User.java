package com.project.web.model;

import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String email;
    private String username;
    private String fullName;
    private String phoneNumber;
    private String dateOfBirth;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
                    joinColumns = @JoinColumn(name = "userId"),
                    inverseJoinColumns = @JoinColumn(name = "roleId"))
    private Set<Role> roles = new HashSet<>();
    @ManyToOne()
    @JoinColumn(name = "departmentId")
    private Department departmentId;
    private String password;

    public User(String email, String username, String fullName, String phoneNumber, String dateOfBirth, Department departmentId, String password) {
        this.username = username;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.departmentId = departmentId;
        this.password = password;
        this.email = email;
    }

    public User() {}

    public User(String username, String email, String encode) {
    }

    public Set<Role> getRoles() {
        return roles;
    }
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
    public Department getDepartmentId() {
        return departmentId;
    }
    public void setDepartmentId(Department departmentId) {
        this.departmentId = departmentId;
    }

}
