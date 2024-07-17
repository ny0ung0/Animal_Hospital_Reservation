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
	
		address.forEach((key, value) -> {
            String[] gus = value.split(",");
            for(int i = 0; i<gus.length; i++) {
            	String gu = gus[i];
            	List<MemVetDto> list = vetListService.getMemberVetList(key + "//"+gu+"%", this.getUserId(request));
            	memList.addAll(list);
            }
        });
		
		return ResponseEntity.ok(memList);
	}
	
	@GetMapping("/near-vet-list")
	public ResponseEntity<List<MemVetDto>> nearVetList(@RequestParam Map<String, String> hosList, HttpServletRequest request) {
		List<MemVetDto> memList = this.convertingAddrs(hosList, this.getUserId(request), vetListService, true);
		
		return ResponseEntity.ok(memList);
	}
	
	@GetMapping("/keyword-vet-list")
	public ResponseEntity<List<MemVetDto>> keywordVetList(@RequestParam Map<String, String> hosList, HttpServletRequest request) {
		List<MemVetDto> memList = this.convertingAddrs(hosList, this.getUserId(request), vetListService, false);
		
		return ResponseEntity.ok(memList);
	}
	
	@GetMapping("/user/vet-detail")
	public ResponseEntity<MemVetDto> vetDetail(@RequestParam("id") Long id, HttpServletRequest request) {
		Long userId = Long.parseLong(request.getHeader("MemberId"));
		MemVetDto mv = vetListService.getVetDetail(id,userId);
		
		return ResponseEntity.ok(mv);
	}
	
	@PostMapping("/user/bookmark/{isBookmarked}/{hosId}")
	public ResponseEntity<String> isBookmarekd(@PathVariable("isBookmarked") Boolean isBookmarked, @PathVariable("hosId") Long hosId, HttpServletRequest request){
		Long userId = Long.parseLong(request.getHeader("MemberId"));
        vetListService.isBookmarked(hosId, userId, isBookmarked);
       
		return ResponseEntity.ok("");
		
	}
	
	private Long getUserId (HttpServletRequest request) {
		Long userId = 0L;
		if(!request.getHeader("MemberId").equals("null")) {
			userId = Long.parseLong(request.getHeader("MemberId"));
		}
		return userId;
	}
	
	private List<MemVetDto> convertingAddrs (Map<String, String> hosList, Long userId, VetListService vetListService, boolean isNearVet) {
        List<MemVetDto> memList = new ArrayList<>();

        hosList.forEach((key, value) -> {
            if (isNearVet) {
                List<MemVetDto> list = vetListService.getMemberVetList(value + "%", userId);
                memList.addAll(list);
            } else {
                List<MemVetDto> list = vetListService.getMemberVet(key, value.split("//")[0] + "//" + value.split("//")[1] + "%", userId);
                memList.addAll(list);
            }
        });

        return memList;
    }
	
}
