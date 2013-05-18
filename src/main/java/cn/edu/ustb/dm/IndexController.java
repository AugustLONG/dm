package cn.edu.ustb.dm;

import java.util.Locale;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.edu.ustb.HomeController;
import cn.edu.ustb.dm.dao.BookAuthorInfoMapper;

@Controller
@RequestMapping(value = "/", method = RequestMethod.GET)
public class IndexController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Resource
	public BookAuthorInfoMapper bookAuthorInfoMapper;
	
	@RequestMapping(value="/", method = RequestMethod.GET)
	public String index(Locale locale, Model model){
		return "index";
	}

}
