package com.example.visites.services;

import com.example.visites.dto.ProfileResponse;
import com.example.visites.models.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface ProfileService {

	ResponseEntity<ProfileResponse> saveProfile(MultipartFile image);

	ResponseEntity<byte[]> getProfileImage(Long id);

	ResponseEntity<ProfileResponse> setDefaultProfile(String id);

	ResponseEntity<ProfileResponse> changeProfileImage(Long id, MultipartFile image);

	void delete(Profile profile);
}
