package com.example.visites.repositories;

import com.example.visites.models.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
	Profile findByNomImg(String image);
}
