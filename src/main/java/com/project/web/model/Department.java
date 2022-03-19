package com.project.web.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Data
@Table(name = "Departments")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long departmentId;
    @NotEmpty(message = "*Please provide department name")
    private String departmentName;
    public Department() {
    }
}
