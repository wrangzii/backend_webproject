package com.project.web.service;

import com.dropbox.core.DbxException;
import com.project.web.model.Category;
import com.project.web.payload.request.CategoryRequest;
import com.project.web.payload.response.ResponseObject;
import com.project.web.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @MockBean
    CategoryRepository cateRepo;
    @Autowired
    CategoryService categoryService;

    @Test
    void testGetAllCategoryService_thenReturnEmptyList() {
        Pageable paging = PageRequest.of(1, 10);
        Page<Category> expected = Page.empty();
        when(cateRepo.findAll(paging)).thenReturn(expected);
        List<Category> actual = categoryService.getAllCategory(1);
        assertEquals(new ArrayList<>(), actual);
        assertEquals(0, actual.size());
    }

    @Test
    void testGetAllCategoryService_thenReturnAnObj() {
        Pageable paging = PageRequest.of(1, 10);
        Category category = new Category();
        Page<Category> expected = new PageImpl<>(Collections.singletonList(category), paging,1);
        when(cateRepo.findAll(paging)).thenReturn(expected);
        List<Category> actual = categoryService.getAllCategory(1);
        assertEquals(expected.getContent(), actual);
        assertEquals(1, actual.size());
    }

    @Test
    void testGetCategoryByIdService_thenReturnAnObject() {
        Category category = new Category();
        category.setDescription("test");
        Optional<Category> categoryOpt = Optional.of(category);
        when(cateRepo.findById(1L)).thenReturn(categoryOpt);
        ResponseEntity<ResponseObject> actual = categoryService.getCateById(1L);
        assertEquals("Get category successfully!", Objects.requireNonNull(actual.getBody()).getMessage());
    }

    @Test
    void testGetCategoryByIdService_thenReturnError() {
        Optional<Category> category = Optional.empty();
        when(cateRepo.findById(1L)).thenReturn(category);
        ResponseEntity<ResponseObject> actual = categoryService.getCateById(1L);
        assertEquals("Error: Category name is already taken!", Objects.requireNonNull(actual.getBody()).getMessage());
    }

    @Test
    void addCategoryWithCategoryNameHasExisted_thenReturnError() {
        CategoryRequest category = new CategoryRequest();
        category.setCateName("test");
        when(cateRepo.existsByCateName("test")).thenReturn(true);
        ResponseEntity<ResponseObject> actual = categoryService.addCategory(category);
        assertEquals("Error: Category name is already taken!", Objects.requireNonNull(actual.getBody()).getMessage());
    }

    @Test
    void addCategoryWithCategoryNameHasExisted_thenReturnAnObject() {
        CategoryRequest category = new CategoryRequest();
        category.setCateName("test");
        when(cateRepo.existsByCateName("test")).thenReturn(false);
        ResponseEntity<ResponseObject> actual = categoryService.addCategory(category);
        assertEquals("Add category successfully!", Objects.requireNonNull(actual.getBody()).getMessage());
    }

    @Test
    void deleteCategoryWithCategoryHasExisted_thenReturnAMessageSuccess() {
        Category deleteCategory = new Category();
        deleteCategory.setCateName("test");
        Optional<Category> cateOpt = Optional.of(deleteCategory);
        when(cateRepo.findById(1L)).thenReturn(cateOpt);
        ResponseEntity<ResponseObject> actual = categoryService.deleteCategory(1L);
        assertEquals("Delete category successfully!", Objects.requireNonNull(actual.getBody()).getMessage());
    }

    @Test
    void deleteCategoryWithCategoryHasNotExisted_thenReturnAErrorMessage() {
        Optional<Category> cateOpt = Optional.empty();
        when(cateRepo.findById(1L)).thenReturn(cateOpt);
        ResponseEntity<ResponseObject> actual = categoryService.deleteCategory(1L);
        assertEquals("Error: Category name is not exist!", Objects.requireNonNull(actual.getBody()).getMessage());
    }

    @Test
    void downloadAllFileIdeaWithCategoryHasNotExisted_thenReturnAErrorMessage() throws IOException, DbxException {
        Optional<Category> cateOpt = Optional.empty();
        when(cateRepo.findById(1L)).thenReturn(cateOpt);
        ResponseEntity<ResponseObject> actual = categoryService.downloadAllFileIdea(1L);
        assertEquals("Download fail!", Objects.requireNonNull(actual.getBody()).getMessage());
    }

//    @Test
//    void downloadAllFileIdeaWithCategoryHasExisted_thenReturnAMessageSuccess() throws IOException, DbxException {
//        Category deleteCategory = new Category();
//        deleteCategory.setCateName("test");
//        Optional<Category> cateOpt = Optional.of(deleteCategory);
//        when(cateRepo.findById(1L)).thenReturn(cateOpt);
//        ResponseEntity<ResponseObject> actual = categoryService.downloadAllFileIdea(1L);
//        assertEquals("Download successfully!", Objects.requireNonNull(actual.getBody()).getMessage());
//    }
    @Test
    void editCategoryWithCategoryHasExisted_thenReturnAMessageSuccess() throws IOException, DbxException {
        Category editCate = new Category();
        CategoryRequest categoryInput = new CategoryRequest();
        categoryInput.setDescription("test");
        editCate.setCateName("test");
        Optional<Category> cateOpt = Optional.of(editCate);
        when(cateRepo.findById(1L)).thenReturn(cateOpt);
        ResponseEntity<ResponseObject> actual = categoryService.editCategory(categoryInput, 1L);
        assertEquals("Edit category successfully!", Objects.requireNonNull(actual.getBody()).getMessage());
    }
    @Test
    void editCategoryWithCategoryHasNotExisted_thenReturnAMessageError() throws IOException, DbxException {
        CategoryRequest categoryInput = new CategoryRequest();
        Optional<Category> cateOpt = Optional.empty();
        when(cateRepo.findById(1L)).thenReturn(cateOpt);
        ResponseEntity<ResponseObject> actual = categoryService.editCategory(categoryInput, 1L);
        assertEquals("Error: Category is not exist!", Objects.requireNonNull(actual.getBody()).getMessage());
    }
}
