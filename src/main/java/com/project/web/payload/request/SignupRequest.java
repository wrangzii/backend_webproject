package com.project.web.payload.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;

@Data
public class SignupRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;
    private Set<String> role;
    private String phoneNumber;
    @JsonFormat(pattern="mm/dd/yyyy HH:mm:ss")
    private Date dateOfBirth;
    private String fullName;
    private Long departmentId;
    @NotBlank
    @Size(min = 6, max = 40)
    private String password;
}
