package com.project.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.web.payload.request.DeleteReactionRequest;
import com.project.web.payload.request.ReactionRequest;
import com.project.web.service.ReactionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
public class ReactionControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ReactionService reactionService;
    @Autowired
    private ObjectMapper mapper;

    @Test
    void whenValidInputWithParams_thenReturns200() throws Exception {
        when(reactionService.getReactionOfAnIdea(1L)).thenReturn(null);
        MockHttpServletResponse response = mockMvc.perform(get("/reaction/{id}", 1L)
                        .contentType("application/json"))
                .andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatus());
    }

    @Test
    void whenValidInputWithoutParams_thenReturns404() throws Exception {
        when(reactionService.getReactionOfAnIdea(null)).thenReturn(null);
        MockHttpServletResponse response = mockMvc.perform(get("/reaction/{id}", (Object) null)
                        .contentType("application/json"))
                .andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(),response.getStatus());
    }

    @Test
    void addReactionOfAnIdeaWithInputParams_thenReturn200() throws Exception {
        ReactionRequest reactionRequest = new ReactionRequest();
        reactionRequest.setReactionType("like");
        String jsonInput = mapper.writeValueAsString(reactionRequest);
        when(reactionService.addReactionOfAnIdea(reactionRequest)).thenReturn(null);
        MockHttpServletResponse response = mockMvc.perform(post("/reaction/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInput))
                .andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    void addReactionWithoutInputParams_thenReturn400() throws Exception {
        ReactionRequest reactionRequest = new ReactionRequest();
        when(reactionService.addReactionOfAnIdea(reactionRequest)).thenReturn(null);
        MockHttpServletResponse response = mockMvc.perform(post("/reaction/add")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    void editReactionWithoutInputParams_thenReturn400() throws Exception {
        ReactionRequest reactionRequest = new ReactionRequest();
        when(reactionService.editReactionOfAnIdea(reactionRequest)).thenReturn(null);
        MockHttpServletResponse response = mockMvc.perform(put("/reaction/edit")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    void editReactionWithInputParams_thenReturn200() throws Exception {
        ReactionRequest reactionRequest = new ReactionRequest();
        reactionRequest.setReactionType("like");
        String jsonInput = mapper.writeValueAsString(reactionRequest);
        when(reactionService.editReactionOfAnIdea(reactionRequest)).thenReturn(null);
        MockHttpServletResponse response = mockMvc.perform(put("/reaction/edit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInput))
                .andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    void deleteReactionSuccess_thenReturn200() throws Exception {
        DeleteReactionRequest reactionRequest = new DeleteReactionRequest();
        reactionRequest.setIdeaId(1L);
        String jsonInput = mapper.writeValueAsString(reactionRequest);
        when(reactionService.deleteReactionOfAnIdea(reactionRequest)).thenReturn(null);
        MockHttpServletResponse response = mockMvc.perform(delete("/reaction/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInput))
                .andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

}
