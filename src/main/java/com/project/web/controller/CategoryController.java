package com.project.web.controller;

import com.dropbox.core.DbxException;
import com.project.web.model.Category;
import com.project.web.payload.response.ResponseObject;
import com.project.web.service.CategoryService;
import com.project.web.service.serviceImp.DropboxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/category")
@Slf4j
@CrossOrigin("http://localhost:3000")
public class CategoryController {
    @Autowired
    DropboxService dropboxService;
    private final CategoryService cateSer;

    @GetMapping("/all")
    public List<Category> getAllCategory(@RequestParam int pageNumber) {
        return cateSer.getAllCategory(pageNumber);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getCateById(@PathVariable Long id) {
        return cateSer.getCateById(id);
    }

    @PostMapping("/add")
    public ResponseEntity<ResponseObject> addCategory(@Valid @RequestBody Category category) throws DbxException {
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

    @GetMapping("/download")
    public void downloadFile(HttpServletResponse response, @RequestParam String categoryName) throws Exception {
        dropboxService.downloadFile(response, categoryName);
    }
}
