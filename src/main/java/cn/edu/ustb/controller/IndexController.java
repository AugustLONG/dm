package cn.edu.ustb.controller;

import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.edu.ustb.HomeController;
import cn.edu.ustb.dm.dao.BookInfoMapper;
import cn.edu.ustb.dm.model.BookInfo;
import cn.edu.ustb.dm.model.BookInfoExample;

@Controller
@RequestMapping(value = "/", method = RequestMethod.GET)
public class IndexController {
	
	private static final Logger logger = LoggerFactory.getLogger(IndexController.class);
	
	@Resource
	public BookInfoMapper bookInfoMapper;
	
	@RequestMapping(value="/", method = RequestMethod.GET)
	public String index(Locale locale, Model model){
		BookInfoExample example = new BookInfoExample();
		example.setOrderByClause("NUMBER_REVIEW DESC");
		List<BookInfo> bookInfoList = bookInfoMapper.selectByExample(example);
		List<>
		model.addAttribute("bookRecommendList", bookInfoList);
		return "index";
	}
}

class BookRecommend {
	private String imageSrc;
	private String title;
	private String author;
	private String binding;
	private 
}
