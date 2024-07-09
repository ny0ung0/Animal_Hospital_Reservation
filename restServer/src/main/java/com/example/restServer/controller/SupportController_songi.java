package com.example.restServer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.restServer.entity.Support;
import com.example.restServer.repository.SupportRepository;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin("*")
public class SupportController_songi {
	
	@Autowired
	private SupportRepository supportRepo;
	
	
	@PostMapping("/qna")
	public ResponseEntity<?> qnaForm(HttpServletRequest request) {
		String title = request.getParameter("title");
        String content = request.getParameter("content");
        String category = request.getParameter("category");
        
        Support support = new Support();
        support.setCategory(category);
        support.setTitle(title);
        support.setContent(content);
        support.setIsConfirmed(false);
        //support.setMember(1);
        
        supportRepo.save(support);
        
        return ResponseEntity.ok("등록 완료");
	}
	
	
	
	
}
