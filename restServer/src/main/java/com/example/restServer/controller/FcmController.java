package com.example.restServer.controller;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.restServer.dto.FcmSendDto;
import com.example.restServer.service.FcmService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/fcm")
public class FcmController {

	private final FcmService fcmService;

    public FcmController(FcmService fcmService) {
        this.fcmService = fcmService;
    }

    @PostMapping("/send")
    public ResponseEntity<Object> pushMessage(@RequestBody @Validated FcmSendDto fcmSendDto, HttpServletRequest request) throws IOException {
        log.debug("[+] 푸시 메시지를 전송합니다. ");
        String memberIdHeader = request.getHeader("MemberId");
        System.out.println(memberIdHeader);
        int result = fcmService.sendMessageTo(fcmSendDto, memberIdHeader);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}