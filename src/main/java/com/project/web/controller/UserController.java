package com.project.web.controller;

import com.project.web.model.User;
import com.project.web.payload.request.SignupRequest;
import com.project.web.payload.response.ResponseObject;
import com.project.web.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:3000")
public class UserController {
    private final UserService userSer;

    @PostMapping("/add")
    public ResponseEntity<ResponseObject> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        return userSer.addUser(signUpRequest);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseObject> deleteUser(@PathVariable Long id){
        return userSer.deleteUser(id);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<ResponseObject> updateUser(@Valid @RequestBody  SignupRequest user, @PathVariable Long id){
        return  userSer.updateUser(user,id);
    }

    @GetMapping("/all")
    public List<User> getAllUser() {
        return userSer.getAllUser();
    }
}
