package com.example.visites.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.visites.models.Visite;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VisiteRepository extends JpaRepository<Visite, Long> {

    List<Visite> findByMotifContaining(String search);

	List<Visite> findByUserId(Long employeId);

	List<Visite> findByType(String type);

	List<Visite> findByDateVisiteAndType(LocalDate now, String s);

    List<Visite> findByUserIdOrType(Long employeId, String string);
}
