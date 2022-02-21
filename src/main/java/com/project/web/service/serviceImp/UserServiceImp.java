package com.project.web.service.serviceImp;

import com.project.web.model.ERole;
import com.project.web.model.Role;
import com.project.web.model.User;
import com.project.web.payload.request.LoginRequest;
import com.project.web.payload.request.SignupRequest;
import com.project.web.payload.response.JwtResponse;
import com.project.web.payload.response.MessageResponse;
import com.project.web.repository.RoleRepository;
import com.project.web.repository.UserRepository;
import com.project.web.security.jwt.JwtUtils;
import com.project.web.security.service.UserDetailsImpl;
import com.project.web.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {
    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Override
    public List<User> getAllUser() {
        return userRepo.findAll();
    }

    @Override
    public ResponseEntity<MessageResponse> addUser(SignupRequest signUpRequest){
        if (userRepo.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }
        if (userRepo.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }
        // Create new user's account
        User user = new User(signUpRequest.getEmail()
                , signUpRequest.getUsername(), signUpRequest.getFullName(), signUpRequest.getPhoneNumber()
                , signUpRequest.getDateOfBirth(), signUpRequest.getDepartmentId()
                , encoder.encode(signUpRequest.getPassword()));
        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();
        if (strRoles == null) {
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
        user.setRoles(roles);
        userRepo.save(user);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @Override
    public ResponseEntity<JwtResponse> login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getUserId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                userDetails.getPhoneNumber(),
                userDetails.getDateOfBirth(),
                userDetails.getFullName(),
                roles));
    }

    @Override
    public ResponseEntity<MessageResponse> deleteUser(Long id) {
        Optional<User> deleteUser = userRepo.findById(id);
        if (deleteUser.isPresent()) {
            userRepo.deleteById(id);
            return ResponseEntity.ok(new MessageResponse("Delete user successfully!"));
        }
        return ResponseEntity.badRequest().body(new MessageResponse("User is not exist"));
    }

    @Override
    public ResponseEntity<MessageResponse> updateUser(User user, Long id) {
        Optional<User> editUser = userRepo.findById(id);
        if (editUser.isPresent()){
            editUser.get().setDepartmentId(user.getDepartmentId());
            editUser.get().setEmail(user.getEmail());
            editUser.get().setUsername(user.getUsername());
            editUser.get().setFullName(user.getFullName());
            editUser.get().setPassword(user.getPassword());
            editUser.get().setDateOfBirth(user.getDateOfBirth());
            editUser.get().setPhoneNumber(user.getPhoneNumber());
            editUser.get().setRoles(user.getRoles());

            userRepo.save(editUser.get());
            return ResponseEntity.ok(new MessageResponse("Edit user successfully!"));
        }
        return null;
    }
}
