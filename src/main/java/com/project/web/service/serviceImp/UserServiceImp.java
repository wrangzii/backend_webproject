package com.project.web.service.serviceImp;

import com.project.web.model.Department;
import com.project.web.model.ERole;
import com.project.web.model.Role;
import com.project.web.model.User;
import com.project.web.payload.request.*;
import com.project.web.payload.response.JwtResponse;
import com.project.web.payload.response.ResponseObject;
import com.project.web.repository.RoleRepository;
import com.project.web.repository.UserRepository;
import com.project.web.security.jwt.JwtUtils;
import com.project.web.security.service.UserDetailsImpl;
import com.project.web.service.EmailSenderService;
import com.project.web.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImp implements UserService {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private RoleRepository roleRepo;
    @Qualifier("passwordEncoder")
    private final PasswordEncoder encoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private EmailSenderService emailSenderService;

    @Override
    public List<User> getAllUser(int pageNumber) {
        int pageSize = 10;
        Pageable paging = PageRequest.of(pageNumber,pageSize);
        Page<User> pagedResult = userRepo.findAll(paging);

        if(pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public ResponseEntity<ResponseObject> getUserById(Long id) {
        Optional<User> existedUser = userRepo.findById(id);
        if (existedUser.isPresent()) {
            return ResponseEntity.ok(new ResponseObject(HttpStatus.OK.toString(),"Get user successfully!", existedUser));
        }
        return ResponseEntity.badRequest().body(new ResponseObject(HttpStatus.NOT_FOUND.toString(),"User is not exist"));
    }

    @Override
    public ResponseEntity<ResponseObject> addUser(SignupRequest signUpRequest){
        if (userRepo.findByUsername(signUpRequest.getUsername()).isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new ResponseObject(HttpStatus.BAD_REQUEST.toString(), "Error: Username is already taken!"));
        }
        if (userRepo.findByEmail(signUpRequest.getEmail()).isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new ResponseObject(HttpStatus.BAD_REQUEST.toString(),"Error: Email is already in use!"));
        }
        // Create new user's account
        User user = new User(signUpRequest.getEmail()
                , signUpRequest.getUsername(), signUpRequest.getFullName(), signUpRequest.getPhoneNumber()
                , signUpRequest.getDateOfBirth()
                , encoder.encode(signUpRequest.getPassword()));
        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();
        validateRole(strRoles, roles);
        Department department = new Department();
        department.setDepartmentId(signUpRequest.getDepartmentId());
        user.setDepartmentId(department);
        user.setRoles(roles);
        user.setEnabled(true);
        userRepo.save(user);
        return ResponseEntity.ok(new ResponseObject(HttpStatus.CREATED.toString(),"User registered successfully!", user));
    }

    @Override
    public ResponseEntity<ResponseObject> login(LoginRequest loginRequest, HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
            JwtResponse userResponse = new JwtResponse(jwt,
                    userDetails.getUserId(),
                    userDetails.getUsername(),
                    userDetails.getEmail(),
                    userDetails.getPhoneNumber(),
                    userDetails.getDateOfBirth(),
                    userDetails.getFullName(),
                    roles);
            Cookie cookie = new Cookie("token", jwt);
            cookie.setPath("/");
            cookie.setSecure(true);
            cookie.setHttpOnly(true);
            response.addCookie(cookie);
            response.addHeader("Authorization", jwt);
            return ResponseEntity.ok(new ResponseObject(userResponse));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObject(HttpStatus.UNAUTHORIZED.toString(),"Username or password is incorrect!"));
        }
    }

    @Override
    public ResponseEntity<ResponseObject> deleteUser(Long id) {
        Optional<User> deleteUser = userRepo.findById(id);
        if (deleteUser.isPresent()) {
            deleteUser.get().setEnabled(false);
            userRepo.save(deleteUser.get());
            return ResponseEntity.ok(new ResponseObject(HttpStatus.OK.toString(),"Delete user successfully!"));
        }
        return ResponseEntity.badRequest().body(new ResponseObject(HttpStatus.NOT_FOUND.toString(),"User is not exist"));
    }

    @Override
    public ResponseEntity<ResponseObject> updateUser(EditUserRequest user, Long id) {
        Optional<User> editUser = userRepo.findById(id);
        if (editUser.isPresent()){
            editUser.get().setEmail(user.getEmail());
            editUser.get().setFullName(user.getFullName());
            editUser.get().setDateOfBirth(user.getDateOfBirth());
            editUser.get().setPhoneNumber(user.getPhoneNumber());
            editUser.get().setEnabled(true);
            Department department = new Department();
            department.setDepartmentId(user.getDepartmentId());
            editUser.get().setDepartmentId(department);
            Set<String> strRoles = user.getRole();
            Set<Role> roles = new HashSet<>();
            validateRole(strRoles, roles);
            editUser.get().setRoles(roles);
            userRepo.save(editUser.get());
            return ResponseEntity.ok(new ResponseObject(HttpStatus.OK.toString(), "Edit user successfully!", editUser));
        }
        return ResponseEntity.badRequest().body(new ResponseObject(HttpStatus.NOT_FOUND.toString(),"User is not exist"));
    }

    @Override
    public ResponseEntity<ResponseObject> forgotPassword(ForgotPasswordRequest user) {
        Optional<User> existedUser = userRepo.findByEmail(user.getEmail());
        if (existedUser.isPresent()) {
            // Save it
            existedUser.get().setResetPasswordToken(UUID.randomUUID().toString());
            userRepo.save(existedUser.get());

            // Create the email
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(existedUser.get().getEmail());
            mailMessage.setSubject("Complete Password Reset!");
            mailMessage.setFrom("test-email@gmail.com");
            mailMessage.setText("To complete the password reset process, please click here: "
                    + "http://localhost:3000/confirm_reset?token=" + existedUser.get().getResetPasswordToken());

            // Send the email
            emailSenderService.sendEmail(mailMessage);

            return ResponseEntity.ok(new ResponseObject(HttpStatus.OK.toString(), "Request to reset password received. Check your inbox for the reset link."));

        } else {
            return ResponseEntity.badRequest().body(new ResponseObject(HttpStatus.OK.toString(), "This email address does not exist!"));
        }
    }

    @Override
    public ResponseEntity<ResponseObject> resetUserPassword(ResetUserPassword user, String confirmationToken) {
        User resetPasswordToken = userRepo.findByResetPasswordToken(confirmationToken);

        if (resetPasswordToken != null) {
            Optional<User> userReset = userRepo.findByEmail(resetPasswordToken.getEmail());
            // Use email to find user
            if (userReset.isPresent()) {
                userReset.get().setPassword(encoder.encode(user.getPassword()));
                userRepo.save(userReset.get());
            }
            return ResponseEntity.ok(new ResponseObject(HttpStatus.OK.toString(), "Password successfully reset. You can now log in with the new credentials."));
        } else {
            return ResponseEntity.badRequest().body(new ResponseObject(HttpStatus.NOT_FOUND.toString(), "The link is invalid or broken!"));
        }
    }
    private void validateRole(Set<String> strRoles, Set<Role> roles) {
        if (strRoles.isEmpty()) {
            Role userRole = roleRepo.findByRoleName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepo.findByRoleName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                        break;
                    case "qa_manager":
                        Role qaManagerRole = roleRepo.findByRoleName(ERole.ROLE_QA_MANAGER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(qaManagerRole);
                        break;
                    case "qa_coordinator":
                        Role qaCoordinatorRole = roleRepo.findByRoleName(ERole.ROLE_QA_COORDINATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(qaCoordinatorRole);
                        break;
                    default:
                        Role userRole = roleRepo.findByRoleName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }
    }
}
