package com.example.springangular.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springangular.dto.SubredditDto;
import com.example.springangular.exceptions.SpringRedditException;
import com.example.springangular.model.Subreddit;
import com.example.springangular.repository.SubredditRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import static java.util.stream.Collectors.toList;


@Service
@AllArgsConstructor
@Slf4j
public class SubredditService {
	
	 private final SubredditRepository subredditRepository;
	// private final SubredditMapper subredditMapper;
	
	@Transactional
	public SubredditDto save(SubredditDto subredditDto) {
		Subreddit save = subredditRepository.save(mapSubredditDto(subredditDto));
		subredditDto.setId(save.getId());
		return subredditDto;
	}
	
	@Transactional(readOnly = true)
	public List<SubredditDto> getAll() {
		return subredditRepository.findAll()
							.stream()
							.map(this::mapToDto)
							.collect(Collectors.toList());
	}
	
	public SubredditDto getSubreddit(Long id) {
		  Subreddit subreddit = subredditRepository.findById(id)
	                .orElseThrow(() -> new SpringRedditException("No subreddit found with ID - " + id));
	        return this.mapToDto(subreddit);
		  
	}

	
	private SubredditDto mapToDto(Subreddit subreddit) {
		
		return SubredditDto.builder().name(subreddit.getName())
									 .id(subreddit.getId())
									 .description(subreddit.getDescription())
									 .numberOfPosts(subreddit.getPosts().size())
									 .build();
		
	}
	
	
	
	private Subreddit mapSubredditDto(SubredditDto subredditDto) {
		return Subreddit.builder()
				.id(subredditDto.getId())
				.name(subredditDto.getName())
				.description(subredditDto.getDescription())
				.build();
		
	}


	
	
	
	
}
