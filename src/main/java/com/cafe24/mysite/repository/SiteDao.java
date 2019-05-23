package com.cafe24.mysite.repository;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cafe24.mysite.vo.SiteVo;

@Repository
public class SiteDao {
	
	@Autowired
	SqlSession sqlSession;
	
	public boolean update(SiteVo siteVo) {
		int result = sqlSession.update("site.update", siteVo);
		return result == 1;
	}
	
	public SiteVo getOne() {
		return (SiteVo)sqlSession.selectOne("site.getOne");
	}
}
