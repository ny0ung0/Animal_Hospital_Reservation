package com.example.restServer.service.user;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.restServer.entity.Coupon;
import com.example.restServer.entity.Doctor;
import com.example.restServer.entity.Member;
import com.example.restServer.entity.Pet;
import com.example.restServer.entity.Point;
import com.example.restServer.entity.Reservation;
import com.example.restServer.entity.UnavailableTime;
import com.example.restServer.repository.CouponRepository;
import com.example.restServer.repository.DoctorRepository;
import com.example.restServer.repository.MemberRepository;
import com.example.restServer.repository.PetRepository;
import com.example.restServer.repository.PointRepository;
import com.example.restServer.repository.ReservationRepository;
import com.example.restServer.repository.UnavailableTimeRepository;
import com.example.restServer.util.DateTimeUtil;

@Service
public class ReservationService {

    @Autowired 
    private MemberRepository memRepo;
    @Autowired 
    private ReservationRepository reservRepo;
    @Autowired 
    private PetRepository petRepo;
    @Autowired 
    private CouponRepository couponRepo;
    @Autowired 
    private DoctorRepository doctorRepo;
    @Autowired 
    private UnavailableTimeRepository unavailableTimeRepo;
    @Autowired 
    private PointRepository pointRepo;

    public Map<String, Object> getPetInfo(Long userId, Long hospitalId) {
        Member user = memRepo.findById(userId).get();
        List<Pet> petList = petRepo.findAllByMemberId(userId);
        List<Coupon> couponList = couponRepo.findCouponByUserAndHospital(userId, hospitalId);
        Integer point = pointRepo.findByUserIdRemainingPoints(userId);
        List<Integer> pointList = new ArrayList<>();
        pointList.add(point);
        Map<String, Object> map = new HashMap<>();
        map.put("user", user);
        map.put("petList", petList);
        map.put("couponList", couponList);
        map.put("pointList", pointList);
        return map;
    }

    public Map<String, Object> getVetAvailInfo(Long hospitalId) {
        Map<String, Object> map = new HashMap<>();
        List<Doctor> docList = doctorRepo.findAllByHospitalId(hospitalId);
        for (Doctor doctor : docList) {
            Long docId = doctor.getId();
            String docName = doctor.getName();
            List<UnavailableTime> unavailList = unavailableTimeRepo.findAllByDoctorId(docId);
            map.put(docId + "//" + docName, unavailList);
        }
        return map;
    }

    public Map<String, Object> getVetInfo(Long hospitalId) {
        Map<String, Object> map = new HashMap<>();
        Member vet = memRepo.findById(hospitalId).get();
        map.put(vet.getHospitalName() + "", vet);
        return map;
    }

    public String makeReservation(Map<String, String> formData, Long userId) {
        Date now = new Date();
        System.out.println(formData);
        Reservation reservation = new Reservation();
        reservation.setUser(memRepo.findById(userId).get());
        reservation.setHospital(memRepo.findById(Long.parseLong(formData.get("hospitalId"))).get());
        reservation.setMemo(formData.get("memo"));
        reservation.setDoctor(doctorRepo.findById(Long.parseLong(formData.get("doctorId"))).get());
        reservation.setStatus("대기");
        reservation.setType(formData.get("type"));
        reservation.setPet(petRepo.findById(Long.parseLong(formData.get("pet"))).get());

        String dateTimeStr = formData.get("date") + " " + formData.get("time");
        LocalDateTime dateTime = null;

        try {
            dateTime = DateTimeUtil.parseDateTime(dateTimeStr);
            reservation.setReservationDatetime(dateTime);
        } catch (DateTimeParseException e) {
            e.printStackTrace();
        }

        String point = formData.get("point");

        if (point != null && !point.equals("") && !point.equals("0")) {
            reservation.setPointsUsed(Integer.parseInt(point));
            Point usedPoint = new Point();
            usedPoint.setUser(memRepo.findById(userId).get());
            usedPoint.setUseDate(now);
            usedPoint.setPointsUsed(Integer.parseInt(point));
            usedPoint.setComment("예약포인트사용");
            pointRepo.save(usedPoint);
        }

        if (!formData.get("coupon").equals("쿠폰사용 안함")) {
            Coupon coupon = couponRepo.findById(Long.parseLong(formData.get("coupon"))).get();
            coupon.setIsUsed(true);
            coupon.setUseDate(now);
            reservation.setCoupon(coupon);
            couponRepo.save(coupon);
        }
        reservRepo.save(reservation);
        UnavailableTime unavailTime = new UnavailableTime();
        unavailTime.setDoctor(doctorRepo.findById(Long.parseLong(formData.get("doctorId"))).get());
        unavailTime.setHospital(memRepo.findById(Long.parseLong(formData.get("hospitalId"))).get());
        unavailTime.setComment("진료예약");

        Date date = java.sql.Timestamp.valueOf(dateTime);
        LocalTime localTime = dateTime.toLocalTime();
        unavailTime.setDate(date);
        unavailTime.setTime(localTime);
        unavailableTimeRepo.save(unavailTime);

        return "";
    }

