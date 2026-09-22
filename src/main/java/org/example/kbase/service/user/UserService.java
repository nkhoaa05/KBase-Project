package org.example.kbase.service.user;

import org.example.kbase.dto.request.AuthRequest;
import org.example.kbase.dto.request.UpdateUserRequest;
import org.example.kbase.dto.response.UserResponse;
import org.example.kbase.model.Enum.UserRole;
import org.example.kbase.model.User;
import org.example.kbase.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void createUser(AuthRequest request) {
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
    @Transactional
    public void updateUser(UUID userId, UpdateUserRequest request) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found: ID " + userId));

        if (passwordEncoder.matches(request.password(),
                                    existingUser.getPassword())){
            throw new RuntimeException("New password must be different from the old one");
        }

        String hashedPassword = passwordEncoder.encode(request.password());

        existingUser.setPassword(hashedPassword);
    }

    @Override
    @Transactional
    public void deleteUser(UUID userId) {
        if (!userRepository.existsById(userId)) throw new RuntimeException("User not found");
        userRepository.deleteById(userId);
    }

    @Override
    public UserResponse getUserById(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found: ID " + userId)
                );

        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getRole().name()
        );
    }

    @Override
    public List<UserResponse> getAllUser() {
        return userRepository.findAll()
                .stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getEmail(),
                        user.getRole().name()
                ))
                .toList();
    }
}
