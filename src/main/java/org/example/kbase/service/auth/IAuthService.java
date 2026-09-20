package org.example.kbase.service.auth;

import org.example.kbase.model.user.dto.AuthRequestDTO;


public interface IAuthService {

    public void register(AuthRequestDTO request);

    public void login(AuthRequestDTO request);
}