    public Reservation findReservInfo(Long reservId) {
        System.out.println(reservRepo.findById(reservId).get());
        return reservRepo.findById(reservId).get();
    }

    public String editReservation(Map<String, String> formData, Long userId) {
        Date now = new Date();
        Long reservId = Long.parseLong(formData.get("reservId"));

        Reservation reservation = reservRepo.findById(reservId).get();

        LocalDateTime oriDateTime = reservation.getReservationDatetime();
        UnavailableTime oriUnavailTime = unavailableTimeRepo.findTimeByDoctorIdNDatetime(
                reservation.getDoctor().getId(),
                DateTimeUtil.formatDate(oriDateTime),
                DateTimeUtil.formatTime(oriDateTime.toLocalTime()));
        unavailableTimeRepo.delete(oriUnavailTime);

        reservation.setUser(reservation.getUser());
        reservation.setHospital(reservation.getHospital());
        reservation.setDoctor(doctorRepo.findById(Long.parseLong(formData.get("doctorId"))).get());
        reservation.setStatus("대기");
        reservation.setType(formData.get("type"));
        reservation.setPet(petRepo.findById(Long.parseLong(formData.get("pet"))).get());

        if (!formData.get("memo").equals("")) {
            reservation.setMemo(formData.get("memo"));
        }

        String dateTimeStr = formData.get("date") + " " + formData.get("time");
        LocalDateTime dateTime = null;

        try {
            dateTime = DateTimeUtil.parseDateTime(dateTimeStr);
            reservation.setReservationDatetime(dateTime);
        } catch (DateTimeParseException e) {
            e.printStackTrace();
        }

        if (formData.get("point").equals("") || formData.get("point").equals("0")) {
            if (reservation.getPointsUsed() != null) {
                Point newPoint = new Point();
                newPoint.setUser(reservation.getUser());
                newPoint.setPointsAccumulated(reservation.getPointsUsed());
                newPoint.setAccumulationDate(now);
                newPoint.setComment("예약취소포인트반환");
                pointRepo.save(newPoint);
            }
            reservation.setPointsUsed(null);
        } else {
            if (reservation.getPointsUsed() != null) {
                Point newPoint = new Point();
                newPoint.setUser(reservation.getUser());
                newPoint.setPointsAccumulated(reservation.getPointsUsed());
                newPoint.setAccumulationDate(now);
                newPoint.setComment("예약취소포인트반환");
                pointRepo.save(newPoint);
            }

            reservation.setPointsUsed(Integer.parseInt(formData.get("point")));
            Point usedPoint = new Point();
            usedPoint.setUser(reservation.getUser());
            usedPoint.setUseDate(now);
            usedPoint.setPointsUsed(Integer.parseInt(formData.get("point")));
            usedPoint.setComment("예약포인트사용");
            pointRepo.save(usedPoint);
        }

        if (formData.get("coupon").equals("쿠폰사용 안함")) {
            if (reservation.getCoupon() != null) {
                Coupon cp = reservation.getCoupon();
                cp.setIsUsed(false);
                cp.setUseDate(null);
                couponRepo.save(cp);
            }
            reservation.setCoupon(null);
        } else {
            if (reservation.getCoupon() != null) {
                Coupon cp = reservation.getCoupon();
                cp.setIsUsed(false);
                cp.setUseDate(null);
                couponRepo.save(cp);
            }
            Coupon coupon = couponRepo.findById(Long.parseLong(formData.get("coupon"))).get();
            coupon.setIsUsed(true);
            coupon.setUseDate(now);
            reservation.setCoupon(coupon);
            couponRepo.save(coupon);
        }

        UnavailableTime unavailTime = new UnavailableTime();
        unavailTime.setDoctor(doctorRepo.findById(Long.parseLong(formData.get("doctorId"))).get());
        unavailTime.setHospital(reservation.getHospital());
        unavailTime.setComment("진료예약");
        Date date = java.sql.Timestamp.valueOf(dateTime);
        LocalTime localTime = dateTime.toLocalTime();
        unavailTime.setDate(date);
        unavailTime.setTime(localTime);
        unavailableTimeRepo.save(unavailTime);

        reservRepo.save(reservation);

        return "";
    }
}
