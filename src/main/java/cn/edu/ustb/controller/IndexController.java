package cn.edu.ustb.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
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

import cn.edu.ustb.dm.dao.BookInfoMapper;
import cn.edu.ustb.dm.dao.BookPublishingInfoMapper;
import cn.edu.ustb.dm.dao.BookTagRelationMapper;
import cn.edu.ustb.dm.dao.TagInfoMapper;
import cn.edu.ustb.dm.model.BookInfo;
import cn.edu.ustb.dm.model.BookInfoExample;
import cn.edu.ustb.dm.model.BookPublishingInfoExample;
import cn.edu.ustb.dm.model.BookPublishingInfoWithBLOBs;
import cn.edu.ustb.dm.model.BookTagRelation;
import cn.edu.ustb.dm.model.BookTagRelationExample;
import cn.edu.ustb.dm.model.TagInfo;
import cn.edu.ustb.dm.model.TagInfoExample;
import cn.edu.ustb.model.BookListItemModel;
import cn.edu.ustb.model.BookClassifyItemModel;

@Controller
@RequestMapping(value = "/", method = RequestMethod.GET)
public class IndexController {
	@Resource
	private SqlSessionFactory sqlSessionFactory;

	private static final Logger logger = LoggerFactory.getLogger(IndexController.class);
	private Properties properties;
	private SqlSession session;
	private BookInfoMapper bookInfoMapper;
	private BookPublishingInfoMapper bookPublishingMapper;
	private TagInfoMapper tagInfoMapper;
	private BookTagRelationMapper bookTagRelationMapper;
	private List<BookListItemModel> bookRecommendList;
	private List<BookClassifyItemModel> bookRecommendClassifyList;
	private List<BookListItemModel> bookRankList;
	
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
	private SqlSession getSession() {
		if(session == null) {
			session = sqlSessionFactory.openSession();
		}
		return session;
	}
	private BookInfoMapper getBookInfoMapper() {
		if(bookInfoMapper == null) {
			bookInfoMapper = getSession().getMapper(BookInfoMapper.class);
		}
		return bookInfoMapper;
	}
	private BookPublishingInfoMapper getBookPublishingMapper() {
		if(bookPublishingMapper == null) {
			bookPublishingMapper = getSession().getMapper(BookPublishingInfoMapper.class);
		}
		return bookPublishingMapper;
	}
	private TagInfoMapper getTagInfoMapper() {
		if(tagInfoMapper == null) {
			tagInfoMapper = getSession().getMapper(TagInfoMapper.class);
		}
		return tagInfoMapper;
	}
	private BookTagRelationMapper getBookTagRelationMapper() {
		if(bookTagRelationMapper == null) {
			bookTagRelationMapper = getSession().getMapper(BookTagRelationMapper.class);
		}
		return bookTagRelationMapper;
	}
	private List<BookListItemModel> getBookRecommendList() {
		if(bookRecommendList == null) {
			bookRecommendList = new ArrayList<BookListItemModel>(6);
			BookInfoExample infoExample = new BookInfoExample();
			infoExample.setOrderByClause("NUMBER_TOREAD DESC");
			List<BookInfo> bookInfoList = getBookInfoMapper().selectByExample(infoExample);
			for(int i=0; i<6&&i<bookInfoList.size(); i++) {
				BookInfo bookInfo = bookInfoList.get(i);
				BookListItemModel item = new BookListItemModel();
				item.setImageSrc(getProperties().getProperty("book_mpic") + bookInfo.getTITLE_PAGE_IMAGES());
				item.setTitle(bookInfo.getTITLE());
				BookPublishingInfoExample publishingExample = new BookPublishingInfoExample();
				publishingExample.or().andBook_idEqualTo(bookInfo.getBOOK_ID());
				List<BookPublishingInfoWithBLOBs> bookPublishingList =
						getBookPublishingMapper().selectByExampleWithBLOBs(publishingExample);
				item.setAuthor(bookPublishingList.get(0).getAuther_name());
				item.setStar(bookInfo.getNUMBER_STAR1(), bookInfo.getNUMBER_STAR2(),
						bookInfo.getNUMBER_STAR3(), bookInfo.getNUMBER_STAR4(),
						bookInfo.getNUMBER_STAR5());
				bookRecommendList.set(i, item);
			}
		}
		return bookRecommendList;
	}
	private List<BookClassifyItemModel> getBookRecommendClassifyList() {
		if(bookRecommendClassifyList == null) {
			bookRecommendClassifyList = new ArrayList<BookClassifyItemModel>(12);
			TagInfoExample tagExample = new TagInfoExample();
			tagExample.setOrderByClause("COUNT DESC");
			List<TagInfo> tagInfoList = this.getTagInfoMapper().selectByExample(tagExample);
			for(int i=0; i<12&&i<tagInfoList.size(); i++) {
				TagInfo tagInfo = tagInfoList.get(i);
				BookClassifyItemModel item = new BookClassifyItemModel();
				item.setImageSrc(getProperties().getProperty("book_mpic"));
				item.setTagName(tagInfo.getNAME());
				BookTagRelationExample relationExample = new BookTagRelationExample();
				relationExample.or().andTAG_IDEqualTo(tagInfo.getTAG_ID());
				List<BookTagRelation> relationList =
						getBookTagRelationMapper().selectByExample(relationExample);
				List<String> titleList = new ArrayList<String>(2);
				for(int j=0; j<2&&j<relationList.size(); j++) {
					BookInfoExample bookExample = new BookInfoExample();
					bookExample.or().andBOOK_IDEqualTo(relationList.get(0).getBOOK_ID());
					List<BookInfo> bookList = getBookInfoMapper().selectByExample(bookExample);
					if(bookList.size() > 0) {
						titleList.set(j, bookList.get(0).getTITLE());
					}
				}
				item.setTitleList(titleList);
				bookRecommendClassifyList.set(i, item);
			}
		}
		return bookRecommendClassifyList;
	}
	private List<BookListItemModel> getBookRankList() {
		if(bookRankList != null) {
			bookRankList = new ArrayList<BookListItemModel>(6);
			BookInfoExample example = new BookInfoExample();
			example.setOrderByClause("NUMBER_REVIEW DESC");
			List<BookInfo> bookList = getBookInfoMapper().selectByExample(example);
			for(int i=0; i<6&&i<bookList.size(); i++) {
				BookInfo bookInfo = bookList.get(i);
				BookListItemModel item = new BookListItemModel();
				item.setImageSrc(getProperties().getProperty("book_mpic")
						+ bookInfo.getTITLE_PAGE_IMAGES());
				item.setTitle(bookInfo.getTITLE());
				bookRankList.set(i, item);
			}
		}
		return bookRankList;
	}
	
	@RequestMapping(value="/", method = RequestMethod.GET)
	public String index(Locale locale, Model model){
		try {
			model.addAttribute("bookRecommendList", getBookRecommendList());
			model.addAttribute("bookRecommendClassifyList", getBookRecommendClassifyList());
			model.addAttribute("bookRankList", getBookRankList());
			getSession().commit();
		} finally {
			getSession().close();
		}
		return "index";
	}
}
