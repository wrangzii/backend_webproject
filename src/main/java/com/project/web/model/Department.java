package com.project.web.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "Departments")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long departmentId;
    private String departmentName;
    public Department() {
    }
}
