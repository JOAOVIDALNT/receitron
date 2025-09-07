package com.joaovidal.receitron.adapter.in.web;

import com.joaovidal.receitron.adapter.in.web.dto.UserDtoMapper;
import com.joaovidal.receitron.adapter.in.web.dto.UserLoginRequest;
import com.joaovidal.receitron.adapter.in.web.dto.UserLoginResponse;
import com.joaovidal.receitron.adapter.in.web.dto.UserSignupRequest;
import com.joaovidal.receitron.domain.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth/")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("signup")
    public ResponseEntity<?> reigster(@RequestBody UserSignupRequest request) {
        authService.register(UserDtoMapper.toDomain(request));

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("login")
    public ResponseEntity<UserLoginResponse> login(@RequestBody UserLoginRequest request) {
        var response = authService.authenticate(request.email(), request.password());

        return ResponseEntity.ok(response);
    }

}
