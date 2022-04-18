package com.project.web.controller;

import com.project.web.payload.request.ForgotPasswordRequest;
import com.project.web.payload.request.LoginRequest;
import com.project.web.payload.request.ResetUserPassword;
import com.project.web.payload.response.ResponseObject;
import com.project.web.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userSer;

    @PostMapping("/login")
    public ResponseEntity<ResponseObject> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        return userSer.login(loginRequest,response);
    }

    @PostMapping("/forgot_password")
    public ResponseEntity<ResponseObject> forgotUserPassword(@RequestBody ForgotPasswordRequest user) {
        return userSer.forgotPassword(user);
    }

    @PostMapping("/confirm_reset")
    public ResponseEntity<ResponseObject> resetUserPassword(@RequestParam("token")String confirmationToken, @RequestBody ResetUserPassword user) {
        return userSer.resetUserPassword(user, confirmationToken);
    }
}
