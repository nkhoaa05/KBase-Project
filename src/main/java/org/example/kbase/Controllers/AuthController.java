package org.example.kbase.Controllers;

import jakarta.validation.Valid;
import org.example.kbase.Models.DTO.Request.AuthRequestDTO;
import org.example.kbase.Models.DTO.Request.Response.ApiResponse;
import org.example.kbase.Services.IAuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/auth")
public class AuthController {

    private final IAuthService authService;

    public AuthController(IAuthService authService){
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody AuthRequestDTO request){
        authService.login(request);
        return ResponseEntity.ok(new ApiResponse("Login successfully", null));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody AuthRequestDTO request){
        authService.register(request);
        return ResponseEntity.ok(new ApiResponse("Account created! You can login now", null));
    }
}
