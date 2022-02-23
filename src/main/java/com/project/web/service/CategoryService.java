package com.project.web.service;

import com.project.web.model.Category;
import com.project.web.payload.response.ResponseObject;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategory();
    ResponseEntity<ResponseObject> addCategory(Category category);
    ResponseEntity<ResponseObject> deleteCategory(Long id);
    ResponseEntity<ResponseObject> editCategory(Category category, Long id);
}
