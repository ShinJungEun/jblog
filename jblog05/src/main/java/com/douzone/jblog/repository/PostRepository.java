package com.douzone.jblog.repository;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douzone.jblog.vo.PostVo;

@Repository
public class PostRepository {
	
	@Autowired
	private SqlSession sqlSession;
	
	public int postCount(Long categoryNo) {
		return sqlSession.selectOne("post.postCount", categoryNo);
	}

	public int insert(PostVo postVo) {
		return sqlSession.insert("post.insert", postVo);
	}
	
	public List<PostVo> postList(Long categoryNo) {
		return sqlSession.selectList("post.findPostList", categoryNo);
	}
	
	public Long findPostNo(Long categoryNo) {
		return sqlSession.selectOne("post.findPostNo", categoryNo);
	}
	
	public PostVo PostView(Long no) {
		return sqlSession.selectOne("post.PostView", no);
	}
	
}
