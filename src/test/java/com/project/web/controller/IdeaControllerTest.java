package com.project.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.web.model.Idea;
import com.project.web.payload.request.SubmitIdeaRequest;
import com.project.web.service.IdeaService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
public class IdeaControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private IdeaService ideaService;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Test
    void whenValidInputWithParams_thenReturns200() throws Exception {
        Idea idea = new Idea();
        when(ideaService.getAllIdea(1)).thenReturn(Collections.singletonList(idea));
        MockHttpServletResponse response = mockMvc.perform(get("/submit_idea/all")
                        .param("pageNumber", "1")
                        .contentType("application/json"))
                .andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatus());
    }
    @Test
    void whenValidInputWithoutParams_thenReturns400() throws Exception {
        Idea idea = new Idea();
        when(ideaService.getAllIdea(null)).thenReturn(Collections.singletonList(idea) );
        MockHttpServletResponse response =  mockMvc.perform(get("/submit_idea/all")
                        .contentType("application/json"))
                .andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    void editIdeaWithInputParams_thenReturn200() throws Exception {
        SubmitIdeaRequest idea = new SubmitIdeaRequest();
        idea.setTitle("test");
        String jsonInput = mapper.writeValueAsString(idea);
        when(ideaService.editIdea(idea, null,1L)).thenReturn(null);
        MockHttpServletResponse response = mockMvc.perform(put("/submit_idea/edit/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInput))
                .andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    void getIdeaByIdWhenValidInputWithParams_thenReturns200() throws Exception {
        when(ideaService.getIdeaById(2L)).thenReturn(null);
        MockHttpServletResponse response = mockMvc.perform(get("/submit_idea/{id}", 2)
                        .contentType("application/json"))
                .andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    void getIdeaByIdWhenValidInputWithoutParamsNotExist_thenReturns404() throws Exception {
        when(ideaService.getIdeaById(null)).thenReturn(null);
        MockHttpServletResponse response = mockMvc.perform(get("/submit_idea/{id}", (Object) null)
                        .contentType("application/json"))
                .andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteIdeaSuccess_thenReturn200() throws Exception {
        when(ideaService.deleteIdea(1L, 2L)).thenReturn(null);
        MockHttpServletResponse response = mockMvc.perform(delete("/submit_idea/delete/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
    }
    @Test
    void exportToCSVFile_thenReturns200() throws Exception {
        HttpServletResponse httpResponse = mock(HttpServletResponse.class);
        when(ideaService.getAllIdeaToExport(httpResponse)).thenReturn(null);
        MockHttpServletResponse response = mockMvc.perform(get("/submit_idea/export")
                        .contentType("application/json"))
                .andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatus());
    }

    @Test
    void viewCountSuccess_thenReturns200() throws Exception {
        when(ideaService.addViewCount(1L)).thenReturn(null);
        MockHttpServletResponse response = mockMvc.perform(get("/submit_idea/viewCount/{id}",1)
                        .contentType("application/json"))
                .andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatus());
    }

    @Test
    void getLatestIdea_thenReturns200() throws Exception {
        when(ideaService.getLatestIdeas(1)).thenReturn(null);
        MockHttpServletResponse response = mockMvc.perform(get("/submit_idea/getLatestIdeas")
                        .param("pageNumber", "1")
                        .contentType("application/json"))
                .andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatus());
    }

    @Test
    void addIdeaWithInputParams_thenReturn200() throws Exception {
        SubmitIdeaRequest idea = new SubmitIdeaRequest();
        String jsonInput = mapper.writeValueAsString(idea);
        MockMultipartFile file = new MockMultipartFile("file", "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes());
        MockMvc mockMvc
                = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        when(ideaService.addIdea(idea, file)).thenReturn(null);
        MockHttpServletResponse response = mockMvc.perform(multipart("/submit_idea/add").file(file)
                        .content(jsonInput))
                .andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
    }
}
