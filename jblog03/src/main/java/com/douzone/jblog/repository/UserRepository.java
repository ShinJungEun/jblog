package com.douzone.jblog.repository;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douzone.jblog.vo.UserVo;

@Repository
public class UserRepository {

	@Autowired
	private SqlSession sqlSession;
	
	public int insert(UserVo userVo) {
		return sqlSession.insert("user.insert", userVo);
	}
	
	public UserVo findUser(UserVo userVo) {
		return sqlSession.selectOne("user.find", userVo);
	}

	public UserVo getUserById(String id) {
		return sqlSession.selectOne("user.getUserById", id);
	}

}
