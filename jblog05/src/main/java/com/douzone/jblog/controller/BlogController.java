package com.douzone.jblog.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.douzone.jblog.service.BlogService;
import com.douzone.jblog.service.CategoryService;
import com.douzone.jblog.service.PostService;
import com.douzone.jblog.service.UserService;
import com.douzone.jblog.vo.BlogVo;
import com.douzone.jblog.vo.CategoryVo;
import com.douzone.jblog.vo.PostVo;
import com.douzone.jblog.vo.UserVo;
import com.douzone.security.Auth;
import com.douzone.security.AuthUser;

@Controller
@RequestMapping("/{id:(?!assets).*}")
//@RequestMapping("/{id}")
public class BlogController {

	@Autowired
	private BlogService blogService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private PostService postService;
	@Autowired
	private UserService userService;

	// 블로그 메인화면
	@RequestMapping( {"", "/{pathNo1}", "/{pathNo1}/{pathNo2}" } )
	public String jblog(@PathVariable("id") String id,
			@PathVariable Optional<Long> pathNo1,
			@PathVariable Optional<Long> pathNo2,
			Model model) {
		
		// 없는 user일 때 처리
		UserVo userVo = userService.getUserById(id);
		if(userVo == null) {
			return "error/404";			
		}

		Long categoryNo = 0L;
		Long postNo = 0L;

		if(pathNo2.isPresent() ) {
			postNo = pathNo2.get();
			categoryNo = pathNo1.get();
		} 
		else if(pathNo1.isPresent() ){
			categoryNo = pathNo1.get();
			postNo = postService.findPostNo(categoryNo);
		}
		else {
			categoryNo = categoryService.findCategoryNo(id);
			postNo = postService.findPostNo(categoryNo);
		}

		BlogVo blogVo = blogService.find(id);
		model.addAttribute("blogVo", blogVo);

		// 카테고리 리스트(우측 상단)
		List<CategoryVo> categoryList = categoryService.list(id);
		model.addAttribute("categoryList", categoryList);

		// 포스트 리스트
		List<PostVo> postList = postService.postList(categoryNo);
		model.addAttribute("postList", postList);

		// 포스트 뷰
		PostVo postViewVo = postService.PostView(postNo);
		model.addAttribute("postViewVo", postViewVo);

		return "/blog/blog-main";
	}


	// 기본설정 업데이트 페이지
	@Auth
	@RequestMapping(value="/admin/basic", method=RequestMethod.GET)
	public String update(@PathVariable("id") String id, Model model,
			@AuthUser UserVo authUser) {

		BlogVo blogVo = blogService.find(id);
		model.addAttribute("blogVo", blogVo);
		return "blog/blog-admin-basic";
	}

	@Auth
	@RequestMapping(value="/admin/basic", method=RequestMethod.POST)
	public String update(BlogVo blogVo,
			@PathVariable("id") String id,
			@RequestParam(value="logo-file") MultipartFile multipartFile,
			@AuthUser UserVo authUser) {

		// 접근제어
		if(!id.equals(authUser.getId())) {	
			return "redirect:/{id}";
		}

		String logo = "";
		if(multipartFile.isEmpty()) {
			logo = blogService.findLogo(id);
			blogVo.setLogo(logo);
			blogService.update(blogVo);
			return "redirect:/{id}";
		}
		logo = blogService.restore(multipartFile);
		blogVo.setLogo(logo);

		blogService.update(blogVo);

		return "redirect:/{id}";
	}

	// 카테고리 설정 페이지
	@Auth
	@RequestMapping(value="/admin/category", method=RequestMethod.GET)
	public String category(Model model,
			@PathVariable("id") String id,
			@AuthUser UserVo authUser) {
		List<CategoryVo> categoryList = categoryService.list(id);

		for(CategoryVo vo : categoryList) {
			vo.setPostCount(postService.postCount(vo.getNo()));
		}
		
		model.addAttribute("categoryList", categoryList);
		
		BlogVo blogVo = blogService.find(id);
		model.addAttribute("blogVo", blogVo);
		
		// 카테고리 개수
		int categoryCount = categoryService.findCategoryCount(id);
		model.addAttribute("categoryCount", categoryCount);

		return "blog/blog-admin-category";
	}

	// 카테고리 추가
	@Auth
	@RequestMapping(value="/admin/category/insert", method=RequestMethod.POST)
	public String categoryInsert(@ModelAttribute CategoryVo categoryVo,
			@PathVariable("id") String id,
			@AuthUser UserVo authUser) {
		categoryVo.setId(id);
		categoryService.insert(categoryVo);

		return "redirect:/{id}/admin/category";
	}

	// 카테고리 삭제
	@Auth
	@RequestMapping(value="/admin/category/delete/{no}")
	public String categoryDelete(@ModelAttribute CategoryVo categoryVo,
			@PathVariable("id") String id,
			@PathVariable("no") Long no,
			@AuthUser UserVo authUser) {
		// 카테고리 개수가 1개일 때는 지우지 못하도록.
		int categoryCount = categoryService.findCategoryCount(id);
		if(categoryCount <= 1) {
			return "redirect:/{id}/admin/category";
		}
		
		// 포스트 개수가 0일 때만 지울 수 있도록.
		int postCount = postService.postCount(categoryVo.getNo());
		if(postCount == 0) {
			categoryService.delete(id, no);
		}
		
		return "redirect:/{id}/admin/category";
	}

	// 글 작성 페이지
	@Auth
	@RequestMapping(value="/admin/write", method=RequestMethod.GET)
	public String write(@PathVariable("id") String id,
			Model model,
			@AuthUser UserVo authUser) {
		BlogVo blogVo = blogService.find(id);
		model.addAttribute("blogVo", blogVo);

		List<CategoryVo> categoryList = categoryService.list(id);
		model.addAttribute("categoryList", categoryList);

		return "blog/blog-admin-write";
	}

	// 글 작성
	@Auth
	@RequestMapping(value="/admin/write", method=RequestMethod.POST)
	public String write(@PathVariable("id") String id,
			@ModelAttribute PostVo postVo,
			@RequestParam("category") String categoryName,
			@AuthUser UserVo authUser) {
		Long categoryNo = categoryService.findCategoryNo(id);
		postVo.setCategoryNo(categoryNo);
		postService.insert(postVo);

		return "redirect:/{id}";
	}
	
	@Auth
	@RequestMapping(value={"/admin/api/category"}, method=RequestMethod.GET)
	public String index(@PathVariable("id") String id, Model model) {
		BlogVo blogVo = blogService.find(id);
		model.addAttribute("blogVo", blogVo);
		return "blog/blog-api-admin-category";
	}
}
