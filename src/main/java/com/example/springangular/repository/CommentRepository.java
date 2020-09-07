package com.example.springangular.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springangular.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

}
