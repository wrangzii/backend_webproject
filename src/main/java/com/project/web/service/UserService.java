package com.project.web.service;

import com.project.web.model.User;
import com.project.web.payload.request.LoginRequest;
import com.project.web.payload.request.SignupRequest;
import com.project.web.payload.response.JwtResponse;
import com.project.web.payload.response.ResponseObject;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface UserService {
    List<User> getAllUser();
    ResponseEntity<ResponseObject> addUser(SignupRequest signupRequest);
    ResponseEntity<JwtResponse> login(LoginRequest loginRequest, HttpServletResponse response);
    ResponseEntity<ResponseObject> deleteUser(Long id);
    ResponseEntity<ResponseObject> updateUser(User user, Long id);
    ResponseEntity<ResponseObject> forgotPassword(User user);
    ResponseEntity<ResponseObject> resetUserPassword(User user, String confirmationToken);

}
