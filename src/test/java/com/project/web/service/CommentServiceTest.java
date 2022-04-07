package com.project.web.service;

import com.project.web.model.Comment;
import com.project.web.model.Idea;
import com.project.web.model.Submission;
import com.project.web.model.User;
import com.project.web.payload.response.CommentResponse;
import com.project.web.repository.CommentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
public class CommentServiceTest {
    @Autowired
    CommentService commentSer;
    @MockBean
    CommentRepository commentRepo;

    @Test
    void testGetAllCommentByIdeaService_thenReturnEmptyList() {
        List<Comment> expected = new ArrayList<>();
        when(commentRepo.findCommentByIdeaId(null)).thenReturn(expected);
        List<CommentResponse> actual = commentSer.getAllCommentByIdea(1L);
        assertEquals(new ArrayList<>(), actual);
        assertEquals(0, actual.size());
    }

    @Test
    void testGetAllDepartmentService_thenReturnAnObj() {
        Comment comment = new Comment();
        Comment parentComment = new Comment();
        Idea idea = new Idea();
        User user = new User();
        Date date = new Date();
        Submission submission = new Submission();
        user.setUsername("test");
        comment.setUserId(user);
        idea.setIdeaId(1L);
        submission.setSubmissionId(1L);
        comment.setContent("test");
        comment.setParentCommentId(parentComment);
        comment.setCreateDate(date);
        comment.setLastModifyDate(date);
        comment.setIsAnonymous(true);
        comment.setIdeaId(idea);
        comment.setUserId(user);
        comment.setCommentId(1L);
        comment.setContent("test");
        List<Comment> comments = new ArrayList<>();
        comments.add(comment);
        comments.add(comment);
        when(commentRepo.findCommentByIdeaId(idea)).thenReturn(comments);
        submission.setFinalClosureDate(date);
        idea.setSubmissionId(submission);
        List<CommentResponse> actual = commentSer.getAllCommentByIdea(1L);
        assertEquals(0, actual.size());
    }

//    @Test
//    void testGetDepartmentByIdService_thenReturnAnObject() {
//        Department department = new Department();
//        department.setDepartmentName("test");
//        Optional<Department> departmentOpt = Optional.of(department);
//        when(departmentRepo.findById(1L)).thenReturn(departmentOpt);
//        ResponseEntity<ResponseObject> actual = departmentService.getDepartmentById(1L);
//        assertEquals("Get department successfully!", Objects.requireNonNull(actual.getBody()).getMessage());
//    }
//
//    @Test
//    void testGetDepartmentByIdService_thenReturnError() {
//        Optional<Department> department = Optional.empty();
//        when(departmentRepo.findById(1L)).thenReturn(department);
//        ResponseEntity<ResponseObject> actual = departmentService.getDepartmentById(1L);
//        assertEquals("Error: Department name is already taken!", Objects.requireNonNull(actual.getBody()).getMessage());
//    }
//
//    @Test
//    void addDepartmentWithDepartmentNameHasExisted_thenReturnError() {
//        Department department = new Department();
//        department.setDepartmentName("test");
//        when(departmentRepo.existsByDepartmentName("test")).thenReturn(true);
//        ResponseEntity<ResponseObject> actual = departmentService.addDepartment(department);
//        assertEquals("Error: Department name is already taken!", Objects.requireNonNull(actual.getBody()).getMessage());
//    }
//
//    @Test
//    void addDepartmentWithDepartmentNameHasExisted_thenReturnAnObject() {
//        Department department = new Department();
//        department.setDepartmentName("test");
//        when(departmentRepo.existsByDepartmentName("test")).thenReturn(false);
//        ResponseEntity<ResponseObject> actual = departmentService.addDepartment(department);
//        assertEquals("Add department successfully!", Objects.requireNonNull(actual.getBody()).getMessage());
//    }
//
//    @Test
//    void deleteDepartmentWithDepartmentHasExisted_thenReturnAMessageSuccess() {
//        Department department = new Department();
//        department.setDepartmentName("test");
//        Optional<Department> departmentOpt = Optional.of(department);
//        when(departmentRepo.findById(1L)).thenReturn(departmentOpt);
//        ResponseEntity<ResponseObject> actual = departmentService.deleteDepartment(1L);
//        assertEquals("Delete department successfully!", Objects.requireNonNull(actual.getBody()).getMessage());
//    }
//
//    @Test
//    void deleteDepartmentWithDepartmentHasNotExisted_thenReturnAErrorMessage() {
//        Optional<Department> department = Optional.empty();
//        when(departmentRepo.findById(1L)).thenReturn(department);
//        ResponseEntity<ResponseObject> actual = departmentService.deleteDepartment(1L);
//        assertEquals("Error: Department name is not exist!", Objects.requireNonNull(actual.getBody()).getMessage());
//    }
//
//    @Test
//    void editDepartmentWithDepartmentHasExisted_thenReturnAMessageSuccess() throws IOException, DbxException {
//        Department department = new Department();
//        department.setDepartmentName("test");
//        Optional<Department> departmentOpt = Optional.of(department);
//        when(departmentRepo.findById(1L)).thenReturn(departmentOpt);
//        ResponseEntity<ResponseObject> actual = departmentService.editDepartment(department, 1L);
//        assertEquals("Edit Department successfully!", Objects.requireNonNull(actual.getBody()).getMessage());
//    }
//    @Test
//    void editDepartmentWithDepartmentHasNotExisted_thenReturnAMessageError() {
//        Department department = new Department();
//        Optional<Department> departmentOptional = Optional.empty();
//        when(departmentRepo.findById(1L)).thenReturn(departmentOptional);
//        ResponseEntity<ResponseObject> actual = departmentService.editDepartment(department, 1L);
//        assertEquals("Error: Department is not exist!", Objects.requireNonNull(actual.getBody()).getMessage());
//    }
}
