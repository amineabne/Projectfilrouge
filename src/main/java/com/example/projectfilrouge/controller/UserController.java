package com.example.projectfilrouge.controller;

import com.example.projectfilrouge.entity.User;
import com.example.projectfilrouge.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @GetMapping(path="/index")
    public ResponseEntity<?> version() {
        return ResponseEntity.ok("1.0");
    }


}
