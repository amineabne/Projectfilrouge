package com.example.projectfilrouge.repository;

import com.example.projectfilrouge.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    @RestResource(path = "/byDesignationpage")
public Page<User> findByUsernameContaining(@Param("mc")String des, Pageable pageable);

}
