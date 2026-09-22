package org.example.kbase.controller;

import jakarta.validation.Valid;
import org.example.kbase.common.response.ApiResponse;
import org.example.kbase.dto.request.UpdateUserRequest;
import org.example.kbase.dto.response.UserResponse;
import org.example.kbase.service.user.IUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/users")
public class UserController {

    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable UUID userId) {
        UserResponse user = userService.getUserById(userId);
        return ResponseEntity.ok(new ApiResponse("Success", user));
    }

    @GetMapping("")
    public ResponseEntity<ApiResponse> getAllUser() {
        List<UserResponse> users = userService.getAllUser();
        return ResponseEntity.ok(new ApiResponse("success", users));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable UUID userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok(new ApiResponse("success", null));
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<ApiResponse> updateUser(@Valid @PathVariable UUID userId, @RequestBody UpdateUserRequest request) {
        userService.updateUser(userId, request);
        return ResponseEntity.ok(new ApiResponse("success", null));
    }

}
