package com.example.projectfilrouge.controller;

import com.example.projectfilrouge.entity.User;
import com.example.projectfilrouge.repository.UserRepository;
import com.example.projectfilrouge.sercices.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;


}
