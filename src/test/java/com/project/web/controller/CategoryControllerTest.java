package com.project.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.web.model.Category;
import com.project.web.payload.request.CategoryRequest;
import com.project.web.service.CategoryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
public class CategoryControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CategoryService categoryService;
    @Autowired
    private ObjectMapper mapper;

    @Test
    void whenValidInputWithParams_thenReturns200() throws Exception {
        Integer pageNumber = 1;
        Category categoryList = new Category();
        when(categoryService.getAllCategory(pageNumber)).thenReturn(Collections.singletonList(categoryList));
        MockHttpServletResponse response = mockMvc.perform(get("/category/all")
//                        .with(SecurityMockMvcRequestPostProcessors.jwt())
                        .param("pageNumber", "1")
                        .contentType("application/json"))
                .andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatus());
    }
    @Test
    void whenValidInputWithoutParams_thenReturns400() throws Exception {
        Category categoryList = new Category();
        when(categoryService.getAllCategory(null)).thenReturn(Collections.singletonList(categoryList) );
        MockHttpServletResponse response =  mockMvc.perform(get("/category/all")
                        .param("pageNumber", "")
                        .contentType("application/json"))
                .andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    void getCateByIdWhenValidInputWithParams_thenReturns200() throws Exception {
        when(categoryService.getCateById(2L)).thenReturn(null);
        MockHttpServletResponse response = mockMvc.perform(get("/category/{id}", 1)
                .contentType("application/json"))
                .andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    void getCateByIdWhenValidInputWithoutParamsNotExist_thenReturns404() throws Exception {
        when(categoryService.getCateById(null)).thenReturn(null);
        MockHttpServletResponse response = mockMvc.perform(get("/category/{id}", (Object) null)
                        .contentType("application/json"))
                .andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void addCategoryWithoutInputParams_thenReturn400() throws Exception {
        CategoryRequest category = new CategoryRequest();
        when(categoryService.addCategory(category)).thenReturn(null);
        String jsonInput = mapper.writeValueAsString(category);
        MockHttpServletResponse response = mockMvc.perform(post("/category/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInput))
                        .andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void addCategoryWithInputParams_thenReturn200() throws Exception {
        CategoryRequest category = new CategoryRequest();
        category.setCateName("test");
        category.setDescription("test");
        String jsonInput = mapper.writeValueAsString(category);
        when(categoryService.addCategory(category)).thenReturn(null);
        MockHttpServletResponse response = mockMvc.perform(post("/category/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInput))
                .andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void editCategoryWithoutInputParams_thenReturn400() throws Exception {
        CategoryRequest category = new CategoryRequest();
        String jsonInput = mapper.writeValueAsString(category);
        when(categoryService.editCategory(null, 1L)).thenReturn(null);
        MockHttpServletResponse response = mockMvc.perform(put("/category/edit/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInput))
                .andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void editCategoryWithInputParams_thenReturn200() throws Exception {
        CategoryRequest category = new CategoryRequest();
        category.setCateName("test");
        category.setDescription("test");
        String jsonInput = mapper.writeValueAsString(category);
        when(categoryService.editCategory(category, 1L)).thenReturn(null);
        MockHttpServletResponse response = mockMvc.perform(put("/category/edit/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInput))
                .andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteCategorySuccess_thenReturn200() throws Exception {
        when(categoryService.deleteCategory(null)).thenReturn(null);
        MockHttpServletResponse response = mockMvc.perform(delete("/category/delete/{id}", 100)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    @WithMockUser(roles = "QA_MANAGER")
    void downloadWholeFileByCate_thenReturn200() throws Exception {
        when(categoryService.downloadAllFileIdea(1L)).thenReturn(null);
        MockHttpServletResponse response = mockMvc.perform(get("/category/download/{cateId}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

}