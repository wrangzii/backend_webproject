package com.project.web.controller;

import com.project.web.model.Category;
import com.project.web.payload.response.ResponseObject;
import com.project.web.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/category")
@Slf4j
@CrossOrigin("http://localhost:3000")
public class CategoryController {
    private final CategoryService cateSer;

    @GetMapping("/all")
    public List<Category> getAllCategory() {
        return cateSer.getAllCategory();
    }

    @PostMapping("/add")
    public ResponseEntity<ResponseObject> addCategory(@Valid @RequestBody Category category) {
        return cateSer.addCategory(category);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<ResponseObject> editCategory(@Valid @RequestBody Category category, @PathVariable Long id) {
        return cateSer.editCategory(category,id);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseObject> deleteCategory(@PathVariable Long id) {
        return cateSer.deleteCategory(id);
    }
}