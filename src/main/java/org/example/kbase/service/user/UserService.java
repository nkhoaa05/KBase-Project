package org.example.kbase.service.user;

import org.example.kbase.model.user.dto.AuthRequestDTO;
import org.example.kbase.model.Enum.UserRole;
import org.example.kbase.model.user.User;
import org.example.kbase.repository.UserRepository;
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
