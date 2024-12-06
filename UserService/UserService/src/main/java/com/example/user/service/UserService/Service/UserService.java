package com.example.user.service.UserService.Service;

import com.example.user.service.UserService.Entities.User;

import java.util.List;

public interface UserService {
    User saveUser(User user);

    List<User> getAllUser();

    User getUser(String userId);

    //TODO
    //delete ,update

}
