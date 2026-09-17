package org.example.kbase.Services;

import org.example.kbase.Models.DTO.Request.AuthRequestDTO;


public interface IAuthService {

    public void register(AuthRequestDTO request);

    public void login(AuthRequestDTO request);
}
