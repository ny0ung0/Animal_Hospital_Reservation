package com.example.restServer.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.restServer.dto.UserReservationDto;
import com.example.restServer.entity.Pet;
import com.example.restServer.entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

	@Query(value = "SELECT * FROM reservation WHERE hospital_id = :memberId AND status = :status ORDER BY reservation_datetime asc", nativeQuery = true)
	Page<Reservation> findAllByHospitalIdAndStatus(Pageable pageable, @Param("memberId")Long memberId, @Param("status")String status);
	
	@Query(value = "SELECT AVG(RATING) FROM reservation WHERE hospital_id = :hospitalId AND !ISNULL(RATING);", nativeQuery = true)
	Long findAvgReview(@Param("hospitalId") Long hospitalId);

	@Query(value = "SELECT * FROM reservation WHERE doctor_id = :doctorId AND STATUS != '취소'", nativeQuery = true)
	List<Reservation> findAllByDoctorId(@Param("doctorId")Long doctorId);
	
	@Query(value = "SELECT * FROM reservation WHERE hospital_id= :hospitalId AND !isnull(review) ORDER BY updated_at DESC", nativeQuery = true)
	List<Reservation> findReservWithReview (@Param("hospitalId")Long hospitalId);
	
	@Query(value = "SELECT re.*,h.hospital_name,\r\n"
			+ "p.name AS pet_name,p.type AS pet_type,p.photo,p.gender,p.birthdate,\r\n"
			+ "u.nickname,u.phone,\r\n"
			+ "c.name AS coupon_name,\r\n"
			+ "d.name AS doctor_name\r\n"
			+ "from reservation re\r\n"
			+ "LEFT JOIN member h\r\n"
			+ "ON re.hospital_id = h.id\r\n"
			+ "LEFT JOIN pet p\r\n"
			+ "ON re.pet_id = p.id\r\n"
			+ "LEFT JOIN member u\r\n"
			+ "ON re.user_id = u.id\r\n"
			+ "LEFT JOIN coupon c\r\n"
			+ "ON re.coupon_id = c.id\r\n"
			+ "LEFT JOIN doctor d\r\n"
			+ "ON re.doctor_id = d.id\r\n"
			+ "WHERE re.user_id = :userId order by re.id desc;", nativeQuery = true )
	List<UserReservationDto> findReserListByUserId(@Param("userId")Long userId);

	List<Reservation> findByHospitalId(Long memberId);

	List<Reservation> findByDoctorIdAndReservationDatetime(Long doctorId, LocalDateTime reservationDatetime);
	
	@Query(value = "SELECT r.id, r.created_at, r.updated_at, r.memo, r.points_used, r.rating, r.reservation_datetime, r.review, r.status, r.type, r.coupon_id, r.doctor_id, r.hospital_id, r.pet_id, r.user_id\r\n"
			+ "FROM reservation r\r\n"
			+ "JOIN (\r\n"
			+ "    SELECT pet_id, MAX(reservation_datetime) AS max_reservation_datetime\r\n"
			+ "    FROM reservation\r\n"
			+ "    WHERE hospital_id =:hospitalId\r\n"
			+ "    GROUP BY pet_id\r\n"
			+ ") latest_reservations\r\n"
			+ "ON r.pet_id = latest_reservations.pet_id\r\n"
			+ "AND r.reservation_datetime = latest_reservations.max_reservation_datetime\r\n"
			+ "ORDER BY r.reservation_datetime DESC", nativeQuery = true)
	Page<Reservation> findByCustomerListLastDate(Pageable pageable, @Param("hospitalId")Long hospitalId);

	@Query(value = "SELECT * FROM reservation WHERE pet_id =:petId AND hospital_id =:hospitalId ORDER BY reservation_datetime desc", nativeQuery = true)
	Page<Reservation> findByPetAndHospitalId(Pageable pageable, @Param("petId")Long petId, @Param("hospitalId")Long hospitalId);
	
	
	
	@Query(value = "SELECT r.id, r.created_at, r.updated_at, r.memo, r.points_used, r.rating, r.reservation_datetime, r.review, r.status, r.type, r.coupon_id, r.doctor_id, r.hospital_id, r.pet_id, r.user_id\r\n"
			+ "FROM reservation r\r\n"
			+ "JOIN (\r\n"
			+ "    SELECT pet_id, MAX(reservation_datetime) AS max_reservation_datetime\r\n"
			+ "    FROM reservation\r\n"
			+ "    WHERE hospital_id =:hospitalId\r\n"
			+ "    GROUP BY pet_id\r\n"
			+ ") latest_reservations\r\n"
			+ "ON r.pet_id = latest_reservations.pet_id\r\n"
			+ "AND r.reservation_datetime = latest_reservations.max_reservation_datetime\r\n"
			+ "JOIN member m ON r.user_id = m.id\r\n"
			+ "JOIN pet p ON r.pet_id = p.id\r\n"
			+ "ORDER BY p.name ASC;", nativeQuery = true)
	Page<Reservation> findByCustomerListFilterByName(Pageable pageable, @Param("hospitalId")Long hospitalId);
	
	@Query(value = "SELECT r.id, r.created_at, r.updated_at, r.memo, r.points_used, r.rating, r.reservation_datetime, r.review, r.status, r.type, r.coupon_id, r.doctor_id, r.hospital_id, r.pet_id, r.user_id\r\n"
			+ "FROM reservation r\r\n"
			+ "JOIN (\r\n"
			+ "    SELECT pet_id, MAX(reservation_datetime) AS max_reservation_datetime\r\n"
			+ "    FROM reservation\r\n"
			+ "    WHERE hospital_id =:hospitalId\r\n"
			+ "    GROUP BY pet_id\r\n"
			+ ") latest_reservations\r\n"
			+ "ON r.pet_id = latest_reservations.pet_id\r\n"
			+ "AND r.reservation_datetime = latest_reservations.max_reservation_datetime\r\n"
			+ "JOIN member m ON r.user_id = m.id\r\n"
			+ "JOIN pet p ON r.pet_id = p.id\r\n"
			+ "WHERE m.name LIKE %:keyword% OR m.phone LIKE %:keyword% OR p.name LIKE %:keyword%\r\n"
			+ "ORDER BY p.name ASC", nativeQuery = true)
	Page<Reservation> findByCustomerListFilterByNameKeyword(Pageable pageable, @Param("hospitalId")Long hospitalId, @Param("keyword") String keyword);
	
	@Query(value = "SELECT r.id, r.created_at, r.updated_at, r.memo, r.points_used, r.rating, r.reservation_datetime, r.review, r.status, r.type, r.coupon_id, r.doctor_id, r.hospital_id, r.pet_id, r.user_id\r\n"
			+ "FROM reservation r\r\n"
			+ "JOIN (\r\n"
			+ "    SELECT pet_id, MAX(reservation_datetime) AS max_reservation_datetime\r\n"
			+ "    FROM reservation\r\n"
			+ "    WHERE hospital_id =:hospitalId\r\n"
			+ "    GROUP BY pet_id\r\n"
			+ ") latest_reservations\r\n"
			+ "ON r.pet_id = latest_reservations.pet_id\r\n"
			+ "AND r.reservation_datetime = latest_reservations.max_reservation_datetime\r\n"
			+ "JOIN member m ON r.user_id = m.id\r\n"
			+ "ORDER BY m.name ASC", nativeQuery = true)
	Page<Reservation> findByCustomerListFilterByUserName(Pageable pageable, @Param("hospitalId")Long hospitalId);
	
	@Query(value = "SELECT r.id, r.created_at, r.updated_at, r.memo, r.points_used, r.rating, r.reservation_datetime, r.review, r.status, r.type, r.coupon_id, r.doctor_id, r.hospital_id, r.pet_id, r.user_id\r\n"
			+ "FROM reservation r\r\n"
			+ "JOIN (\r\n"
			+ "    SELECT pet_id, MAX(reservation_datetime) AS max_reservation_datetime\r\n"
			+ "    FROM reservation\r\n"
			+ "    WHERE hospital_id =:hospitalId\r\n"
			+ "    GROUP BY pet_id\r\n"
			+ ") latest_reservations\r\n"
			+ "ON r.pet_id = latest_reservations.pet_id\r\n"
			+ "AND r.reservation_datetime = latest_reservations.max_reservation_datetime\r\n"
			+ "JOIN member m ON r.user_id = m.id\r\n"
			+ "JOIN pet p ON r.pet_id = p.id\r\n"
			+ "WHERE m.name LIKE %:keyword% OR m.phone LIKE %:keyword% OR p.name LIKE %:keyword%\r\n"
			+ "ORDER BY m.name ASC", nativeQuery = true)
	Page<Reservation> findByCustomerListFilterByUserNameKeyword(Pageable pageable, @Param("hospitalId")Long hospitalId, @Param("keyword") String keyword);
	
	@Query(value = "SELECT r.id, r.created_at, r.updated_at, r.memo, r.points_used, r.rating, r.reservation_datetime, r.review, r.status, r.type, r.coupon_id, r.doctor_id, r.hospital_id, r.pet_id, r.user_id\r\n"
			+ "FROM reservation r\r\n"
			+ "JOIN (\r\n"
			+ "    SELECT pet_id, MAX(reservation_datetime) AS max_reservation_datetime\r\n"
			+ "    FROM reservation\r\n"
			+ "    WHERE hospital_id =:hospitalId\r\n"
			+ "    GROUP BY pet_id\r\n"
			+ ") latest_reservations\r\n"
			+ "ON r.pet_id = latest_reservations.pet_id\r\n"
			+ "AND r.reservation_datetime = latest_reservations.max_reservation_datetime\r\n"
			+ "JOIN member m ON r.user_id = m.id\r\n"
			+ "JOIN pet p ON r.pet_id = p.id\r\n"
			+ "WHERE m.name LIKE %:keyword% OR m.phone LIKE %:keyword% OR p.name LIKE %:keyword%\r\n"
			+ "ORDER BY r.reservation_datetime DESC", nativeQuery = true)
	Page<Reservation> findByCustomerListLastDateKeyword(Pageable pageable, @Param("hospitalId")Long hospitalId, @Param("keyword") String keyword);
	
	
}