package org.example.kbase.controller;

import org.example.kbase.common.response.ApiResponse;
import org.example.kbase.dto.request.AuthRequest;
import org.example.kbase.dto.response.AuthResponse;
import org.example.kbase.service.auth.IAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("${api.prefix}/auth")
@Tag(name = "Auth APIs")
public class AuthController {

    private final IAuthService authService;

    public AuthController(IAuthService authService) {
        this.authService = authService;
    }

    @Operation(
            summary = "Login"
    )
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody AuthRequest request) {
        AuthResponse accessToken = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Login successfully", accessToken));
    }

    @Operation(
            summary = "Sign up"
    )
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Void>> signup(@Valid @RequestBody AuthRequest request) {
        authService.signup(request);
        return ResponseEntity.ok(ApiResponse.success("Account created! You can login now", null));
    }

    @GetMapping("/me")
    public Object me(Authentication authentication) {
        return authentication.getDetails();
    }
}
