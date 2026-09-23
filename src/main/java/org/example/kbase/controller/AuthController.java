package org.example.kbase.controller;

import jakarta.validation.Valid;
import org.example.kbase.common.response.ApiResponse;
import org.example.kbase.dto.request.AuthRequest;
import org.example.kbase.dto.response.AuthResponse;
import org.example.kbase.service.auth.IAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/auth")
public class AuthController {

    private final IAuthService authService;

    public AuthController(IAuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody AuthRequest request) {
        AuthResponse accessToken = authService.login(request);
        return ResponseEntity.ok(new ApiResponse("Login successfully", accessToken));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody AuthRequest request) {
        authService.register(request);
        return ResponseEntity.ok(new ApiResponse("Account created! You can login now", null));
    }

    @GetMapping("/me")
    public Object me(Authentication authentication) {
        return authentication.getPrincipal();
    }
}
