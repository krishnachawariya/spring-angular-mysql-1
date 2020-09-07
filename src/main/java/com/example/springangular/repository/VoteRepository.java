package com.example.springangular.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springangular.model.Vote;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long>{

}
