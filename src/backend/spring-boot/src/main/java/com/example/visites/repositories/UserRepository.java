package com.example.visites.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.visites.models.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByNomContainingOrPrenomContainingOrUsernameContainingOrEmailContainingOrTelContaining(String search, String search1, String search2, String search3, String search4);

    Optional<User> findByEmailOrUsername(String email, String username);
}
