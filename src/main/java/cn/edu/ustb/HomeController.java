package cn.edu.ustb;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.edu.ustb.dm.dao.BookAuthorInfoMapper;
import cn.edu.ustb.dm.dao.BookOnlineInfoMapper;
import cn.edu.ustb.dm.dao.BookPublishingInfoMapper;
import cn.edu.ustb.dm.model.BookAuthorInfo;
import cn.edu.ustb.dm.model.BookAuthorInfoExample;
import cn.edu.ustb.dm.model.BookPublishingInfo;
import cn.edu.ustb.dm.model.BookPublishingInfoExample;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	@Resource
	public BookAuthorInfoMapper bookAuthorInfoMapper;
	@Resource
	public BookOnlineInfoMapper bookOnlineInfoMapper;
	@Resource
	public BookPublishingInfoMapper bookPublishingInfoMapper;
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);

		BookAuthorInfoExample bookAuthorInfoExample = new BookAuthorInfoExample();
		BookAuthorInfoExample.Criteria criteria = bookAuthorInfoExample.createCriteria();
		criteria.andBook_idGreaterThan(0);
		List<BookAuthorInfo> bookAuthorInfo = bookAuthorInfoMapper.selectByExample(bookAuthorInfoExample);
		
		BookPublishingInfoExample bookPublishingInfoExample = new BookPublishingInfoExample();
		BookPublishingInfoExample.Criteria criteriap = bookPublishingInfoExample.createCriteria();
		criteria.andBook_idGreaterThan(0);
		List<BookPublishingInfo> bookPublishingInfo = bookPublishingInfoMapper.selectByExample(bookPublishingInfoExample);
		
		logger.info("---------------------" + bookPublishingInfo.get(0).getPublisher());
		model.addAttribute("serverTime", bookPublishingInfo.get(0).getPublisher());
		
		return "home";
	}
}
