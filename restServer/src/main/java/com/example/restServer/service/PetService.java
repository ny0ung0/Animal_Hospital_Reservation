package com.example.restServer.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.restServer.entity.Pet;
import com.example.restServer.repository.PetRepository;

@Service
public class PetService {
	@Autowired
    private PetRepository petRepository;
	
	@Value("${spring.servlet.multipart.location}")
    private String uploadPath;

	
    public void savePhoto(Long petId, MultipartFile file) throws IOException {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found"));

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        Path targetLocation = Paths.get(uploadPath).resolve(fileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        pet.setPhoto(fileName);
        petRepository.save(pet);
    }
}
