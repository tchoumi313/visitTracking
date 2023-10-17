package com.example.visites.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.visites.models.Visiteur;

import java.util.List;

@Repository
public interface VisiteurRepository extends JpaRepository<Visiteur, Long> {

    List<Visiteur> findByNomContainingOrPrenomContainingOrEmailContainingOrTelContaining(String search, String search1, String search2, String search3);
}
