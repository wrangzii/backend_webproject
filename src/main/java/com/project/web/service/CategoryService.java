package com.project.web.service;

import com.dropbox.core.DbxException;
import com.project.web.model.Category;
import com.project.web.payload.request.CategoryRequest;
import com.project.web.payload.response.ResponseObject;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

public interface CategoryService {
    List<Category> getAllCategory(Integer pageNumber);
    ResponseEntity<ResponseObject> getCateById(Long id);
    ResponseEntity<ResponseObject> addCategory(CategoryRequest category);
    ResponseEntity<ResponseObject> deleteCategory(Long id);
    ResponseEntity<ResponseObject> editCategory(CategoryRequest category, Long id);
    ResponseEntity<ResponseObject> downloadAllFileIdea(Long id) throws IOException, DbxException;
}
