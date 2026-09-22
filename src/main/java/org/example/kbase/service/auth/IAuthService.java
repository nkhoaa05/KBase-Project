package org.example.kbase.service.auth;

import org.example.kbase.dto.request.AuthRequest;


public interface IAuthService {

    void register(AuthRequest request);

    void login(AuthRequest request);
}
