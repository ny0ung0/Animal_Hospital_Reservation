package com.example.restServer.service;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.example.restServer.dto.FcmSendDto;

@Service
public interface FcmService {

	int sendMessageTo(FcmSendDto fcmSendDto) throws IOException;
}
