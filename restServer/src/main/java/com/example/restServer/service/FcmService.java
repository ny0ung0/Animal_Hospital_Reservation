package com.example.restServer.service;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.example.restServer.dto.FcmSendDto;

import jakarta.servlet.http.HttpServletRequest;

@Service
public interface FcmService {

	//int sendMessageTo(FcmSendDto fcmSendDto) throws IOException;

	int sendMessageTo(FcmSendDto fcmSendDto, String memberIdHeader) throws IOException;
}
