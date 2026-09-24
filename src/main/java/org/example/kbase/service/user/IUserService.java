package org.example.kbase.service.user;

import org.example.kbase.dto.request.AuthRequest;
import org.example.kbase.dto.request.CreateUserRequest;
import org.example.kbase.dto.request.UpdateUserRequest;
import org.example.kbase.dto.response.UserResponse;

import java.util.List;
import java.util.UUID;


public interface IUserService {

    void createUser(CreateUserRequest request);

    void updateUser(UUID userId, UpdateUserRequest request);

    void deleteUser(UUID userId);

    UserResponse getUserById(UUID userId);

    List<UserResponse> getAllUser();
}
