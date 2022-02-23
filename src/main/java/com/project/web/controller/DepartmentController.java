package com.project.web.controller;

import com.project.web.model.Department;
import com.project.web.payload.response.MessageResponse;
import com.project.web.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/department")
@Slf4j
public class DepartmentController {
    private final DepartmentService departmentSer;

    @GetMapping("/all")
    public List<Department> getAllDepart() {
        return departmentSer.getAllDepartment();
    }

    @PostMapping("/add")
    public ResponseEntity<MessageResponse> addDepartment(@Valid @RequestBody Department department) {
        return departmentSer.addDepartment(department);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<MessageResponse> editDepartment(@Valid @RequestBody Department department, @PathVariable Long id) {
        return departmentSer.editDepartment(department,id);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<MessageResponse> deleteDepartment(@PathVariable Long id) {
        return departmentSer.deleteDepartment(id);
    }
}
