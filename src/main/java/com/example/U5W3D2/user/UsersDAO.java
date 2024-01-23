package com.example.U5W3D2.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsersDAO extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    Page<User> findByName(String name, Pageable pageable);
    Page<User> findBySurname(String surname, Pageable pageable);
    Page<User> findByNameAndSurname(String name, String surname, Pageable pageable);

}
