package com.example.projectfilrouge.repository;

import com.example.projectfilrouge.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    @Override
    Page<User> findAll(Pageable pageable);

    List<User> findByUsernameIsContainingIgnoreCase(String name);
    Optional<User> findByUsername(String username);

}
