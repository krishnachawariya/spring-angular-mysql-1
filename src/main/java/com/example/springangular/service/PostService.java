	package com.example.springangular.service;

import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.springangular.dto.PostRequest;
import com.example.springangular.dto.PostResponse;
import com.example.springangular.exceptions.PostNotFoundException;
import com.example.springangular.exceptions.SubredditNotFoundException;
import com.example.springangular.model.Post;
import com.example.springangular.model.Subreddit;
import com.example.springangular.model.User;
import com.example.springangular.repository.PostRepository;
import com.example.springangular.repository.SubredditRepository;
import com.example.springangular.repository.UserRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class PostService {
	
	private final PostRepository postRepository;
    private final SubredditRepository subredditRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    
    public Post save(PostRequest postRequest) {
        Subreddit subreddit = subredditRepository.findByName(postRequest.getSubredditName())
                				.orElseThrow(() -> new SubredditNotFoundException(postRequest.getSubredditName()));
        return postRepository.save(mapPostReqDto(postRequest, subreddit, authService.getCurrentUser()));
    }
    
    @Transactional(readOnly = true)
    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id.toString()));
        return this.mapToDto(post);
    }
    
    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(toList());
    }
    
    @Transactional(readOnly = true)
    public List<PostResponse> getPostsBySubreddit(Long subredditId) {
        Subreddit subreddit = subredditRepository.findById(subredditId)
                .orElseThrow(() -> new SubredditNotFoundException(subredditId.toString()));
        List<Post> posts = postRepository.findAllBySubreddit(subreddit);
        return posts.stream().map(this::mapToDto).collect(toList());
    }
    
    @Transactional(readOnly = true)
    public List<PostResponse> getPostsByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return postRepository.findByUser(user)
                .stream()
                .map(this::mapToDto)
                .collect(toList());
    }

	private PostResponse mapToDto(Post post) {
		
		return PostResponse.builder()
				.id(post.getPostId())
				.postName(post.getPostName())
				.description(post.getDescription())
				.url(post.getUrl())
				.subredditName(post.getSubreddit().getName())
				.userName(post.getUser().getUsername())
				.build();
	}

	private Post mapPostReqDto(PostRequest postRequest, Subreddit subreddit, User currentUser) {
		
		return Post.builder()
				.postId(postRequest.getPostId())
				.postName(postRequest.getPostName())
				.user(currentUser)
				.description(postRequest.getDescription())
				.subreddit(subreddit)
				.url(postRequest.getUrl())
				.createdDate(java.time.Instant.now())
				.build();
	}

}
