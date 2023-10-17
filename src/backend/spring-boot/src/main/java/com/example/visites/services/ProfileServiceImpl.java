package com.example.visites.services;

import com.example.visites.configs.AppConstants;
import com.example.visites.dto.ProfileResponse;
import com.example.visites.exceptions.APIException;
import com.example.visites.exceptions.ResourceNotFoundException;
import com.example.visites.exceptions.UnsupportedFileTypeException;
import com.example.visites.manager.FileFilter;
import com.example.visites.models.Profile;
import com.example.visites.repositories.ProfileRepository;
import jakarta.xml.bind.DatatypeConverter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

	@Autowired
	private ProfileRepository profileRepository;

	@Autowired
	private FileFilter fileFilter;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public ResponseEntity<ProfileResponse> saveProfile(MultipartFile image) {
		String nouveauNom = copyImgToPath(image);
		Profile profile = new Profile();
		profile.setNomImg(nouveauNom);
		ProfileResponse saved = modelMapper.map(profileRepository.save(profile), ProfileResponse.class);
		return new ResponseEntity<>(saved, HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<byte[]> getProfileImage(Long id) {
		Profile article = profileRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Le profile","d'id",id));
		String cheminFichierImage = AppConstants.DEFAULT_PATH + "/" + article.getNomImg();

		try {
			Path cheminVersImages = Paths.get(cheminFichierImage);
			byte[] imageBytes = Files.readAllBytes(cheminVersImages);
			String contentType = fileFilter.determineContentType(cheminFichierImage);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.parseMediaType(contentType));
			headers.setContentLength(imageBytes.length);
			return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
		} catch (IOException e) {
			// Gestion des exceptions liées à la lecture du fichier
			throw new APIException("Echec . Veillez reessayer");
		} catch (UnsupportedFileTypeException e) {
			// Gestion des exceptions lorsque le type MIME ne peut pas être résolu
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
		}
	}

	@Override
	public ResponseEntity<ProfileResponse> setDefaultProfile(String newFilename) {
		try {
			Path sourceImage = Paths.get("pictures/default.png");

			String extension = "." + FilenameUtils.getExtension(sourceImage.toString());

			Path newImage = Files.createFile(Paths.get(AppConstants.DEFAULT_PATH, newFilename + extension));

			Files.copy(sourceImage, newImage, StandardCopyOption.REPLACE_EXISTING);

			Profile profile = new Profile();
			profile.setNomImg(newImage.getFileName().toString());
			ProfileResponse saved = modelMapper.map(profileRepository.save(profile), ProfileResponse.class);
			return new ResponseEntity<>(saved, HttpStatus.ACCEPTED);
		}catch (IOException e) {
			throw new APIException("Erreur lors de l'attibution de l'image par defaut");
		}
	}

	@Override
	public ResponseEntity<ProfileResponse> changeProfileImage(Long id, MultipartFile image) {
		try {
			Profile profile = profileRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Le profile ", "d'id : ", id));
			Path toDelete = Paths.get(AppConstants.DEFAULT_PATH, profile.getNomImg());
			if (Files.exists(toDelete))
				Files.delete(toDelete);
			String nouvNom = copyImgToPath(image);
			profile.setNomImg(nouvNom);
			ProfileResponse updated = modelMapper.map(profileRepository.save(profile), ProfileResponse.class);
			return new ResponseEntity<>(updated, HttpStatus.ACCEPTED);
		}catch (IOException e){
			throw new APIException("Echec de la modification de l'image");
		}
	}

	//	private String copyImgToPath(MultipartFile image){
//		File repertoire = new File(AppConstants.DEFAULT_PATH);
//		if (!repertoire.exists()) {
//			boolean repertoireCree = repertoire.mkdirs();
//			if (!repertoireCree) {
//				throw new APIException("Impossible de créer le répertoire 'images'");
//			}
//		}
//		if (image.isEmpty())
//			throw new APIException("Veillez selectionner une image");
//		String nomFichier = image.getOriginalFilename();
//		String nouveauNom = FilenameUtils.getBaseName(nomFichier) + "." + FilenameUtils.getExtension(nomFichier);
//		File fichierDuServeur = new File(repertoire, nouveauNom);
//		try {
//			FileUtils.writeByteArrayToFile(fichierDuServeur, image.getBytes());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return nouveauNom;
//	}
private String copyImgToPath(MultipartFile image) {
	File repertoire = new File(AppConstants.DEFAULT_PATH);
	if (!repertoire.exists()) {
		boolean repertoireCree = repertoire.mkdirs();
		if (!repertoireCree) {
			throw new APIException("Impossible de créer le répertoire 'images'");
		}
	}
	if (image.isEmpty())
		throw new APIException("Veillez selectionner une image");

	String nomFichier = image.getOriginalFilename();
	String extension = FilenameUtils.getExtension(nomFichier);

	// Générer un nom de fichier unique et sécurisé
	byte[] imageBytes = null;
	try {
		imageBytes = image.getBytes();
	} catch (IOException e) {
		e.printStackTrace();
	}
	byte[] hashBytes = null;
	try {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(imageBytes);
		hashBytes = md.digest();
	} catch (NoSuchAlgorithmException e) {
		e.printStackTrace();
	}
	String hashString = DatatypeConverter.printHexBinary(hashBytes);
	String nouveauNom = hashString + "." + extension;

	File fichierDuServeur = new File(repertoire, nouveauNom);
	try {
		FileUtils.writeByteArrayToFile(fichierDuServeur, image.getBytes());
	} catch (Exception e) {
		e.printStackTrace();
	}
	return nouveauNom;
	}

	@Override
	public void delete(Profile profile) {
		String fileName = profile.getNomImg();
		Path filePath = Paths.get(AppConstants.DEFAULT_PATH, fileName);
		try{
			Files.deleteIfExists(filePath);
			profileRepository.delete(profile);
			System.out.println("\n Le profile a ete supprime");
		} catch (IOException e) {
			throw new APIException("Echec de suppression de la photo de profile");
		}
	}

}
