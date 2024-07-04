package com.example.restServer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.restServer.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}