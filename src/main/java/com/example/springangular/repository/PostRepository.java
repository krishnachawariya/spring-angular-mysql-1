package com.example.springangular.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springangular.model.Post;
import com.example.springangular.model.Subreddit;
import com.example.springangular.model.User;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
	
	 List<Post> findAllBySubreddit(Subreddit subreddit);

	 List<Post> findByUser(User user);

}
