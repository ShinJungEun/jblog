package com.douzone.jblog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {
	
//	@Autowired
//	private SiteService siteService;
	
	@RequestMapping({"", "/main"})
	public String index() {
		return "main/index";
	}
}
