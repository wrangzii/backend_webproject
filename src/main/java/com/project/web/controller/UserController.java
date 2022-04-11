package com.project.web.controller;

import com.project.web.model.User;
import com.project.web.payload.request.EditUserRequest;
import com.project.web.payload.request.SignupRequest;
import com.project.web.payload.response.ResponseObject;
import com.project.web.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:3000")
public class UserController {
    private final UserService userSer;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<ResponseObject> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        return userSer.addUser(signUpRequest);
    }

    @GetMapping("{id}")
    public ResponseEntity<ResponseObject> getUserById(@PathVariable Long id) {
        return userSer.getUserById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseObject> deleteUser(@PathVariable Long id){
        return userSer.deleteUser(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/edit/{id}")
    public ResponseEntity<ResponseObject> updateUser(@Valid @RequestBody EditUserRequest user, @PathVariable Long id){
        return  userSer.updateUser(user,id);
    }

    @GetMapping("/all")
    public List<User> getAllUser(@RequestParam int pageNumber) {
        return userSer.getAllUser(pageNumber);
    }
}
