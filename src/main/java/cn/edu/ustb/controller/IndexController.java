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
	private BookInfoMapper getBookInfoMapper() {
		if(bookInfoMapper == null) {
			bookInfoMapper = session.getMapper(BookInfoMapper.class);
		}
		return bookInfoMapper;
	}
	private BookPublishingInfoMapper getBookPublishingMapper() {
		if(bookPublishingMapper == null) {
			bookPublishingMapper = session.getMapper(BookPublishingInfoMapper.class);
		}
		return bookPublishingMapper;
	}
	private TagInfoMapper getTagInfoMapper() {
		if(tagInfoMapper == null) {
			tagInfoMapper = session.getMapper(TagInfoMapper.class);
		}
		return tagInfoMapper;
	}
	private BookTagRelationMapper getBookTagRelationMapper() {
		if(bookTagRelationMapper == null) {
			bookTagRelationMapper = session.getMapper(BookTagRelationMapper.class);
		}
		return bookTagRelationMapper;
	}
	private List<BookListItemModel> getBookRecommendList() {
		if(bookRecommendList == null) {
			bookRecommendList = new ArrayList<BookListItemModel>(6);
			BookInfoExample infoExample = new BookInfoExample();
			infoExample.setOrderByClause("NUMBER_TOREAD DESC limit 30");
			infoExample.or().andTITLE_PAGE_IMAGESIsNotNull();
			List<BookInfo> bookInfoList = getBookInfoMapper().selectByExample(infoExample);
			int i=0;
			for(BookInfo bookInfo : bookInfoList) {
				logger.info("bookInfo i = " + i);
				if(bookInfo.getTITLE_PAGE_IMAGES().trim().length() != 0) {
					if(i<6)
						i++;
					else
						break;
					logger.info("bookInfo with image i = " + i);
					BookListItemModel item = new BookListItemModel();
					item.setId(bookInfo.getBOOK_ID());
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
					bookRecommendList.add(item);
				}
			}
		}
		return bookRecommendList;
	}
	private List<BookClassifyItemModel> getBookRecommendClassifyList() {
		if(bookRecommendClassifyList == null) {
			bookRecommendClassifyList = new ArrayList<BookClassifyItemModel>(12);
			TagInfoExample tagExample = new TagInfoExample();
			tagExample.setOrderByClause("COUNT DESC limit 12");
			List<TagInfo> tagInfoList = this.getTagInfoMapper().selectByExample(tagExample);
			for(int i=0; i<12&&i<tagInfoList.size(); i++) {
				TagInfo tagInfo = tagInfoList.get(i);
				BookClassifyItemModel item = new BookClassifyItemModel();
				item.setTagName(tagInfo.getNAME());
				BookTagRelationExample relationExample = new BookTagRelationExample();
				relationExample.or().andTAG_IDEqualTo(tagInfo.getTAG_ID());
				List<BookTagRelation> relationList =
						getBookTagRelationMapper().selectByExample(relationExample);
				List<BookClassifyItemModel.TitleList> titleList = new ArrayList<BookClassifyItemModel.TitleList>(2);
				int j = 0;
				for(BookTagRelation relation : relationList) {
					BookInfoExample bookExample = new BookInfoExample();
					bookExample.or().andBOOK_IDEqualTo(relation.getBOOK_ID());
					List<BookInfo> bookList = getBookInfoMapper().selectByExample(bookExample);
					if(bookList.size() > 0) {
						BookInfo bookInfo = bookList.get(0);
						if(bookInfo.getTITLE_PAGE_IMAGES() != null &&
								bookInfo.getTITLE_PAGE_IMAGES().trim().length() != 0) {
							if(j<2)
								j++;
							else
								break;
							if(j == 1)
								item.setImageSrc(getProperties().getProperty("book_mpic")
										+ bookInfo.getTITLE_PAGE_IMAGES());
							BookClassifyItemModel.TitleList title = item.createTitleList();
							title.setId(bookInfo.getBOOK_ID());
							title.setTitle(bookInfo.getTITLE());
							titleList.add(title);
						}
					}
				}
				item.setTitleList(titleList);
				bookRecommendClassifyList.add(item);
			}
		}
		return bookRecommendClassifyList;
	}
	private List<BookListItemModel> getBookRankList() {
		if(bookRankList == null) {
			bookRankList = new ArrayList<BookListItemModel>(16);
			BookInfoExample example = new BookInfoExample();
			example.setOrderByClause("NUMBER_REVIEW DESC limit 40");
			example.or().andTITLE_PAGE_IMAGESIsNotNull();
			List<BookInfo> bookList = getBookInfoMapper().selectByExample(example);
			int i = 0;
			for(BookInfo bookInfo : bookList) {
				if(bookInfo.getTITLE_PAGE_IMAGES().trim().length() != 0) {
					if(i<16)
						i++;
					else
						break;
					BookListItemModel item = new BookListItemModel();
					item.setId(bookInfo.getBOOK_ID());
					item.setImageSrc(getProperties().getProperty("book_mpic")
							+ bookInfo.getTITLE_PAGE_IMAGES());
					item.setTitle(bookInfo.getTITLE());
					bookRankList.add(item);
				}
			}
		}
		return bookRankList;
	}
	
	@RequestMapping(value="/", method = RequestMethod.GET)
	public String index(Locale locale, Model model){
		session = sqlSessionFactory.openSession();
		try {
			model.addAttribute("bookRecommendList", getBookRecommendList());
			model.addAttribute("bookRecommendClassifyList", getBookRecommendClassifyList());
			model.addAttribute("bookRankList", getBookRankList());
			session.commit();
		} finally {
			session.close();
		}
		return "index";
	}
}
