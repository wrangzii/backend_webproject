package com.project.web.controller;

import com.project.web.model.Department;
import com.project.web.payload.response.ResponseObject;
import com.project.web.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/department")
@Slf4j
@CrossOrigin("http://localhost:3000")
public class DepartmentController {
    private final DepartmentService departmentSer;

    @GetMapping("/all")
    public List<Department> getAllDepart(@RequestParam int pageNumber) {
        return departmentSer.getAllDepartment(pageNumber);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getDepartmentById(@PathVariable Long id) {
        return departmentSer.getDepartmentById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<ResponseObject> addDepartment(@Valid @RequestBody Department department) {
        return departmentSer.addDepartment(department);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/edit/{id}")
    public ResponseEntity<ResponseObject> editDepartment(@Valid @RequestBody Department department, @PathVariable Long id) {
        return departmentSer.editDepartment(department,id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseObject> deleteDepartment(@PathVariable Long id) {
        return departmentSer.deleteDepartment(id);
    }
}
