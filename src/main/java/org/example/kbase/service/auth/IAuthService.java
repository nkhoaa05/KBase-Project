package org.example.kbase.service.auth;

import org.example.kbase.dto.request.AuthRequest;
import org.example.kbase.dto.response.AuthResponse;


public interface IAuthService {

    void register(AuthRequest request);

    AuthResponse login(AuthRequest request);
}
