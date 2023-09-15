package com.example.projectfilrouge.controller;

import com.example.projectfilrouge.dto.RegistrationDto;
import com.example.projectfilrouge.entity.UserEntity;
import com.example.projectfilrouge.repository.UserRepository;
import com.example.projectfilrouge.service.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepo;



    @GetMapping("/users")
    public List<UserEntity> getAll() {
        if (userRepo.findAll().isEmpty()){
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
        return userRepo.findAll();
    }
    /*@GetMapping("/users/")
    @ResponseBody
    public List<User> getByTitle(@RequestParam String username) {
        if (userRepo.findByUsernameIsContainingIgnoreCase(username).isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return userRepo.findByUsernameIsContainingIgnoreCase(username);
    }*/
    @GetMapping("/users/{id}")
    public Optional<UserEntity> getById(@PathVariable Long id){
        if (userRepo.findById(id).isEmpty()){
            throw new ResponseStatusException((HttpStatus.NOT_FOUND));
        }
        return userRepo.findById(id);
    }

    @PutMapping("/users/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateById(@PathVariable Long id, @RequestBody UserEntity userEntity){
        //userRepo.save(new User(user.getUsername(), user.getPassword()));
    }

    @DeleteMapping("/users/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteById(@PathVariable Long id){
        if ((userRepo.findById(id).isEmpty())){
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
        userRepo.deleteById(id);
    }

    @PostMapping("/users/registration")
    public HttpStatus register(@RequestBody RegistrationDto request) {
        userService.signUpUser(request);
        return HttpStatus.CREATED;
    }

    @GetMapping(path = "/users/registration/confirm")
    public String confirm(@RequestParam("token") String token) {
        return userService.confirmToken(token);
    }
}
