package com.project.web.repository;

import com.project.web.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department,Long> {
    Boolean existsByDepartmentName(String departmentName);
}
