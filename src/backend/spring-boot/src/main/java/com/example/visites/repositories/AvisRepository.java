package com.example.visites.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.visites.models.Avis;

@Repository
public interface AvisRepository extends JpaRepository<Avis,Long>{
	
	 @Query
	 Optional<Avis> findByVisiteId(Long id);
	 

}
