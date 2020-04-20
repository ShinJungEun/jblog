package com.douzone.jblog.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.douzone.jblog.dto.JsonResult;
import com.douzone.jblog.service.CategoryService;
import com.douzone.jblog.service.PostService;
import com.douzone.jblog.vo.CategoryVo;
import com.douzone.security.Auth;

@RestController("BlogApiController")
@RequestMapping("/{id:(?!assets).*}/admin/api/category")
public class BlogController {

	@Autowired
	private CategoryService categoryService;
	@Autowired
	private PostService postService;
	
	@Auth
	@RequestMapping(value="/list", method=RequestMethod.GET)
	public JsonResult list(@PathVariable("id") String id) {
		List<CategoryVo> categoryList = categoryService.list(id);
		
		for(CategoryVo vo : categoryList) {
			vo.setPostCount(postService.postCount(vo.getNo()));
		}
		
		return JsonResult.success(categoryList);
	}
	
	@Auth
	@PostMapping("/insert")
	public JsonResult insert(@RequestBody CategoryVo vo) {
		categoryService.insert(vo);
		
		return JsonResult.success(vo);
	}
	
	@DeleteMapping("/delete/{no}")
	public JsonResult delete(
			@PathVariable("no") Long no, 
			@RequestParam(value="password", required=true, defaultValue="") String password,
			@PathVariable("id") String id,
			@ModelAttribute CategoryVo categoryVo) {
		
				boolean result = false;
				
				// 카테고리 개수가 1개일 때는 지우지 못하도록.
				int categoryCount = categoryService.findCategoryCount(id);
				if(categoryCount <= 1) {
					return JsonResult.fail("not delete");
				}
				
				// 포스트 개수가 0일 때만 지울 수 있도록.
				int postCount = postService.postCount(categoryVo.getNo());
				if(postCount == 0) {
					result = categoryService.delete(id, no);
				}
				return JsonResult.success(result ? no : 1);
	}
}
