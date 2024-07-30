package com.example.restServer.controller.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.restServer.entity.Point;
import com.example.restServer.repository.PointRepository;

@RestController
@RequestMapping("/api/v1/user")
public class PointController {

	@Autowired
	PointRepository pointRepository;
	
	@GetMapping("/pointresult/{memberId}")
	public Integer getpoint(@PathVariable(value = "memberId") Long memberId) {
		
		return pointRepository.findByUserIdRemainingPoints(memberId);
	}
	
	@GetMapping("/pointList/{memberId}")
	public List<Point> getpointList(@PathVariable(value = "memberId") Long memberId){
		
		return pointRepository.findAllByUserId(memberId);
	}
	
}
