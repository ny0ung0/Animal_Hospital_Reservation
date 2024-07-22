package com.example.restServer.service;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.restServer.entity.Reservation;
import com.example.restServer.repository.ReservationRepository;
import com.example.restServer.service.user.ReservationService;

@SpringBootTest
public class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;
    @Autowired
    private ReservationRepository reservationRepo;

    @Test
    public void testConcurrentReservations() throws InterruptedException, ParseException {
        int threadCount = 10; // 동시에 실행할 스레드의 개수
        CountDownLatch latch = new CountDownLatch(threadCount); // 스레드들이 완료될 때까지 기다리기 위한 래치
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount); // 고정된 스레드 풀 생성
        Long userId = 8L;

        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> { // 스레드 풀에 새로운 작업 제출
                try {
                    Map<String, String> formData = new HashMap<>();
                    formData.put("hospitalId", "1");
                    formData.put("doctorId", "1");
                    formData.put("type", "건강검진");
                    formData.put("memo", "정기검진입니다");
                    formData.put("pet", "1");
                    formData.put("date", "2024-07-29");
                    formData.put("time", "10:00");
                    formData.put("point", "0");
                    formData.put("coupon", "쿠폰사용 안함");
                    String result = reservationService.makeReservation(formData, userId);
                    
                } catch (ParseException e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown(); // 스레드가 완료되면 래치 카운트 감소
                }
            });
        }

        latch.await(); // 모든 스레드가 작업을 마칠 때까지 기다림
        executorService.shutdown(); // 스레드 풀 종료

        // 추가 검증: 예약이 제대로 생성되었는지 확인
        List<Reservation> reservations = reservationRepo.findAll();
        assertEquals(1, reservations.size(), "중복 예약 발생이슈");
     
    }
}