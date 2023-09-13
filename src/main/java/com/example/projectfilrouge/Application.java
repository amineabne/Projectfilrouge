package com.example.projectfilrouge;

import com.example.projectfilrouge.entity.User;
import com.example.projectfilrouge.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;


@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;




    public static void main(String[] args) {

            SpringApplication.run(Application.class, args);
        }

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        userRepository.save(new User(null,"amine","123","email","photo",null,null));
        userRepository.save(new User(null,"junaid","123","email","photo",null,null));
        userRepository.findAll().forEach(u-> {System.out.println(u.toString());});
    }
}
