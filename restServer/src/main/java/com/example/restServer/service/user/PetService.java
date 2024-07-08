package com.example.restServer.service.user;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

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
	
    
}
