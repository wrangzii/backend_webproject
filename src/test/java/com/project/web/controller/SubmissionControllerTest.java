package com.project.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.web.model.Submission;
import com.project.web.service.SubmissionService;
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

import java.util.Date;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
public class SubmissionControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private SubmissionService submissionService;
    @Autowired
    private ObjectMapper mapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    void whenValidInputWithParams_thenReturns200() throws Exception {
        when(submissionService.getAllSubmission(1)).thenReturn(null);
        MockHttpServletResponse response = mockMvc.perform(get("/submission/all")
                        .param("pageNumber", "1")
                        .contentType("application/json"))
                .andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatus());
    }

    @Test
    void getSubmissionByIdWhenValidInputWithParams_thenReturns200() throws Exception {
        when(submissionService.getSubmissionById(2L)).thenReturn(null);
        MockHttpServletResponse response = mockMvc.perform(get("/submission/{id}", 1)
                        .contentType("application/json"))
                .andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void addSubmissionWithInputParams_thenReturn200() throws Exception {
        Submission submission = new Submission();
        Date date = new Date();
        submission.setSubmissionName("test");
        submission.setDescription("test");
        submission.setClosureDate(date);
        submission.setFinalClosureDate(date);
        String jsonInput = mapper.writeValueAsString(submission);
        when(submissionService.addSubmission(submission)).thenReturn(null);
        MockHttpServletResponse response = mockMvc.perform(post("/submission/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInput))
                .andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void addSubmissionWithoutInputParams_thenReturn() throws Exception {
        Submission submission = new Submission();
        when(submissionService.addSubmission(submission)).thenReturn(null);
        MockHttpServletResponse response = mockMvc.perform(post("/submission/add")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void editSubmissionWithoutInputParams_thenReturn400() throws Exception {
        Submission submission = new Submission();
        when(submissionService.editSubmission(submission,1L)).thenReturn(null);
        MockHttpServletResponse response = mockMvc.perform(put("/submission/edit/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void editSubmissionWithInputParams_thenReturn200() throws Exception {
        Submission submission = new Submission();
        Date date = new Date();
        submission.setSubmissionName("test");
        submission.setDescription("test");
        submission.setClosureDate(date);
        submission.setFinalClosureDate(date);
        String jsonInput = mapper.writeValueAsString(submission);
        when(submissionService.editSubmission(submission, 1L)).thenReturn(null);
        MockHttpServletResponse response = mockMvc.perform(put("/submission/edit/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInput))
                .andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteSubmissionSuccess_thenReturn200() throws Exception {
        when(submissionService.deleteSubmission(1L)).thenReturn(null);
        MockHttpServletResponse response = mockMvc.perform(delete("/submission/delete/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

}
