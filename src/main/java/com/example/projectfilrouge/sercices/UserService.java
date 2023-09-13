package com.example.projectfilrouge.sercices;

import com.example.projectfilrouge.entity.User;

import java.util.List;

public interface UserService {
    public User saveUser(User user ) ;
    public List<User> getAllUsers();
}
