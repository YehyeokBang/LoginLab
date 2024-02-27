package com.example.loginlab.api;

import com.example.loginlab.api.dto.UserDto;
import com.example.loginlab.app.LoginService;
import com.example.loginlab.app.UserService;
import com.example.loginlab.common.annotation.CurrentUser;
import com.example.loginlab.common.annotation.UserLevelCheck;
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
    @UserLevelCheck
    public ResponseEntity<UserDto.UserResponse> myPage(@CurrentUser String email) {
        return ResponseEntity.ok(userService.findByEmail(email));
    }

    @GetMapping("/certification/email")
    @UserLevelCheck
    public ResponseEntity<String> emailCertification(@CurrentUser String email) {
        return ResponseEntity.ok(userService.sendCertificationEmail(email));
    }

    @PostMapping("/certification/email/{code}")
    @UserLevelCheck
    public ResponseEntity<String> emailCertification(@CurrentUser String email, @PathVariable String code) {
        return ResponseEntity.ok(userService.verifyCertificationCode(email, code));
    }

}
