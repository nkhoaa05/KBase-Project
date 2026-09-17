package org.example.kbase.Services;

import org.example.kbase.Models.DTO.Request.AuthRequestDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements IAuthService{

    private final IUserService userService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(IUserService userService,PasswordEncoder passwordEncoder ){
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void register(AuthRequestDTO request) {
        userService.createUser(request);
    }

    @Override
    public void login(AuthRequestDTO request) {
    }
}
