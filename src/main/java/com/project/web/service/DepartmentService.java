package com.project.web.service;

import com.project.web.model.Department;
import com.project.web.payload.response.MessageResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface DepartmentService {
    List<Department> getAllDepartment();
    ResponseEntity<MessageResponse> addDepartment(Department department);
    ResponseEntity<MessageResponse> deleteDepartment(Long id);
    ResponseEntity<MessageResponse> editDepartment(Department department, Long id);

}
