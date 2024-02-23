package com.example.loginlab.api;

import com.example.loginlab.api.dto.UserDto;
import com.example.loginlab.app.LoginService;
import com.example.loginlab.app.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    private final LoginService loginService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(@Valid @RequestBody UserDto.SignUpRequest request) {
        userService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto.LoginResponse> login(@Valid @RequestBody UserDto.LoginRequest request) {
        return ResponseEntity.ok(loginService.login(request));
    }

    @GetMapping
    public ResponseEntity<UserDto.UserResponse> myPage(Principal principal) {
        return ResponseEntity.ok(userService.findByEmail(principal.getName()));
    }

    @GetMapping("/certification/email")
    public ResponseEntity<String> emailCertification(Principal principal) {
        return ResponseEntity.ok(userService.sendCertificationEmail(principal.getName()));
    }

    @PostMapping("/certification/email/{code}")
    public ResponseEntity<String> emailCertification(@PathVariable String code, Principal principal) {
        return ResponseEntity.ok(userService.verifyCertificationCode(principal.getName(), code));
    }

}
