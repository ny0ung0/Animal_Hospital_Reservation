package com.example.restServer.controller.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.restServer.dto.BookmarkDto;
import com.example.restServer.repository.BookmarkRepository;

@RestController
@RequestMapping("/api/v1/user")
public class FavVetController_jun {

	@Autowired
	BookmarkRepository bookmarkRepository;
	
	@GetMapping("/fav_vet/{memberId}")
	public ResponseEntity<?> fav_vet(@PathVariable("memberId")Long memberId ) {
		System.out.println("즐겨찾기 컨트롤러 들어왔어요~");
		//System.out.println(memberId);
		List<BookmarkDto> list =bookmarkRepository.findAllByUserId(memberId);
		
		return ResponseEntity.ok().body(list);
	}
	
	
	@DeleteMapping("/fav_vet/{id}")
	public ResponseEntity<?> fav_vet_delete(@PathVariable("id")Long id){
		System.out.println("즐겨찾기 아이디 : " + id);
		bookmarkRepository.deleteById(id);
		return ResponseEntity.ok().body("성공");
	}
 }
