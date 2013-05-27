package cn.edu.ustb.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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
import org.springframework.web.bind.annotation.RequestParam;

import cn.edu.ustb.dm.dao.AuthorInfoMapper;
import cn.edu.ustb.dm.dao.BookAuthorRelationMapper;
import cn.edu.ustb.dm.dao.BookInfoMapper;
import cn.edu.ustb.dm.dao.BookPublishingInfoMapper;
import cn.edu.ustb.dm.dao.BookTagRelationMapper;
import cn.edu.ustb.dm.dao.PublisherInfoMapper;
import cn.edu.ustb.dm.dao.TagInfoMapper;
import cn.edu.ustb.dm.model.AuthorInfo;
import cn.edu.ustb.dm.model.AuthorInfoExample;
import cn.edu.ustb.dm.model.BookAuthorRelation;
import cn.edu.ustb.dm.model.BookAuthorRelationExample;
import cn.edu.ustb.dm.model.BookInfo;
import cn.edu.ustb.dm.model.BookInfoExample;
import cn.edu.ustb.dm.model.BookPublishingInfoExample;
import cn.edu.ustb.dm.model.BookPublishingInfoWithBLOBs;
import cn.edu.ustb.dm.model.PublisherInfo;
import cn.edu.ustb.dm.model.PublisherInfoExample;
import cn.edu.ustb.model.BookListItemModel;
import cn.edu.ustb.model.SearchResultPaginationModel;

@Controller
public class ResultListController {
	@Resource
	private SqlSessionFactory sqlSessionFactory;

