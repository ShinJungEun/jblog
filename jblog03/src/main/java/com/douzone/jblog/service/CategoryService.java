package com.douzone.jblog.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.douzone.jblog.repository.CategoryRepository;
import com.douzone.jblog.vo.CategoryVo;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;
	
	public boolean defaultInsert(String id) {
		int count = categoryRepository.defaultInsert(id);
		return count == 1;
	}
	
	public boolean insert(CategoryVo categoryVo) {
		int count = categoryRepository.insert(categoryVo);
		return count == 1;		
	}
	
	public boolean delete(String id, Long no) {
		int count = categoryRepository.delete(id, no);
		return count == 1;	
	}
	
	public List<CategoryVo> list(String id) {
		return categoryRepository.findAll(id);
		
	}
	
	public Long findCategoryNo(String id) {
		return categoryRepository.findCategoryNo(id);
	}
	
	public int findCategoryCount(String id) {
		return categoryRepository.findCategoryCount(id);
	}
	
}
