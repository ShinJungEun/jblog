package com.douzone.jblog.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.douzone.jblog.repository.PostRepository;
import com.douzone.jblog.vo.PostVo;

@Service
public class PostService {

	@Autowired
	PostRepository postRepository;
	
	public int postCount(Long categoryNo) {
		
		return postRepository.postCount(categoryNo);
	}

	public boolean insert(PostVo postVo) {
		int count = postRepository.insert(postVo);
		return count == 1;		
		
	}
	
	public List<PostVo> postList(Long categoryNo) {
		return postRepository.postList(categoryNo);
	}
	
	public PostVo PostView(Long no) {
		return postRepository.PostView(no);
	}
	
	public Long findPostNo(Long categoryNo) {
		return postRepository.findPostNo(categoryNo);
	}
}
