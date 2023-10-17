package com.example.visites.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.visites.models.Role;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{

    List<Role> findByNomContaining(String name);

}
