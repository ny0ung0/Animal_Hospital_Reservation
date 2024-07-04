package com.example.veiwServer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/hospital")
public class HospitalController_jisun {
	
	@RequestMapping("/reservList")
	public String reservList() {
		return "/hospital/reserv_list";
	}

}
