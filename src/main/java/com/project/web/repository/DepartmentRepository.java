package com.project.web.repository;

import com.project.web.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department,Long> {
    Boolean existsByDepartmentName(String departmentName);
    @Query(value = "SELECT departments.department_name AS department ,count(*) AS \"count\"\n" +
            "FROM departments, users, ideas\n" +
            "WHERE departments.department_id= users.department_id\n" +
            "    and users.user_id=ideas.user_id\n" +
            "GROUP BY departments.department_name", nativeQuery = true)
    List<IdeaChart> getIdeaChart();
    interface IdeaChart {
        String getDepartment();
        int getCount();
    }
}
