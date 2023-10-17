package com.example.visites.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.visites.models.Bureau;

import java.util.List;

@Repository
public interface BureauRepository extends JpaRepository<Bureau,Long>{

    List<Bureau> findByBatimentContainingAndEtageContainingAndPorteContaining(String batiment, String porte, String etage);

    List<Bureau> findByBatimentContainingAndEtageContaining(String batiment, String etage);

    List<Bureau> findByBatimentContainingAndPorteContaining(String batiment, String porte);

    List<Bureau> findByEtageContainingAndPorteContaining(String etage, String porte);

    List<Bureau> findByBatimentContaining(String batiment);

    List<Bureau> findByEtageContaining(String etage);

    List<Bureau> findByPorteContaining(String porte);
}
