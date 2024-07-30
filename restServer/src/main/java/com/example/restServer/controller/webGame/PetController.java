package com.example.restServer.controller.webGame;

import java.time.LocalDateTime;
import java.util.Date;
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
import com.example.restServer.entity.Point;
import com.example.restServer.repository.MemberRepository;
import com.example.restServer.repository.PetGameRepository;
import com.example.restServer.repository.PointRepository;

@RestController
@RequestMapping("/api/petgame")
public class PetController {
	
	@Autowired
	PointRepository pointRepository;
	
	@Autowired
	private PetGameRepository petGameRepository;

	@Autowired
	private MemberRepository memberRepository;

	@GetMapping("/{userId}")
	public PetGame getPetGameData(@PathVariable(name = "userId") Long userId) {
		Optional<Member> user = memberRepository.findById(userId);
		if (user.isPresent()) {
			return petGameRepository.findByUserAndIsFullyGrownFalse(user.get()).orElseGet(() -> {
				PetGame newPetGame = PetGame.builder().user(user.get()).feedCount(1).playCount(0).isFullyGrown(false)
						.currentExperience(0).level(1).maxExperience(100).lastGivenTimeMorning(null)
						.lastGivenTimeAfternoon(null).lastGivenTimeEvening(null).build();
				return petGameRepository.save(newPetGame);
			});
		} else {
			throw new RuntimeException("User not found");
		}
	}

	@PostMapping("/{userId}")
	public PetGame savePetGameData(@PathVariable(name = "userId") Long userId, @RequestBody PetGame data) {
		Optional<Member> user = memberRepository.findById(userId);
		if (user.isPresent()) {
			Optional<PetGame> pet = petGameRepository.findByUserAndIsFullyGrownFalse(user.get());
			if (pet.isPresent()) {
				PetGame existingPet = pet.get();
				if (data.getLevel() >= 3 && data.getCurrentExperience() >= 200) {
					existingPet.setIsFullyGrown(true);
					petGameRepository.save(existingPet);
					Point point = new Point();
					point.setUser(user.get());
					point.setPointsAccumulated(500);
					point.setComment("펫키우기 완료 보상");
					point.setAccumulationDate(new Date());
					pointRepository.save(point);
					
					PetGame newPetGame = PetGame.builder().user(user.get()).feedCount(1).playCount(0)
							.isFullyGrown(false).currentExperience(0).level(1).maxExperience(100)
							.lastGivenTimeMorning(null).lastGivenTimeAfternoon(null).lastGivenTimeEvening(null).build();
					return petGameRepository.save(newPetGame);
				} else {
					existingPet.setFeedCount(data.getFeedCount());
					existingPet.setPlayCount(data.getPlayCount());
					existingPet.setCurrentExperience(data.getCurrentExperience());
					existingPet.setLevel(data.getLevel());
					existingPet.setMaxExperience(data.getMaxExperience());
					existingPet.setLastGivenTimeMorning(data.getLastGivenTimeMorning());
					existingPet.setLastGivenTimeAfternoon(data.getLastGivenTimeAfternoon());
					existingPet.setLastGivenTimeEvening(data.getLastGivenTimeEvening());
					existingPet.setLastGivenTimeDay(data.getLastGivenTimeDay());
					return petGameRepository.save(existingPet);
				}
			} else {
				throw new RuntimeException("PetGame not found");
			}
		} else {
			throw new RuntimeException("User not found");
		}
	}
}
