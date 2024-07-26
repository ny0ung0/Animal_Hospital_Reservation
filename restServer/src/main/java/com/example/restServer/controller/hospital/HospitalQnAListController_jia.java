package com.example.restServer.controller.hospital;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.restServer.entity.Support;
import com.example.restServer.repository.SupportRepository;

import jakarta.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/api/v1/hospital")
public class HospitalQnAListController_jia {

	@Autowired
	private SupportRepository supportRepo;
	
	@GetMapping("/qna")
	public ResponseEntity<List<Support>> getMyQnAList(HttpServletRequest request) {
		Long userId = Long.parseLong(request.getHeader("MemberId"));
		return ResponseEntity.ok(supportRepo.findAllByMemberId(userId));
	}
}
