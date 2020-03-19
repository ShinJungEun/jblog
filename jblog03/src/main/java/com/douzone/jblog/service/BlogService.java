package com.douzone.jblog.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.douzone.jblog.repository.BlogRepository;
import com.douzone.jblog.vo.BlogVo;

@Service
public class BlogService {

	@Autowired
	private BlogRepository blogRepository;

	private static final String SAVE_PATH = "/jblog-uploads";
	private static final String URL = "/assets/image";
	
	public boolean insert(String id) {
		int count = blogRepository.insert(id);
		return count == 1;
	}
	
	public BlogVo find(String id) {
		return blogRepository.find(id);
	}
	
	public String restore(MultipartFile multipartFile) {
		String url = "";
		try {
			if(multipartFile.isEmpty()) {
				return url;
			}

			String originFilename = multipartFile.getOriginalFilename();
			// 확장자 제거를 위한 시작 index 추출
			String extName = originFilename.substring(originFilename.lastIndexOf('.') + 1);	
			String saveFilename = generateSaveFilename(extName);
			long fileSize = multipartFile.getSize();

			byte[] fileData = multipartFile.getBytes();
			OutputStream os = new FileOutputStream(SAVE_PATH + "/" + saveFilename);
			os.write(fileData);
			os.close();
			
			url = URL + "/" + saveFilename;

		} catch(IOException ex) {
			throw new RuntimeException("file upload error:" + ex);
		}

		return url;
	}
	
	private String generateSaveFilename(String extName) {
		String filename = "";

		Calendar calendar = Calendar.getInstance();		// 현재시간
		filename += calendar.get(Calendar.YEAR);
		filename += calendar.get(Calendar.MONTH);
		filename += calendar.get(Calendar.DATE);
		filename += calendar.get(Calendar.HOUR);
		filename += calendar.get(Calendar.MINUTE);
		filename += calendar.get(Calendar.SECOND);
		filename += calendar.get(Calendar.MILLISECOND);
		filename += ("." + extName);

		return filename;
	}

	public String findLogo(String id) {
		BlogVo blogVo = blogRepository.find(id);
		String logo = blogVo.getLogo();
		return logo;
	}

	public boolean update(BlogVo blogVo) {
		int count = blogRepository.update(blogVo);
		return count == 1;
	}

}
