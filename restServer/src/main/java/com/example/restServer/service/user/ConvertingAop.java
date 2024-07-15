package com.example.restServer.service.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.example.restServer.dto.MemVetDto;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class ConvertingAop {

	public Long getUserId (HttpServletRequest request) {
		Long userId = 0L;
		if(!request.getHeader("MemberId").equals("null")) {
			userId = Long.parseLong(request.getHeader("MemberId"));
		}
		return userId;
	}
	
	public static List<MemVetDto> convertingAddrs (Map<String, String> hosList, Long userId, VetListService vetListService, boolean isNearVet) {
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
