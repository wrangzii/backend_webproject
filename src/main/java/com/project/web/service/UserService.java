package com.project.web.service;

import com.project.web.model.User;
import com.project.web.payload.request.LoginRequest;
import com.project.web.payload.request.SignupRequest;
import com.project.web.payload.response.ResponseObject;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface UserService {
    List<User> getAllUser(int pageNumber);
    ResponseEntity<ResponseObject> getUserById(Long id);
    ResponseEntity<ResponseObject> addUser(SignupRequest signupRequest);
    ResponseEntity<ResponseObject> login(LoginRequest loginRequest, HttpServletResponse response);
    ResponseEntity<ResponseObject> deleteUser(Long id);
    ResponseEntity<ResponseObject> updateUser(SignupRequest user, Long id);
    ResponseEntity<ResponseObject> forgotPassword(User user);
    ResponseEntity<ResponseObject> resetUserPassword(User user, String confirmationToken);
}
