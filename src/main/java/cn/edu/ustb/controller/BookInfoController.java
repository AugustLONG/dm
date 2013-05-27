package cn.edu.ustb.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.edu.ustb.dm.dao.BookAuthorInfoMapper;
import cn.edu.ustb.dm.dao.BookOnlineInfoMapper;
import cn.edu.ustb.dm.dao.BookPublishingInfoMapper;

@Controller
public class BookInfoController {
	@Resource
	private SqlSessionFactory sqlSessionFactory;

	private static final Logger logger = LoggerFactory.getLogger(BookInfoController.class);
	private Properties properties;
	private SqlSession session;
	private BookPublishingInfoMapper publishingInfoMapper;
	private BookOnlineInfoMapper onlineInfoMapper;
	private BookAuthorInfoMapper authorInfoMapper;

	private Properties getProperties() {
		if(properties == null) {
			properties = new Properties();
			try {
				InputStream in = getClass().getResourceAsStream("/Web.properties");
				properties.load(in);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return properties;
	}
	private BookPublishingInfoMapper getPublishingInfoMapper() {
		if(publishingInfoMapper == null)
			publishingInfoMapper =  session.getMapper(BookPublishingInfoMapper.class);
		return publishingInfoMapper;
	}
	private BookOnlineInfoMapper getOnlineInfoMapper() {
		if(onlineInfoMapper == null)
			onlineInfoMapper =  session.getMapper(BookOnlineInfoMapper.class);
		return onlineInfoMapper;
	}
	private BookAuthorInfoMapper getBookAuthorInfoMapper() {
		if(authorInfoMapper == null)
			authorInfoMapper =  session.getMapper(BookAuthorInfoMapper.class);
		return authorInfoMapper;
	}
	
	private void resetMapper() {
		publishingInfoMapper = null;
		onlineInfoMapper = null;
		authorInfoMapper = null;
	}
	
	@RequestMapping(value="/bookInfo", method = RequestMethod.GET)
	public String bookInfo(Locale locale, Model model,
			@RequestParam("id") int id) {
		resetMapper();
		session = sqlSessionFactory.openSession();
		try {
			model.addAttribute("imageSrc", getProperties().getProperty("book_lpic"));
			model.addAttribute("publishing", getPublishingInfoMapper().selectByPrimaryKey(id));
			model.addAttribute("online", getOnlineInfoMapper().selectByPrimaryKey(id));
			model.addAttribute("author", getBookAuthorInfoMapper().selectByPrimaryKey(id));
			session.commit();
		} finally {
			session.close();
		}
		return "bookInfo";
	}

}
