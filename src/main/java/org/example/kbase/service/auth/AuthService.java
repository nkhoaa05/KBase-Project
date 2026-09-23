package org.example.kbase.service.auth;

import org.example.kbase.common.security.jwt.JwtService;
import org.example.kbase.common.security.user.UserDetailSecurity;
import org.example.kbase.dto.request.AuthRequest;
import org.example.kbase.dto.response.AuthResponse;
import org.example.kbase.model.Enum.UserRole;
import org.example.kbase.model.User;
import org.example.kbase.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService implements IAuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    AuthenticationManager authenticationManager;
    private final JwtService jwtService;


    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    @Transactional
    public void register(AuthRequest request) {
        String email = request.email()
                .trim()
                .toLowerCase();

        if (userRepository.existsByEmail(email)) throw new RuntimeException("Email already exist!");

        User newUser = new User(
                email,
                passwordEncoder.encode(request.password()),
                UserRole.USER
        );

        userRepository.save(newUser);
    }

    @Override
    public AuthResponse login(AuthRequest request) {
        Authentication authenticationRequest = UsernamePasswordAuthenticationToken
                .unauthenticated(request.email(), request.password());


        Authentication authentication =
                authenticationManager
                        .authenticate(authenticationRequest);

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof UserDetailSecurity userDetail)) {
            throw new RuntimeException("Not authenticated");
        }

        String accessToken = jwtService.generateToken(userDetail);

        return new AuthResponse(accessToken);
    }
}
