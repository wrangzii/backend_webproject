package com.project.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.web.model.User;
import com.project.web.payload.request.LoginRequest;
import com.project.web.service.UserService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @Autowired
    ObjectMapper mapper;

    @Test
    void loginWithParams_thenReturn200() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("test");
        loginRequest.setPassword("test");
        String jsonInput = mapper.writeValueAsString(loginRequest);
        when(userService.login(loginRequest, null)).thenReturn(null);
        MockHttpServletResponse response = mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInput))
                .andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    void loginWithoutParams_thenReturn400() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        String jsonInput = mapper.writeValueAsString(loginRequest);
        when(userService.login(loginRequest, null)).thenReturn(null);
        MockHttpServletResponse response = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInput))
                .andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    void forgotPasswordWithParams_thenReturn200() throws Exception {
        User user = new User();
        user.setEmail("");
        String jsonInput = mapper.writeValueAsString(user);
        when(userService.forgotPassword(user)).thenReturn(null);
        MockHttpServletResponse response = mockMvc.perform(post("/forgot_password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonInput)).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    void forgotPasswordWithoutParams_thenReturn400() throws Exception {
        when(userService.forgotPassword(null)).thenReturn(null);
        MockHttpServletResponse response = mockMvc.perform(post("/forgot_password")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    void confirmResetWithParams_thenReturn200() throws Exception {
        User user = new User();
        String jsonInput = mapper.writeValueAsString(user);
        when(userService.resetUserPassword(user,"test")).thenReturn(null);
        MockHttpServletResponse response = mockMvc.perform(post("/confirm_reset")
                        .param("token", "test")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonInput)).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    void confirmResetPasswordWithoutParams_thenReturn400() throws Exception {
        when(userService.resetUserPassword(null,"")).thenReturn(null);
        MockHttpServletResponse response = mockMvc.perform(post("/confirm_reset")
                        .param("token", "")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }
}
