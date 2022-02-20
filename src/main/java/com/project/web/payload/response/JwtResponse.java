package com.project.web.payload.response;

import lombok.Data;

import java.util.List;

@Data
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String email;
    private String phoneNumber;
    private String dateOfBirth;
    private String fullName;
    private List<String> roles;

    public JwtResponse(String accessToken, Long id, String username, String email,
                       String phoneNumber, String dateOfBirth, String fullName,List<String> roles) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.fullName = fullName;
        this.roles = roles;
    }
}
