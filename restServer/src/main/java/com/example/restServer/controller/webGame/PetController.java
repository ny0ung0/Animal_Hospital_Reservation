package com.example.restServer.controller.webGame;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.restServer.entity.Member;
import com.example.restServer.entity.PetGame;
import com.example.restServer.repository.MemberRepository;
import com.example.restServer.repository.PetGameRepository;

@RestController
@RequestMapping("/api/petgame")
public class PetController {

	@Autowired
	  private PetGameRepository petGameRepository;

	  @Autowired
	  private MemberRepository memberRepository;

	  @GetMapping("/{userId}")
	  public PetGame getPetGameData(@PathVariable (name="userId") Long userId) {
	    Optional<Member> user = memberRepository.findById(userId);
	    if (user.isPresent()) {
	      return petGameRepository.findByUser(user.get()).orElseGet(() -> {
	        PetGame newPetGame = PetGame.builder()
	          .user(user.get())
	          .feedCount(1)
	          .playCount(0)
	          .currentExperience(0)
	          .level(1)
	          .maxExperience(100)
	          .build();
	        return petGameRepository.save(newPetGame);
	      });
	    } else {
	      throw new RuntimeException("User not found");
	    }
	  }

	  @PostMapping("/{userId}")
	  public PetGame savePetGameData(@PathVariable (name="userId") Long userId, @RequestBody PetGame data) {
		  System.out.println("와");
		  System.out.println(userId);
	    Optional<Member> user = memberRepository.findById(userId);
	    System.out.println("safd");
	    if (user.isPresent()) {
	    Optional<PetGame> pet = petGameRepository.findByUserId(user.get().getId());
	    System.out.println("petdata:" + pet);
	      data.setUser(user.get());
	      data.setId(pet.get().getId());
	      return petGameRepository.save(data);
	    } else {
	      throw new RuntimeException("User not found");
	    }
	  }
	}
