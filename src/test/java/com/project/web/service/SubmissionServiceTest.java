package com.project.web.service;

import com.project.web.model.Submission;
import com.project.web.payload.request.SubmissionRequest;
import com.project.web.payload.response.ResponseObject;
import com.project.web.repository.SubmissionRepository;
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

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
public class SubmissionServiceTest {
    @Autowired
    SubmissionService submissionService;
    @MockBean
    SubmissionRepository submissionRepository;

    @Test
    void testGetAllSubmissionsService_thenReturnEmptyList() {
        Pageable paging = PageRequest.of(1, 10);
        Page<Submission> expected = Page.empty();
        when(submissionRepository.findAll(paging)).thenReturn(expected);
        List<Submission> actual = submissionService.getAllSubmission(1);
        assertEquals(new ArrayList<>(), actual);
        assertEquals(0, actual.size());
    }

    @Test
    void testGetAllSubmissionService_thenReturnAnObj() {
        Pageable paging = PageRequest.of(1, 10);
        Submission submission = new Submission();
        Page<Submission> expected = new PageImpl<>(Collections.singletonList(submission), paging,1);
        when(submissionRepository.findAll(paging)).thenReturn(expected);
        List<Submission> actual = submissionService.getAllSubmission(1);
        assertEquals(expected.getContent(), actual);
        assertEquals(1, actual.size());
    }

    @Test
    void testGetSubmissionByIdService_thenReturnAnObject() {
        Submission submission = new Submission();
        submission.setSubmissionName("test");
        Optional<Submission> submissionOptional = Optional.of(submission);
        when(submissionRepository.findById(1L)).thenReturn(submissionOptional);
        ResponseEntity<ResponseObject> actual = submissionService.getSubmissionById(1L);
        assertEquals("Get submission successfully!", Objects.requireNonNull(actual.getBody()).getMessage());
    }

    @Test
    void testGetSubmissionByIdService_thenReturnError() {
        Optional<Submission> submission = Optional.empty();
        when(submissionRepository.findById(1L)).thenReturn(submission);
        ResponseEntity<ResponseObject> actual = submissionService.getSubmissionById(1L);
        assertEquals("Error: Submission name is already taken!", Objects.requireNonNull(actual.getBody()).getMessage());
    }

    @Test
    void addSubmissionWithSubmissionNameHasExisted_thenReturnError() {
        SubmissionRequest submission = new SubmissionRequest();
        submission.setSubmissionName("test");
        when(submissionRepository.existsBySubmissionName("test")).thenReturn(true);
        ResponseEntity<ResponseObject> actual = submissionService.addSubmission(submission);
        assertEquals("Error: Submission name is already taken!", Objects.requireNonNull(actual.getBody()).getMessage());
    }

    @Test
    void addSubmissionWithSubmissionNameHasExisted_thenReturnAnObject() {
        Submission submission = new Submission();
        SubmissionRequest submissionRequest = new SubmissionRequest();
        submission.setSubmissionName("test");
        when(submissionRepository.existsBySubmissionName("test")).thenReturn(false);
        ResponseEntity<ResponseObject> actual = submissionService.addSubmission(submissionRequest);
        assertEquals("Add submission successfully!", Objects.requireNonNull(actual.getBody()).getMessage());
    }

    @Test
    void deleteSubmissionWithSubmissionHasExisted_thenReturnAMessageSuccess() {
        Submission submission = new Submission();
        submission.setDescription("test");
        Optional<Submission> submissionOptional = Optional.of(submission);
        when(submissionRepository.findById(1L)).thenReturn(submissionOptional);
        ResponseEntity<ResponseObject> actual = submissionService.deleteSubmission(1L);
        assertEquals("Delete submission successfully!", Objects.requireNonNull(actual.getBody()).getMessage());
    }

    @Test
    void deleteSubmissionWithSubmissionHasNotExisted_thenReturnAErrorMessage() {
        Optional<Submission> submission = Optional.empty();
        when(submissionRepository.findById(1L)).thenReturn(submission);
        ResponseEntity<ResponseObject> actual = submissionService.deleteSubmission(1L);
        assertEquals("Error: Submission name is not exist!", Objects.requireNonNull(actual.getBody()).getMessage());
    }

    @Test
    void editSubmissionWithSubmissionHasExisted_thenReturnAMessageSuccess() {
        Submission submission = new Submission();
        SubmissionRequest submissionRequest = new SubmissionRequest();
        submission.setSubmissionName("test");
        Optional<Submission> submissionOptional = Optional.of(submission);
        when(submissionRepository.findById(1L)).thenReturn(submissionOptional);
        ResponseEntity<ResponseObject> actual = submissionService.editSubmission(submissionRequest, 1L);
        assertEquals("Edit submission successfully!", Objects.requireNonNull(actual.getBody()).getMessage());
    }

    @Test
    void editSubmissionWithSubmissionHasNotExisted_thenReturnAMessageError() {
        Submission submission = new Submission();
        SubmissionRequest submissionRequest = new SubmissionRequest();
        Optional<Submission> submissionOptional = Optional.empty();
        when(submissionRepository.findById(1L)).thenReturn(submissionOptional);
        ResponseEntity<ResponseObject> actual = submissionService.editSubmission(submissionRequest, 1L);
        assertEquals("Error: Submission is not exist!", Objects.requireNonNull(actual.getBody()).getMessage());
    }
}
