package com.project.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.web.model.Department;
import com.project.web.service.DepartmentService;
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
public class DepartmentControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private DepartmentService departmentSer;
    @Autowired
    private ObjectMapper mapper;

    @Test
    void whenValidInputWithParams_thenReturns200() throws Exception {
        Department department = new Department();
        when(departmentSer.getAllDepartment(1)).thenReturn(Collections.singletonList(department));
        MockHttpServletResponse response = mockMvc.perform(get("/department/all")
                        .param("pageNumber", "1")
                        .contentType("application/json"))
                .andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatus());
    }
    @Test
    void whenValidInputWithoutParams_thenReturns400() throws Exception {
        Department department = new Department();
        when(departmentSer.getAllDepartment(null)).thenReturn(Collections.singletonList(department) );
        MockHttpServletResponse response =  mockMvc.perform(get("/department/all")
                        .contentType("application/json"))
                .andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void addCommentWithInputParams_thenReturn200() throws Exception {
        Department department = new Department();
        department.setDepartmentName("Test");
        String jsonInput = mapper.writeValueAsString(department);
        when(departmentSer.addDepartment(department)).thenReturn(null);
        MockHttpServletResponse response = mockMvc.perform(post("/department/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInput))
                .andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void addCommentWithoutInputParams_thenReturn200() throws Exception {
        Department department = new Department();
        when(departmentSer.addDepartment(department)).thenReturn(null);
        MockHttpServletResponse response = mockMvc.perform(post("/department/add")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void editCommentWithoutInputParams_thenReturn400() throws Exception {
        Department department = new Department();
        when(departmentSer.editDepartment(department, null)).thenReturn(null);
        MockHttpServletResponse response = mockMvc.perform(put("/department/edit/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void editCommentWithInputParams_thenReturn200() throws Exception {
        Department department = new Department();
        department.setDepartmentName("test");
        String jsonInput = mapper.writeValueAsString(department);
        when(departmentSer.editDepartment(department, 1L)).thenReturn(null);
        MockHttpServletResponse response = mockMvc.perform(put("/department/edit/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInput))
                .andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    void getDepartmentByIdWhenValidInputWithParams_thenReturns200() throws Exception {
        when(departmentSer.getDepartmentById(2L)).thenReturn(null);
        MockHttpServletResponse response = mockMvc.perform(get("/department/{id}", 2)
                        .contentType("application/json"))
                .andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    void getDepartmentByIdWhenValidInputWithoutParamsNotExist_thenReturns404() throws Exception {
        when(departmentSer.getDepartmentById(null)).thenReturn(null);
        MockHttpServletResponse response = mockMvc.perform(get("/department/{id}", (Object) null)
                        .contentType("application/json"))
                .andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteCommentSuccess_thenReturn200() throws Exception {
        when(departmentSer.deleteDepartment(1L)).thenReturn(null);
        MockHttpServletResponse response = mockMvc.perform(delete("/department/delete/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
    }
}
