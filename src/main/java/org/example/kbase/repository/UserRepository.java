package org.example.kbase.Repositories;

import org.example.kbase.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    @Query()
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}
