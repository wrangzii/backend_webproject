package com.project.web.model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @Email(message = "*Please provide a valid Email")
    @NotEmpty(message = "*Please provide an email")
    private String email;
    private String username;
    @NotEmpty(message = "*Please provide full name")
    private String fullName;
    @NotEmpty(message = "*Please provide phone number")
    private String phoneNumber;
    private Date dateOfBirth;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
                    joinColumns = @JoinColumn(name = "userId"),
                    inverseJoinColumns = @JoinColumn(name = "roleId"))
    private Set<Role> roles = new HashSet<>();
    @ManyToOne()
    @JoinColumn(name = "departmentId")
    private Department departmentId;
    @Length(min = 5, message = "*Your password must have at least 5 characters")
    @NotEmpty(message = "*Please provide your password")
    private String password;
    private Boolean enabled;
    private String resetPasswordToken;

    public User(String email, String username, String fullName, String phoneNumber, Date dateOfBirth, Department departmentId, String password) {
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
