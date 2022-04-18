package com.project.web.service;

import com.project.web.model.Department;
import com.project.web.payload.request.DepartmentRequest;
import com.project.web.payload.response.ResponseObject;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface DepartmentService {
    List<Department> getAllDepartment(Integer pageNumber);
    ResponseEntity<ResponseObject> getDepartmentById(Long id);
    ResponseEntity<ResponseObject> addDepartment(DepartmentRequest department);
    ResponseEntity<ResponseObject> deleteDepartment(Long id);
    ResponseEntity<ResponseObject> editDepartment(DepartmentRequest department, Long id);

}
