package com.example.restServer.controller.user;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.restServer.dto.PetDto;
import com.example.restServer.entity.Pet;
import com.example.restServer.repository.PetRepository;

@RestController
@RequestMapping("/user/mypage")
public class MyPetController_songi {
	
	@Autowired
	private PetRepository petRepository;
	
	@Value("${spring.servlet.multipart.location}")
    private String uploadPath;
	
	
	@PostMapping("/myPet")
	public ResponseEntity<?> myPetRegist(PetDto petDto, @RequestParam("photo") MultipartFile file) {
	    try {
	        Pet pet = new Pet();
	        pet.setBirthdate(petDto.getBirthdate());
	        pet.setGender(petDto.getGender());
	        pet.setHealthIssues(petDto.getHealthIssues());
	        pet.setIsNeutered(petDto.getIsNeutered());
	        pet.setName(petDto.getName());
	        pet.setType(petDto.getType());
	        pet.setWeight(petDto.getWeight());

	        
	        String originalFilename = file.getOriginalFilename();
	        String extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
	        String filename = UUID.randomUUID().toString() + extension;

	        // 실제 파일 저장
	        Path destinationFile = Paths.get(uploadPath).resolve(filename).normalize().toAbsolutePath();
	        Files.copy(file.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);
	        
	        pet.setPhoto(filename);
	        
	        
	        
	        // Pet 객체를 데이터베이스에 저장
	        petRepository.save(pet);

	        return ResponseEntity.ok("success");

	   
	    } catch (Exception e) {
	        // 기타 예외 처리
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to register pet: " + e.getMessage());
	    }
	}
	
	
	@GetMapping("/myPet")
	public void myPetList() {
		
	}
	
	
	@PutMapping("/myPet")
	public void myPetEdit() {
		
	}
	
	
	@DeleteMapping("/myPet")
	public void myPetDelte() {
		
	}
	
}
