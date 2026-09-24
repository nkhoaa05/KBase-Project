package org.example.kbase.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.kbase.common.response.ApiResponse;
import org.example.kbase.dto.request.CreateUserRequest;
import org.example.kbase.dto.request.UpdateUserRequest;
import org.example.kbase.dto.response.UserResponse;
import org.example.kbase.service.user.IUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/users")
@Tag(name = "User APIs")
public class UserController {

    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Create new user"
    )
    @PostMapping("")
    public ResponseEntity<ApiResponse<Void>> createUser(@Valid @RequestBody CreateUserRequest request){
        userService.createUser(request);
        return ResponseEntity.ok(new ApiResponse<Void>("success", null));
    }

    @Operation(
            summary = "Get user by ID"
    )
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable UUID userId) {
        UserResponse user = userService.getUserById(userId);
        return ResponseEntity.ok(new ApiResponse<>("Success", user));
    }

    @Operation(
            summary = "Get all user"
    )
    @GetMapping("")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUser() {
        List<UserResponse> users = userService.getAllUser();
        return ResponseEntity.ok(new ApiResponse<>("success", users));
    }

    @Operation(
            summary = "Delete user by ID"
    )
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable UUID userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok(new ApiResponse<Void>("success", null));
    }

    @Operation(
            summary = "Update user information (password)"
    )
    @PatchMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> updateUser(@Valid @PathVariable UUID userId, @RequestBody UpdateUserRequest request) {
        userService.updateUser(userId, request);
        return ResponseEntity.ok(new ApiResponse<Void>("success", null));
    }

}