	private static final Logger logger = LoggerFactory.getLogger(ResultListController.class);
	private Properties properties;
	private SqlSession session;
	private BookInfoMapper bookInfoMapper;
	private TagInfoMapper tagInfoMapper;
	private BookTagRelationMapper bookTagRelationMapper;
	private PublisherInfoMapper publisherMapper;
	private AuthorInfoMapper authorMapper;
	private BookAuthorRelationMapper authorRelationMapper;
	private BookPublishingInfoMapper publishingInfoMapper;
	private List<Integer> relativeAuthorList;
	private List<BookListItemModel> resultList;
	private List<BookListItemModel> titleResultList;
	private List<BookListItemModel> authorResultList;
	private List<BookListItemModel> publisherResultList;
	private List<BookListItemModel> bookRankList;
	private Integer titleResultListCount;
	private Integer authorResultListCount;
	private Integer publisherResultListCount;
	private String title;
	private String author;
	private String publisher;
	private int from;
	private int count;

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
	private PublisherInfoMapper getPublisherMapper() {
		if(publisherMapper == null) {
			publisherMapper = session.getMapper(PublisherInfoMapper.class);
		}
		return publisherMapper;
	}
	private AuthorInfoMapper getAuthorMapper() {
		if(authorMapper == null) {
			authorMapper = session.getMapper(AuthorInfoMapper.class);
		}
		return authorMapper;
	}
	private BookAuthorRelationMapper getAuthorRelationMapper() {
		if(authorRelationMapper == null) {
			authorRelationMapper = session.getMapper(BookAuthorRelationMapper.class);
		}
		return authorRelationMapper;
	}
	private BookPublishingInfoMapper getPublishingInfoMapper() {
		if(publishingInfoMapper == null) {
			publishingInfoMapper =  session.getMapper(BookPublishingInfoMapper.class);
		}
		return publishingInfoMapper;
	}
	private List<Integer> getRelativeAuthorList(String author) {
		logger.info("author = " + author + " old: " + this.author + " isEqual: " + (author.equals(this.author)));
		if(relativeAuthorList == null || !author.equals(this.author)) {
			relativeAuthorList = new ArrayList<Integer>();
			AuthorInfoExample authorExample = new AuthorInfoExample();
			authorExample.or().andAUTHOR_NAMELike("%" + author + "%");
			List<AuthorInfo> authorList = getAuthorMapper().selectByExample(authorExample);
			for(AuthorInfo authorInfo : authorList) {
				if(authorInfo.getAUTHOR_ID() != 0) {
					relativeAuthorList.add(authorInfo.getAUTHOR_ID());
				}
			}
			logger.info("author count = " + relativeAuthorList.size());
		}
		return relativeAuthorList;
	}
	private List<BookListItemModel> getResultList(List<BookInfo> bookInfoList) {
		resultList = new ArrayList<BookListItemModel>(bookInfoList.size());
		for(BookInfo bookInfo : bookInfoList) {
			BookListItemModel item = new BookListItemModel();
			item.setId(bookInfo.getBOOK_ID());
			BookPublishingInfoExample publishingExample = new BookPublishingInfoExample();
			publishingExample.or().andBook_idEqualTo(bookInfo.getBOOK_ID());
			List<BookPublishingInfoWithBLOBs> publishingInfoList =
					getPublishingInfoMapper().selectByExampleWithBLOBs(publishingExample);
			if(publishingInfoList.size() > 0) {
				item.setAuthor(publishingInfoList.get(0).getAuther_name());
				item.setPublisher(publishingInfoList.get(0).getPublisher());
			}
			if(bookInfo.getPUBLICATION_DATE() != null)
				item.setDate(bookInfo.getPUBLICATION_DATE());
			if(bookInfo.getTITLE_PAGE_IMAGES() != null &&
					bookInfo.getTITLE_PAGE_IMAGES().trim().length() != 0)
				item.setImageSrc(getProperties().getProperty("book_mpic")
						+ bookInfo.getTITLE_PAGE_IMAGES());
			else
				item.setImageSrc("/resources/img/default_book.jpg");
			item.setPrice(bookInfo.getLIST_PRICE());
			item.setStar(bookInfo.getNUMBER_STAR1(), bookInfo.getNUMBER_STAR2(),
					bookInfo.getNUMBER_STAR3(), bookInfo.getNUMBER_STAR4(),
					bookInfo.getNUMBER_STAR5());
			item.setTitle(bookInfo.getTITLE());
			resultList.add(item);
		}
		return resultList;
	}
	private List<BookListItemModel> getTitleResultList(String title, int from, int count) {
		if(titleResultList == null || !title.equals(this.title)
				|| from != this.from || count != this.count) {
			BookInfoExample bookExample = new BookInfoExample();
			bookExample.setOrderByClause("book_id limit " + from + "," + count);
			bookExample.or().andTITLELike("%" + title + "%");
			List<BookInfo> bookInfoList = getBookInfoMapper().selectByExample(bookExample);
			List<BookInfo> j
			= getBookInfoMapper().selectByExample(bookExample);
			titleResultList = getResultList(bookInfoList);
			this.title = title;
			this.from = from;
			this.count = count;
		}
		return titleResultList;
	}
	private List<BookListItemModel> getAuthorResultList(String author, int from, int count) {
		if(authorResultList == null || !author.equals(this.author)
				|| from != this.from || count != this.count) {
			BookAuthorRelationExample relationExample = new BookAuthorRelationExample();
			relationExample.setOrderByClause("book_id limit " + from + "," + count);
			relationExample.or().andAUTHOR_IDIn(getRelativeAuthorList(author));
			List<BookAuthorRelation> relationList = getAuthorRelationMapper().selectByExample(relationExample);
			List<BookInfo> bookInfoList = new ArrayList<BookInfo>(count);
			for(BookAuthorRelation relation : relationList) {
				BookInfoExample bookExample = new BookInfoExample();
				bookExample.or().andBOOK_IDEqualTo(relation.getBOOK_ID());
				bookInfoList.addAll(getBookInfoMapper().selectByExample(bookExample));
			}
			authorResultList = getResultList(bookInfoList);
			this.author = author;
			this.from = from;
			this.count = count;
		}
		return authorResultList;
	}
	private List<BookListItemModel> getPublisherResultList(String publisher, int from, int count) {
		if(publisherResultList == null || !publisher.equals(this.publisher)
				|| from != this.from || count != this.count) {
			PublisherInfoExample publisherExample = new PublisherInfoExample();
			publisherExample.or().andPUBLISHER_NAMELike("%" + publisher + "%");
			List<PublisherInfo> publisherList = getPublisherMapper().selectByExample(publisherExample);
			List<Integer> publisherIdList = new ArrayList<Integer>();
			for(PublisherInfo publisherInfo : publisherList) {
				publisherIdList.add(publisherInfo.getPUBLISHER_ID());
			}
			BookInfoExample bookExample = new BookInfoExample();
			bookExample.setOrderByClause("publication_date desc limit " + from + "," + count);
			bookExample.or().andPUBLISHER_IDIn(publisherIdList);
			List<BookInfo> bookInfoList = getBookInfoMapper().selectByExample(bookExample);
			publisherResultList = getResultList(bookInfoList);
			this.publisher = publisher;
			this.from = from;
			this.count = count;
		}
		return publisherResultList;
	}
	private Integer getTitleResultListCount(String title) {
		if(titleResultListCount == null || !title.equals(this.title)) {
			BookInfoExample bookExample = new BookInfoExample();
			bookExample.or().andTITLELike("%" + title + "%");
			titleResultListCount = getBookInfoMapper().countByExample(bookExample);
			logger.info("titleResultList.size = " + titleResultListCount);
		}
		return titleResultListCount;
	}
	private Integer getAuthorResultListCount(String author) {
		logger.info("getauthor = " + author + " old: " + this.author);
		if(authorResultListCount == null || !author.equals(this.author)) {
			logger.info("ininin");
			BookAuthorRelationExample relationExample = new BookAuthorRelationExample();
			List<Integer> authorList = getRelativeAuthorList(author);
			if(authorList.size() == 0)
				return 0;
			relationExample.or().andAUTHOR_IDIn(authorList);
			authorResultListCount = getAuthorRelationMapper().countByExample(relationExample);
		}
		return authorResultListCount;
	}
	private Integer getPublisherResultListCount(String publisher) {
		if(publisherResultListCount == null || !publisher.equals(this.publisher)) {
			PublisherInfoExample publisherExample = new PublisherInfoExample();
			publisherExample.or().andPUBLISHER_NAMELike("%" + publisher + "%");
			List<PublisherInfo> publisherList = getPublisherMapper().selectByExample(publisherExample);
			if(publisherList.size() == 0)
				return 0;
			List<Integer> publisherIdList = new ArrayList<Integer>();
			for(PublisherInfo publisherInfo : publisherList) {
				publisherIdList.add(publisherInfo.getPUBLISHER_ID());
			}
			BookInfoExample bookExample = new BookInfoExample();
			bookExample.or().andPUBLISHER_IDIn(publisherIdList);
			publisherResultListCount = getBookInfoMapper().countByExample(bookExample);
		}
		return publisherResultListCount;
	}
	
