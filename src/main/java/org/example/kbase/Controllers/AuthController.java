package org.example.kbase.Controllers;

import jakarta.validation.Valid;
import org.example.kbase.Models.DTO.Request.AuthRequestDTO;
import org.example.kbase.Services.IAuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final IAuthService authService;

    public AuthController(IAuthService authService){
        this.authService = authService;
    }

    @PostMapping("/login")
    public String login(@Valid @RequestBody AuthRequestDTO request){
        return "Login successfully: \n" + request.email() + "\npassword: " + request.password();
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody AuthRequestDTO request){
        authService.register(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }
}
