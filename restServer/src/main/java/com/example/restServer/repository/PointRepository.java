package com.example.restServer.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.restServer.entity.Point;

public interface PointRepository extends JpaRepository<Point, Long> {
	
	@Query(value = "SELECT (SELECT SUM(points_accumulated) FROM point WHERE user_id =:userId)- IFNULL((SELECT SUM(points_used) FROM point WHERE user_id =:userId), 0) AS remaining_points", nativeQuery = true)
	public Integer findByUserIdRemainingPoints(@Param("userId")Long userId);

	public List<Point> findAllByUserId(Long id);
	
	public List<Point> findAllByUserId(Long id, Sort sort);
}
