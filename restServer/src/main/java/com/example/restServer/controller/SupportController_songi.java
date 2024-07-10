package com.example.restServer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.restServer.entity.Support;
import com.example.restServer.repository.SupportRepository;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin("*")
public class SupportController_songi {
	
	@Autowired
	private SupportRepository supportRepo;
	
	//문의등록
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
	
	
	//문의 상세페이지
	@GetMapping("/qna/{id}")
	public ResponseEntity<?> qnaDetail(@PathVariable("id")Long id) {
		
		Support qna = supportRepo.findById(id).get();
		System.out.println("qna 상세정보 출력 : " + qna);
		return ResponseEntity.ok(qna);
	
		
	}
	
	
	//문의 수정
	@PutMapping("/qna/{id}")
	public ResponseEntity<?> qnaEdit(@PathVariable("id")Long id, HttpServletRequest request) {
		String title = request.getParameter("title");
        String content = request.getParameter("content");
        
		Support qna = supportRepo.findById(id).get();
		
		qna.setTitle(title);
		qna.setContent(content);
		supportRepo.save(qna);
		
		return ResponseEntity.ok("문의 수정 성공");
	
		
	}
	
	//문의 삭제
	@DeleteMapping("/qna/{id}")
	public ResponseEntity<?> qnaDelete(@PathVariable("id")Long id){
		supportRepo.deleteById(id);
		return ResponseEntity.ok("문의 삭제 성공");
	}
	
	
	
	//공지사항 목록 불러오기
	@GetMapping("/notice")
	public ResponseEntity<?> getNoticeList(){
		
		List<Support> noticeList = supportRepo.findAllByCategory("공지사항");
		System.out.println("공지목록 출력 : " + noticeList);
		
		return ResponseEntity.ok(noticeList);
	}
	
	
	
	
	
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
	
	
	
	
	//공지사항 상세페이지 불러오기
	@GetMapping("/notice/{id}")
	public ResponseEntity<?> getNotice(@PathVariable("id")Long id){
		Support notice = supportRepo.findById(id).get();
		System.out.println("공지 상세 출력 : " + notice);
		
		return ResponseEntity.ok(notice);
	}
	
	
	
	
	//공지사항 삭제
	@DeleteMapping("/notice/{id}")
	public ResponseEntity<?> deleteNotice(@PathVariable("id")Long id){
		supportRepo.deleteById(id);
		
		return ResponseEntity.ok("공지 삭제 성공");
	}
	
	
}
