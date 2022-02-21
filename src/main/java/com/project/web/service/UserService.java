package com.project.web.service;

import com.project.web.model.User;
import com.project.web.payload.request.LoginRequest;
import com.project.web.payload.request.SignupRequest;
import com.project.web.payload.response.JwtResponse;
import com.project.web.payload.response.MessageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

public interface UserService {
    List<User> getAllUser();
    ResponseEntity<MessageResponse> addUser(SignupRequest signupRequest);
    ResponseEntity<JwtResponse> login(LoginRequest loginRequest);
    ResponseEntity<MessageResponse> deleteUser(Long id);
    ResponseEntity<MessageResponse> updateUser(User user, Long id);
}
