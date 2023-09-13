package com.example.projectfilrouge.controller;

import com.example.projectfilrouge.entity.User;
import com.example.projectfilrouge.sercices.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin("*")
@RestController
@RequestMapping("/user")
public class UserController {

  @Autowired
    private UserService userService ;
@PostMapping("/add")
    public String add(@RequestBody User user) {
    userService.saveUser(user);
    return  "  New User is added " ;
}

    public List<User> getAllUsers() {
    return  userService.getAllUsers();
}
}
