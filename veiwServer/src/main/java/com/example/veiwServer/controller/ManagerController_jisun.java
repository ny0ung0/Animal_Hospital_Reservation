package com.example.veiwServer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/manager")
public class ManagerController_jisun {
	
	@RequestMapping("/vetPetmit")
	public String vetPermit() {
		return "/manager/vet_permit";
	}
	
}
