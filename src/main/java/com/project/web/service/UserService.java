package com.project.web.service;

import com.project.web.model.User;
import com.project.web.payload.request.*;
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
    ResponseEntity<ResponseObject> updateUser(EditUserRequest user, Long id);
    ResponseEntity<ResponseObject> forgotPassword(ForgotPasswordRequest user);
    ResponseEntity<ResponseObject> resetUserPassword(ResetUserPassword user, String confirmationToken);
}
