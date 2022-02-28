package com.project.web.controller;

import com.project.web.model.User;
import com.project.web.payload.request.LoginRequest;
import com.project.web.payload.request.SignupRequest;
import com.project.web.payload.response.JwtResponse;
import com.project.web.payload.response.ResponseObject;
import com.project.web.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userSer;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        return userSer.login(loginRequest,response);
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseObject> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        return userSer.addUser(signUpRequest);
    }

    @PostMapping("/logout")
    public ResponseObject logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
       return new ResponseObject(HttpStatus.OK.toString(),"Logout successfully!");
    }

    @PostMapping("/forgot_password")
    public ResponseEntity<ResponseObject> forgotUserPassword(@RequestBody User user) {
        return userSer.forgotPassword(user);
    }

    @PostMapping("/confirm_reset")
    public ResponseEntity<ResponseObject> resetUserPassword(@RequestParam("token")String confirmationToken, @RequestBody User user) {
        return userSer.resetUserPassword(user, confirmationToken);
    }
}
