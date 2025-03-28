package com.Illia.service;

import java.util.ArrayList;
import java.util.List;

import com.Illia.dto.UserDTO;

public class UserService {

    public List<UserDTO> getAllUsers() {
        // This method would typically interact with a database to retrieve user data.
        // Below is a mock implementation for demonstration purposes.
        List<UserDTO> users = new ArrayList<>();
        users.add(new UserDTO(1, "John Doe", "john.doe@example.com"));
        users.add(new UserDTO(2, "Jane Smith", "jane.smith@example.com"));
        return users;
    }
}