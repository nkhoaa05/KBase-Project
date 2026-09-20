package org.example.kbase.service.auth;

import org.example.kbase.model.user.dto.AuthRequestDTO;
import org.example.kbase.model.Enum.UserRole;
import org.example.kbase.model.user.User;
import org.example.kbase.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements IAuthService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void register(AuthRequestDTO request) {
        if (userRepository.existsByEmail(request.email()
                .trim()
                .toLowerCase())
        ) throw new RuntimeException("Email already exist!");

        User newUser = new User(
                request.email(),
                passwordEncoder.encode(request.password()),
                UserRole.USER
        );

        userRepository.save(newUser);
    }

    @Override
    public void login(AuthRequestDTO request) {
        User existingUser = userRepository.findByEmail(request.email()
                                            .trim()
                                            .toLowerCase()
                )
            .orElseThrow(() ->
                new RuntimeException("Invalid email or password")
            );
        if (!passwordEncoder.matches(request.password(), existingUser.getPassword()))
            throw new RuntimeException("Invalid email or password");
    }
}
