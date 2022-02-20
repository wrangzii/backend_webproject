package com.project.web.controller;

import com.project.web.model.Department;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/department")
public class DepartmentController {
    @GetMapping("/")
    public List<Department> getAllDepart() {
        return null;
    }
}
