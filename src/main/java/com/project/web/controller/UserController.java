package com.project.web.controller;

import com.project.web.model.User;
import com.project.web.payload.request.LoginRequest;
import com.project.web.payload.request.SignupRequest;
import com.project.web.payload.response.JwtResponse;
import com.project.web.payload.response.ResponseObject;
import com.project.web.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/collecting_idea")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:3000")
public class UserController {
    private final UserService userSer;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        return userSer.login(loginRequest,response);
    }
    @PostMapping("/register")
    public ResponseEntity<ResponseObject> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        return userSer.addUser(signUpRequest);
    }

    @DeleteMapping("/user/delete/{id}")
    public ResponseEntity<ResponseObject> deleteUser(@PathVariable Long id){
        return userSer.deleteUser(id);
    }

    @PutMapping("/user/edit/{id}")
    public ResponseEntity<ResponseObject> updateUser(@Valid @RequestBody  User user, @PathVariable Long id){
        return  userSer.updateUser(user,id);
    }

    @GetMapping("/user")
    public List<User> getAllUser() {
        return userSer.getAllUser();
    }
}
