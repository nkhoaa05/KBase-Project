package org.example.kbase.Services;

import org.example.kbase.Models.DTO.Request.AuthRequestDTO;
import org.example.kbase.Models.Enum.UserRole;
import org.example.kbase.Models.User;
import org.example.kbase.Repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void createUser(AuthRequestDTO request) {
        String email = request.email()
                .trim()
                .toLowerCase();
        if (userRepository.existsByEmail(email)) throw new RuntimeException("Email already exists");
        String hashedPassword = passwordEncoder.encode(request.password());
        User newUser = new User(
                email,
                hashedPassword,
                UserRole.USER
        );
        userRepository.save(newUser);
    }

    @Override
    public User updateUser() {
        return null;
    }

    @Override
    public User deleteUser() {
        return null;
    }

    @Override
    public User getUser() {
        return null;
    }
}
