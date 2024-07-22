package com.example.restServer.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.restServer.entity.Support;
import com.example.restServer.repository.MemberRepository;
import com.example.restServer.repository.SupportRepository;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/manager")
public class NoticeController_songi {
	
	@Autowired
	private SupportRepository supportRepo;
	
	//공지등록
	@PostMapping("/notice")
	public ResponseEntity<?> noticeForm(HttpServletRequest request) {
		String title = request.getParameter("title");
        String content = request.getParameter("content");
        String category = "공지사항";
        
        Support support = new Support();
        support.setCategory(category);
        support.setTitle(title);
        support.setContent(content);
        support.setIsConfirmed(false);
        //support.setMember(1);
        
        supportRepo.save(support);
        
        return ResponseEntity.ok("공지 등록 완료");
	}
		
	

	//공지사항 삭제
	@DeleteMapping("/notice/{id}")
	public ResponseEntity<?> deleteNotice(@PathVariable("id")Long id){
		supportRepo.deleteById(id);
		
		return ResponseEntity.ok("공지 삭제 성공");
	}
	
}
