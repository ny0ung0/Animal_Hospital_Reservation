package com.example.restServer.controller.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.restServer.dto.MemVetDto;
import com.example.restServer.entity.Bookmark;
import com.example.restServer.repository.BookmarkRepository;
import com.example.restServer.service.user.VetListService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1")
public class VetListController_jia {

	@Autowired
	private VetListService vetListService;

	
	
	@GetMapping("/vet-list")
	public ResponseEntity<List<MemVetDto>> vetList(@RequestParam Map<String, String> address, HttpServletRequest request) {
		List<MemVetDto> memList = new ArrayList<>();
		
		if(!request.getHeader("MemberId").equals("null")) {
			Long userId = Long.parseLong(request.getHeader("MemberId"));
			
			address.forEach((key, value) -> {
//	            System.out.println("Key: " + key + ", Value: " + value);
	            String[] gus = value.split(",");
	            
	            for(int i = 0; i<gus.length; i++) {
	            	String gu = gus[i];
	            	List<MemVetDto> list = vetListService.getMemberVetList(key + "//"+gu+"%", userId);
	            	memList.addAll(list);
	            }
	        });
		}else {
			address.forEach((key, value) -> {
//	            System.out.println("Key: " + key + ", Value: " + value);
	            String[] gus = value.split(",");
	            
	            for(int i = 0; i<gus.length; i++) {
	            	String gu = gus[i];
	            	List<MemVetDto> list = vetListService.getMemberVetList(key + "//"+gu+"%", 0L);
	            	memList.addAll(list);
	            }
	        });
		}
		
		return ResponseEntity.ok(memList);
	}
	
	@GetMapping("/near-vet-list")
	public ResponseEntity<List<MemVetDto>> nearVetList(@RequestParam Map<String, String> address, HttpServletRequest request) {
		List<MemVetDto> memList = new ArrayList<>();
	
	
		if(!request.getHeader("MemberId").equals("null")) {
			Long userId = Long.parseLong(request.getHeader("MemberId"));
			
			address.forEach((key, value) -> {
            	List<MemVetDto> list = vetListService.getMemberVetList(value+"%", userId);
            	memList.addAll(list);
	        });
		}else {
			address.forEach((key, value) -> {
            	List<MemVetDto> list = vetListService.getMemberVetList(value+"%", 0L);
            	memList.addAll(list);
	        });
		}
		
		return ResponseEntity.ok(memList);
	}
	
	@PostMapping("/user/bookmark/{isBookmarked}/{hosId}")
	public ResponseEntity<String> isBookmarekd(@PathVariable("isBookmarked") Boolean isBookmarked, @PathVariable("hosId") Long hosId, HttpServletRequest request){
		Long userId = Long.parseLong(request.getHeader("MemberId"));
        vetListService.isBookmarked(hosId, userId, isBookmarked);
       
		return ResponseEntity.ok("");
		
	}
}
