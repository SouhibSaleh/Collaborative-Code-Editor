package com.example.lastproject.controller.sharedArea;

import com.example.lastproject.model.User;
import com.example.lastproject.model.enums.Role;
import com.example.lastproject.model.requests.AuthRequest;
import com.example.lastproject.model.responses.AuthResponse;
import com.example.lastproject.service.JwtUtil;
import com.example.lastproject.service.UserService;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.LocalDateTime;

@RestController
public class AuthenticationController {

    private UserService userService;
    private PasswordEncoder passwordEncoder;
    private JwtDecoder jwtDecoder;
    public AuthenticationController(UserService userService,
                                    PasswordEncoder passwordEncoder,
                                    JwtDecoder jwtDecoder){
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtDecoder = jwtDecoder;
    }
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody AuthRequest authRequest){
        var user = prepareUser(authRequest.getEmail(), authRequest.isAdmin()?Role.Admin:Role.User,
                passwordEncoder.encode(authRequest.getPassword()), LocalDateTime.now());
        var response = userService.saveUser(user);
        AuthResponse authResponse = new AuthResponse();
        if(response==null) {
            authResponse.setMessage("something went wrong, change password or email");
            return ResponseEntity.badRequest().body(authResponse);
        }
        authResponse.setMessage("Account created successfully, now you can log in !");
        return ResponseEntity.ok(authResponse);
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        User user = userService.getUserByEmail(authRequest.getEmail());
        var authResponse = new AuthResponse();
        if(user==null) {
            authResponse.setMessage("email: "+authRequest.getEmail()+" not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(authResponse);
        }
        if ((user.getRole().equals(Role.Admin)) != authRequest.isAdmin()){
            authResponse.setMessage("wrong role");
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(authResponse);
        }
        if(!passwordEncoder.matches(authRequest.getPassword(),user.getPassword())){
            authResponse.setMessage("wrong password");
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(authResponse);
        }
        return ResponseEntity.ok(prepareAuthResponse(user));
    }
    @PostMapping("/google")
    public ResponseEntity<?> googleLogin(@RequestBody String token) throws ParseException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        String email = signedJWT.getJWTClaimsSet().getStringClaim("email");
        User user= userService.getUserByEmail(email);
        AuthResponse authResponse = new AuthResponse();
        if(user ==null){
            User u = prepareUser(email,Role.User,"",LocalDateTime.now());
            userService.saveUser(u);
            authResponse.setMessage("Account created successfully, now you can log in !");
            return ResponseEntity.ok(authResponse);
        }
        return ResponseEntity.ok(prepareAuthResponse(user));
    }

    private AuthResponse prepareAuthResponse(User user){
        AuthResponse authResponse = new AuthResponse();
        authResponse.setId(user.getId());
        authResponse.setEmail(user.getEmail());
        authResponse.setToken(JwtUtil.generateToken(user.getEmail(),user.getRole(),user.getId()+""));
        authResponse.setRole(user.getRole().name());
        return authResponse;
    }
    private User prepareUser(String email,Role role,String password,LocalDateTime time){
        User u = new User();
        u.setEmail(email);
        u.setRole(role);
        u.setPassword("");
        u.setCreationDate(time);
        return u;
    }

}
