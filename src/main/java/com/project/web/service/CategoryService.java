package com.project.web.service;

import com.dropbox.core.DbxException;
import com.project.web.model.Category;
import com.project.web.payload.response.ResponseObject;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface CategoryService {
    List<Category> getAllCategory(int pageNumber);
    ResponseEntity<ResponseObject> getCateById(Long id);
    ResponseEntity<ResponseObject> addCategory(Category category);
    ResponseEntity<ResponseObject> deleteCategory(Long id);
    ResponseEntity<ResponseObject> editCategory(Category category, Long id);
    ResponseEntity<ResponseObject> downloadAllFileIdea(HttpServletResponse response, Long id) throws IOException, DbxException;
}
