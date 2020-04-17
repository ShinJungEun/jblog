package com.douzone.jblog.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douzone.jblog.vo.CategoryVo;

@Repository
public class CategoryRepository {

	@Autowired
	private SqlSession sqlSession;
	
	public int defaultInsert(String id) {
		return sqlSession.insert("category.defaultInsert", id);
	}

	public int insert(CategoryVo categoryVo) {
		return sqlSession.insert("category.insert", categoryVo);
	}
	
	public List<CategoryVo> findAll(String id) {
		return sqlSession.selectList("category.findAll", id);
	}
	
	public Long findCategoryNo(String id) {
		return sqlSession.selectOne("category.findCategoryNo", id);
	}

	public int delete(String id, Long no) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		map.put("no", no);
		return sqlSession.delete("category.delete", map);
	}
	
	public int findCategoryCount(String id) {
		return sqlSession.selectOne("category.findCategoryCount", id);
	}

}