	private List<BookListItemModel> getBookRankList() {
		if(bookRankList == null) {
			bookRankList = new ArrayList<BookListItemModel>(20);
			BookInfoExample example = new BookInfoExample();
			example.setOrderByClause("NUMBER_REVIEW DESC limit 40");
			example.or().andTITLE_PAGE_IMAGESIsNotNull();
			List<BookInfo> bookList = getBookInfoMapper().selectByExample(example);
			int i = 0;
			for(BookInfo bookInfo : bookList) {
				if(bookInfo.getTITLE_PAGE_IMAGES().trim().length() != 0) {
					if(i<20)
						i++;
					else
						break;
					BookListItemModel item = new BookListItemModel();
					item.setId(bookInfo.getBOOK_ID());
					item.setImageSrc(getProperties().getProperty("book_mpic")
							+ bookInfo.getTITLE_PAGE_IMAGES());
					item.setTitle(bookInfo.getTITLE());
					BookPublishingInfoExample publishingExample = new BookPublishingInfoExample();
					publishingExample.or().andBook_idEqualTo(bookInfo.getBOOK_ID());
					List<BookPublishingInfoWithBLOBs> bookPublishingList =
							getPublishingInfoMapper().selectByExampleWithBLOBs(publishingExample);
					item.setAuthor(bookPublishingList.get(0).getAuther_name());
					item.setStar(bookInfo.getNUMBER_STAR1(), bookInfo.getNUMBER_STAR2(),
							bookInfo.getNUMBER_STAR3(), bookInfo.getNUMBER_STAR4(),
							bookInfo.getNUMBER_STAR5());
					bookRankList.add(item);
				}
			}
		}
		return bookRankList;
	}
	
