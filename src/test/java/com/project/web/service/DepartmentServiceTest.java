package com.project.web.service;

import com.dropbox.core.DbxException;
import com.project.web.model.Department;
import com.project.web.payload.request.DepartmentRequest;
import com.project.web.payload.response.ResponseObject;
import com.project.web.repository.DepartmentRepository;
import org.junit.jupiter.api.Test;
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
public class DepartmentServiceTest {
    @Autowired
    private DepartmentService departmentService;
    @MockBean
    private DepartmentRepository departmentRepo;

    @Test
    void testGetAllDepartmentsService_thenReturnEmptyList() {
        Pageable paging = PageRequest.of(1, 10);
        Page<Department> expected = Page.empty();
        when(departmentRepo.findAll(paging)).thenReturn(expected);
        List<Department> actual = departmentService.getAllDepartment(1);
        assertEquals(new ArrayList<>(), actual);
        assertEquals(0, actual.size());
    }

    @Test
    void testGetAllDepartmentService_thenReturnAnObj() {
        Pageable paging = PageRequest.of(1, 10);
        Department department = new Department();
        Page<Department> expected = new PageImpl<>(Collections.singletonList(department), paging,1);
        when(departmentRepo.findAll(paging)).thenReturn(expected);
        List<Department> actual = departmentService.getAllDepartment(1);
        assertEquals(expected.getContent(), actual);
        assertEquals(1, actual.size());
    }

    @Test
    void testGetDepartmentByIdService_thenReturnAnObject() {
        Department department = new Department();
        department.setDepartmentName("test");
        Optional<Department> departmentOpt = Optional.of(department);
        when(departmentRepo.findById(1L)).thenReturn(departmentOpt);
        ResponseEntity<ResponseObject> actual = departmentService.getDepartmentById(1L);
        assertEquals("Get department successfully!", Objects.requireNonNull(actual.getBody()).getMessage());
    }

    @Test
    void testGetDepartmentByIdService_thenReturnError() {
        Optional<Department> department = Optional.empty();
        when(departmentRepo.findById(1L)).thenReturn(department);
        ResponseEntity<ResponseObject> actual = departmentService.getDepartmentById(1L);
        assertEquals("Error: Department name is already taken!", Objects.requireNonNull(actual.getBody()).getMessage());
    }

    @Test
    void addDepartmentWithDepartmentNameHasExisted_thenReturnError() {
        DepartmentRequest department = new DepartmentRequest();
        department.setDepartmentName("test");
        when(departmentRepo.existsByDepartmentName("test")).thenReturn(true);
        ResponseEntity<ResponseObject> actual = departmentService.addDepartment(department);
        assertEquals("Error: Department name is already taken!", Objects.requireNonNull(actual.getBody()).getMessage());
    }

    @Test
    void addDepartmentWithDepartmentNameHasExisted_thenReturnAnObject() {
        DepartmentRequest department = new DepartmentRequest();
        department.setDepartmentName("test");
        when(departmentRepo.existsByDepartmentName("test")).thenReturn(false);
        ResponseEntity<ResponseObject> actual = departmentService.addDepartment(department);
        assertEquals("Add department successfully!", Objects.requireNonNull(actual.getBody()).getMessage());
    }

    @Test
    void deleteDepartmentWithDepartmentHasExisted_thenReturnAMessageSuccess() {
        Department department = new Department();
        department.setDepartmentName("test");
        Optional<Department> departmentOpt = Optional.of(department);
        when(departmentRepo.findById(1L)).thenReturn(departmentOpt);
        ResponseEntity<ResponseObject> actual = departmentService.deleteDepartment(1L);
        assertEquals("Delete department successfully!", Objects.requireNonNull(actual.getBody()).getMessage());
    }

    @Test
    void deleteDepartmentWithDepartmentHasNotExisted_thenReturnAErrorMessage() {
        Optional<Department> department = Optional.empty();
        when(departmentRepo.findById(1L)).thenReturn(department);
        ResponseEntity<ResponseObject> actual = departmentService.deleteDepartment(1L);
        assertEquals("Error: Department name is not exist!", Objects.requireNonNull(actual.getBody()).getMessage());
    }

    @Test
    void editDepartmentWithDepartmentHasExisted_thenReturnAMessageSuccess() throws IOException, DbxException {
        DepartmentRequest department = new DepartmentRequest();
        Department department1 = new Department();
        department.setDepartmentName("test");
        Optional<Department> departmentOpt = Optional.of(department1);
        when(departmentRepo.findById(1L)).thenReturn(departmentOpt);
        ResponseEntity<ResponseObject> actual = departmentService.editDepartment(department, 1L);
        assertEquals("Edit Department successfully!", Objects.requireNonNull(actual.getBody()).getMessage());
    }
    @Test
    void editDepartmentWithDepartmentHasNotExisted_thenReturnAMessageError() {
        DepartmentRequest department = new DepartmentRequest();
        Optional<Department> departmentOptional = Optional.empty();
        when(departmentRepo.findById(1L)).thenReturn(departmentOptional);
        ResponseEntity<ResponseObject> actual = departmentService.editDepartment(department, 1L);
        assertEquals("Error: Department is not exist!", Objects.requireNonNull(actual.getBody()).getMessage());
    }
}
