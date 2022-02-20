package com.project.web.controller;

import com.project.web.model.User;
import com.project.web.payload.request.LoginRequest;
import com.project.web.payload.request.SignupRequest;
import com.project.web.payload.response.JwtResponse;
import com.project.web.payload.response.MessageResponse;
import com.project.web.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/collecting_idea")
@RequiredArgsConstructor
public class UserController {
    private final UserService userSer;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return userSer.login(loginRequest);
    }
    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        return userSer.addUser(signUpRequest);
    }

    @DeleteMapping("/user/delete/{id}")
    public ResponseEntity<MessageResponse> deleteUser(@PathVariable Long id){
        return userSer.deleteUser(id);
    }

    @PutMapping("/user/edit/{id}")
    public ResponseEntity<MessageResponse> updateUser(@Valid @RequestBody  User user, @PathVariable Long id){
        return  userSer.updateUser(user,id);
    }

    @GetMapping("/user")
    public List<User> getAllUser() {
        return userSer.getAllUser();
    }
}
