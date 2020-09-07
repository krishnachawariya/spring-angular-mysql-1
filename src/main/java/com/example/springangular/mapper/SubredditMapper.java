package com.example.springangular.mapper;

import java.util.List;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.springangular.dto.SubredditDto;
import com.example.springangular.model.Post;
import com.example.springangular.model.Subreddit;

@Mapper(componentModel = "spring")
public interface SubredditMapper {

	 	@Mapping(target = "numberOfPosts", expression = "java(mapPosts(subreddit.getPosts()))")
	    SubredditDto mapSubredditToDto(Subreddit subreddit);

	    default Integer mapPosts(List<Post> numberOfPosts) {
	        return numberOfPosts.size();
	    }

	    @InheritInverseConfiguration
	    @Mapping(target = "posts", ignore = true)
	    Subreddit mapDtoToSubreddit(SubredditDto subredditDto);
	    
}
