package com.cafe24.mysite.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cafe24.mysite.repository.SiteDao;
import com.cafe24.mysite.vo.SiteVo;

@Service
public class SiteService {
	private String ADMIN_SAVE_PATH = "/mysite-uploads/admin";
	private String ADMIN_URL = "/images/admin";
	
	@Autowired
	SiteDao siteDao;
	
	public boolean mainUpdate(SiteVo siteVo, MultipartFile file1) {
		String url = restore(file1);
		siteVo.setProfile(url);
		
		if(url.equals("")) {
			SiteVo SiteVoOld = getOne();
			siteVo.setProfile(SiteVoOld.getProfile());
		}
		
		return siteDao.update(siteVo);
	}
	
	public SiteVo getOne() {
		return siteDao.getOne();
	}
	
	
	
	
	public String restore(MultipartFile multipartFile) {
		String url = "";

		try {
		
			if(multipartFile.isEmpty()) {
				return url;
			}
			
			String originalFilename = 
					multipartFile.getOriginalFilename();
			String extName = originalFilename.substring(originalFilename.lastIndexOf('.')+1);
			String saveFileName = generateSaveFileName(extName);
			long fileSize = multipartFile.getSize();
			
			System.out.println("##########" + originalFilename);
			System.out.println("##########" + extName);
			System.out.println("##########" + saveFileName);
			System.out.println("##########" + fileSize);
			
			byte[] fileData = multipartFile.getBytes();
			OutputStream os = new FileOutputStream(ADMIN_SAVE_PATH + "/" + saveFileName);
			os.write(fileData);
			os.close();
			
			url = ADMIN_URL + "/" + saveFileName;
			
		} catch (IOException e) {
			throw new RuntimeException("Fileupload error:" + e);
		}
		
		return url;
	}
	
	private String generateSaveFileName(String extName) {
		String filename = "";
		Calendar calendar = Calendar.getInstance();
		
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
}
