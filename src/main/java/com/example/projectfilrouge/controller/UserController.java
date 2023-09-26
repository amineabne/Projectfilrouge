package com.example.projectfilrouge.controller;

import com.example.projectfilrouge.dto.AllTicketDto;
import com.example.projectfilrouge.dto.JwtResponse;
import com.example.projectfilrouge.dto.LoginDto;
import com.example.projectfilrouge.dto.UserDto;
import com.example.projectfilrouge.entity.UserEntity;
import com.example.projectfilrouge.security.jwt.JwtUtils;
import com.example.projectfilrouge.service.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin("*")
public class UserController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtils jwtUtils;

    @GetMapping("/users")
    public ResponseEntity<List<UserEntity>> getAll() {
        return userService.findAll();
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserEntity> getById(@PathVariable Long id){
        return userService.findById(id);
    }

    @PutMapping("/users/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateById(@PathVariable Long id, @RequestBody UserDto userDto){
        userService.updateUser(id, userDto);
    }

    @DeleteMapping("/users/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteById(@PathVariable Long id){
       userService.deleteUserById(id);
    }

    @PostMapping("/registration")
    public HttpStatus register(@RequestBody UserDto request) {
        userService.signUpUser(request);
        return HttpStatus.CREATED;
    }

    @GetMapping(path = "/registration/confirm")
    public String confirm(@RequestParam("token") String token) {
        return userService.confirmToken(token);
    }
    @PostMapping("/registration/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateJwtToken(authentication);

        return ResponseEntity.ok(new JwtResponse(jwt));
    }
    @GetMapping("/users/tickets")
    public AllTicketDto getAllUserRelatedTicket() {
        return userService.getAllUserRelatedTicket();
    }
}
