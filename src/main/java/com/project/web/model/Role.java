package com.project.web.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ERole roleName;

    public Role(Long roleId, ERole roleName) {
        this.roleId = roleId;
        this.roleName = roleName;
    }

    public Role() {}
}
