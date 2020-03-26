package com.douzone.jblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.douzone.jblog.repository.UserRepository;
import com.douzone.jblog.vo.UserVo;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;

	public boolean insert(UserVo userVo) {
		int count = userRepository.insert(userVo);
		return count == 1;
		
	}
	
	public UserVo getUser(UserVo userVo) {
		return userRepository.findUser(userVo);
	}

	public UserVo getUserById(String id) {
		return userRepository.getUserById(id);
	}
 
}
