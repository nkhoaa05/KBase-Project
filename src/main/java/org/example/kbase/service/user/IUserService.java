package org.example.kbase.service.user;

import org.example.kbase.model.user.dto.AuthRequestDTO;
import org.example.kbase.model.user.User;


public interface IUserService {
    public void createUser(AuthRequestDTO request);
    public User updateUser();
    public User deleteUser();
    public User getUser();
}
