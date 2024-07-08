package com.example.restServer.controller.user;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.restServer.dto.MemVetDto;
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
		
		if(request.getHeader("username") != null ) {
			Long userId = Long.parseLong(request.getHeader("username"));
			
			address.forEach((key, value) -> {
//	            System.out.println("Key: " + key + ", Value: " + value);
	            String[] gus = value.split(",");
	            
	            for(int i = 0; i<gus.length; i++) {
	            	String gu = gus[i];
	            	List<MemVetDto> list = vetListService.getMemberVetList(key + "//"+gu+"%", userId);
	            	memList.addAll(list);
	            }
	            System.out.println(memList);
	            System.out.println(memList.size());
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
	            System.out.println(memList);
	            System.out.println(memList.size());
	        });
		}
	
		
		
		return ResponseEntity.ok(memList);
	}
	
	@GetMapping("/near-vet-list")
	public ResponseEntity<List<MemVetDto>> nearVetList(@RequestParam Map<String, String> address, HttpServletRequest request) {
		List<MemVetDto> memList = new ArrayList<>();
		System.out.println(address);
//		System.out.println("xxxx");
	
		if(request.getHeader("username") != null ) {
			Long userId = Long.parseLong(request.getHeader("username"));
			
			address.forEach((key, value) -> {
            	List<MemVetDto> list = vetListService.getMemberVetList(value+"%", userId);
            	memList.addAll(list);
	            System.out.println(memList);
	            System.out.println(memList.size());
	        });
		}else {
			address.forEach((key, value) -> {
            	List<MemVetDto> list = vetListService.getMemberVetList(value+"%", 0L);
            	memList.addAll(list);
	            System.out.println(memList);
	            System.out.println(memList.size());
	        });
		}
	
		
		
		return ResponseEntity.ok(memList);
	}
}