	private void resetMapper() {
		bookInfoMapper = null;
		tagInfoMapper = null;
		bookTagRelationMapper = null;
		publisherMapper = null;
		authorMapper = null;
		authorRelationMapper = null;
		publishingInfoMapper = null;
	}

	@RequestMapping(value="/titleResultList", method = RequestMethod.GET)
	public String titleResultList(Locale locale, Model model,
			@RequestParam("keyword") String keyword,
			@RequestParam("from") int from,
			@RequestParam("count") int count,
			@RequestParam("type") int type) {
		try {
			keyword = new String(keyword.getBytes("iso-8859-1"),"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		SearchResultPaginationModel pagination = new SearchResultPaginationModel();
		pagination.setKeyword(keyword);
		pagination.setFrom(from);
		pagination.setCount(count);
		session = sqlSessionFactory.openSession();
		resetMapper();
		try {
			pagination.setTotalCount(getTitleResultListCount(keyword));
			if(pagination.getTotalCount() > 0) {
				List<BookListItemModel> titleList = getTitleResultList(keyword, from, count);
				pagination.setListCount(titleList.size());
				model.addAttribute("resultList", titleList);
			}
			model.addAttribute("pagination", pagination);
			model.addAttribute("searchType", "titleResultList");
			model.addAttribute("bookRankList", getBookRankList());
			session.commit();
		} finally {
			session.close();
		}
		if(type == 0)
			return "resultListPage";
		else
			return "resultList";
	}

	@RequestMapping(value="/authorResultList", method = RequestMethod.GET)
	public String authorResultList(Locale locale, Model model,
			@RequestParam("keyword") String keyword,
			@RequestParam("from") int from,
			@RequestParam("count") int count,
			@RequestParam("type") int type) {
		try {
			keyword = new String(keyword.getBytes("iso-8859-1"),"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		SearchResultPaginationModel pagination = new SearchResultPaginationModel();
		pagination.setKeyword(keyword);
		pagination.setFrom(from);
		pagination.setCount(count);
		session = sqlSessionFactory.openSession();
		resetMapper();
		logger.info("authorRelativeList");
		try {
			pagination.setTotalCount(getAuthorResultListCount(keyword));
			if(pagination.getTotalCount() > 0) {
				List<BookListItemModel> authorList = getAuthorResultList(keyword, from, count);
				pagination.setListCount(authorList.size());
				model.addAttribute("resultList", authorList);
			}
			model.addAttribute("pagination", pagination);
			model.addAttribute("searchType", "authorResultList");
			model.addAttribute("bookRankList", getBookRankList());
			session.commit();
		} finally {
			session.close();
		}
		if(type == 0)
			return "resultListPage";
		else
			return "resultList";
	}

	@RequestMapping(value="/publisherResultList", method = RequestMethod.GET)
	public String publisherResultList(Locale locale, Model model,
			@RequestParam("keyword") String keyword,
			@RequestParam("from") int from,
			@RequestParam("count") int count,
			@RequestParam("type") int type) {
		try {
			keyword = new String(keyword.getBytes("iso-8859-1"),"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		SearchResultPaginationModel pagination = new SearchResultPaginationModel();
		pagination.setKeyword(keyword);
		pagination.setFrom(from);
		pagination.setCount(count);
		session = sqlSessionFactory.openSession();
		resetMapper();
		try {
			pagination.setTotalCount(getPublisherResultListCount(keyword));
			if(pagination.getTotalCount() > 0) {
				List<BookListItemModel> publisherList = getPublisherResultList(keyword, from, count);
				pagination.setListCount(publisherList.size());
				model.addAttribute("resultList", publisherList);
			}
			model.addAttribute("pagination", pagination);
			model.addAttribute("searchType", "publisherResultList");
			model.addAttribute("bookRankList", getBookRankList());
			session.commit();
		} finally {
			session.close();
		}
		if(type == 0)
			return "resultListPage";
		else
			return "resultList";
	}
}
