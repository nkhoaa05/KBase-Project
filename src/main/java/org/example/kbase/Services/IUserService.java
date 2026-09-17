package org.example.kbase.Services;

import org.example.kbase.Models.DTO.Request.AuthRequestDTO;
import org.example.kbase.Models.User;


public interface IUserService {
    public void createUser(AuthRequestDTO request);
    public User updateUser();
    public User deleteUser();
    public User getUser();
}
