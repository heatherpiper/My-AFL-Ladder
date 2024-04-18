package com.heatherpiper.dao;

import com.heatherpiper.model.RegisterUserDto;
import com.heatherpiper.model.User;

import java.util.List;

public interface UserDao {

    List<User> getUsers();

    User getUserById(int id);

    User getUserByUsername(String username);

    User createUser(RegisterUserDto user);

    boolean userExists(int userId);

    void updateUserLastLogin(String username);
}
