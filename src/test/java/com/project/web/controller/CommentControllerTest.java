package com.project.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.web.payload.request.CommentRequest;
import com.project.web.payload.response.CommentResponse;
import com.project.web.service.CommentService;
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
public class CommentControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CommentService commentService;
    @Autowired
    private ObjectMapper mapper;

    @Test
    void whenValidInputWithParams_thenReturns200() throws Exception {
        CommentResponse commentResponse = new CommentResponse();
        when(commentService.getAllCommentByIdea(1L)).thenReturn(Collections.singletonList(commentResponse));
        MockHttpServletResponse response = mockMvc.perform(get("/comment/all/{ideaId}", 1L)
                        .contentType("application/json"))
                .andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatus());
    }
    @Test
    void whenValidInputWithoutParams_thenReturns404() throws Exception {
        CommentResponse commentResponse = new CommentResponse();
        when(commentService.getAllCommentByIdea(null)).thenReturn(Collections.singletonList(commentResponse) );
        MockHttpServletResponse response =  mockMvc.perform(get("/comment/all/{ideaId}", (Object) null)
                        .contentType("application/json"))
                .andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void addCommentWithInputParams_thenReturn200() throws Exception {
        CommentRequest commentRequest = new CommentRequest();
        String jsonInput = mapper.writeValueAsString(commentRequest);
        when(commentService.addCommentOfUser(commentRequest, null)).thenReturn(null);
        MockHttpServletResponse response = mockMvc.perform(post("/comment/add")
                        .param("parentCommentId", "")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInput))
                .andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void addCommentWithoutInputParams_thenReturn200() throws Exception {
        CommentRequest commentRequest = new CommentRequest();
        when(commentService.addCommentOfUser(commentRequest, null)).thenReturn(null);
        MockHttpServletResponse response = mockMvc.perform(post("/comment/add")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void editCommentWithoutInputParams_thenReturn400() throws Exception {
        CommentRequest commentRequest = new CommentRequest();
        when(commentService.editComment(commentRequest, 1L, null)).thenReturn(null);
        MockHttpServletResponse response = mockMvc.perform(put("/comment/edit/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void editCommentWithInputParams_thenReturn200() throws Exception {
        CommentRequest commentRequest = new CommentRequest();
        String jsonInput = mapper.writeValueAsString(commentRequest);
        when(commentService.editComment(commentRequest, 1L, null)).thenReturn(null);
        MockHttpServletResponse response = mockMvc.perform(put("/comment/edit/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInput))
                .andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteCommentSuccess_thenReturn200() throws Exception {
        when(commentService.deleteComment(null)).thenReturn(null);
        MockHttpServletResponse response = mockMvc.perform(delete("/comment/delete/{id}", 100)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

}
