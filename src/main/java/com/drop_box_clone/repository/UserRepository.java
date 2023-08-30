package com.drop_box_clone.repository;

import com.drop_box_clone.entites.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    User findByUsername(String username);
}
