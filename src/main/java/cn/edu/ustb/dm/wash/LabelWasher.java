package cn.edu.ustb.dm.wash;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.swing.text.DateFormatter;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.edu.ustb.dm.dao.AuthorInfoMapper;
import cn.edu.ustb.dm.dao.BindingTypeMapper;
import cn.edu.ustb.dm.dao.BookAuthorInfoMapper;
import cn.edu.ustb.dm.dao.BookAuthorRelationMapper;
import cn.edu.ustb.dm.dao.BookInfoMapper;
import cn.edu.ustb.dm.dao.BookOnlineInfoMapper;
import cn.edu.ustb.dm.dao.BookPublishingInfoMapper;
import cn.edu.ustb.dm.dao.BookRelationMapper;
import cn.edu.ustb.dm.dao.BookTagRelationMapper;
import cn.edu.ustb.dm.dao.NationalityInfoMapper;
import cn.edu.ustb.dm.dao.PublisherInfoMapper;
import cn.edu.ustb.dm.dao.TagInfoMapper;
import cn.edu.ustb.dm.model.AuthorInfo;
import cn.edu.ustb.dm.model.AuthorInfoExample;
import cn.edu.ustb.dm.model.BindingType;
import cn.edu.ustb.dm.model.BindingTypeExample;
import cn.edu.ustb.dm.model.BookAuthorInfo;
import cn.edu.ustb.dm.model.BookAuthorInfoExample;
import cn.edu.ustb.dm.model.BookAuthorRelation;
import cn.edu.ustb.dm.model.BookInfo;
import cn.edu.ustb.dm.model.BookInfoWithBLOBs;
import cn.edu.ustb.dm.model.BookOnlineInfo;
import cn.edu.ustb.dm.model.BookOnlineInfoExample;
import cn.edu.ustb.dm.model.BookOnlineInfoWithBLOBs;
import cn.edu.ustb.dm.model.BookPublishingInfo;
import cn.edu.ustb.dm.model.BookPublishingInfoExample;
import cn.edu.ustb.dm.model.BookPublishingInfoWithBLOBs;
import cn.edu.ustb.dm.model.BookRelation;
import cn.edu.ustb.dm.model.BookTagRelation;
import cn.edu.ustb.dm.model.NationalityInfo;
import cn.edu.ustb.dm.model.NationalityInfoExample;
import cn.edu.ustb.dm.model.PublisherInfo;
import cn.edu.ustb.dm.model.PublisherInfoExample;
import cn.edu.ustb.dm.model.TagInfo;
import cn.edu.ustb.dm.model.TagInfoExample;

@Controller
public class LabelWasher {
	private static final Logger logger = LoggerFactory.getLogger(LabelWasher.class);

	private static final int interval = 50;
	private static final Pattern tagPattern = Pattern.compile("([^\\s\\(]+)\\((\\d+)\\)");
	private static final Pattern authorPattern = Pattern.compile(
			"^(;([\u4e00-\u9fa5]+);)?([\\w\u4e00-\u9fa5\\.•]+)");
	
	@Resource
	public SqlSessionFactory sqlSessionFactory;

	private SqlSession sqlSession;
	
	@RequestMapping(value = "/washer", method = RequestMethod.GET)
	public String washerDriver() {
		//int fromId = 1055000;//Original start.
		int endId = 4929300;//Original end;
		//int endId = 1250000;
		int fromId = 1109128;
		try {
			while(fromId < endId) {
				//fromId = washTagData(fromId, interval);
				//fromId = washAuthorData(fromId, interval);
				//fromId = washPublisherData(fromId, interval);
				//fromId = washTogetherBoughtData(fromId, interval);
				fromId = washBookInfoData(fromId, interval);
				
			}
		} finally {
			//sqlSession.close();
		}
		return "home";
	}
	
	private int washTagData(int fromId, int interval) {
		logger.info("washTag Id = " + fromId);
		sqlSession = sqlSessionFactory.openSession();
		BookOnlineInfoMapper bookOnlineInfoMapper = sqlSession.getMapper(BookOnlineInfoMapper.class);
		TagInfoMapper tagInfoMapper = sqlSession.getMapper(TagInfoMapper.class);
		BookTagRelationMapper bookTagRelationMapper = sqlSession.getMapper(BookTagRelationMapper.class);
		BookOnlineInfoExample bookOnlineInfoExample = new BookOnlineInfoExample();
		BookOnlineInfoExample.Criteria bookOnlineCriteria = bookOnlineInfoExample.createCriteria();
		bookOnlineCriteria.andBook_idBetween(fromId, fromId+interval-1);
		List<BookOnlineInfoWithBLOBs> bookOnlineInfoList =
				bookOnlineInfoMapper.selectByExampleWithBLOBs(bookOnlineInfoExample);
		for(BookOnlineInfoWithBLOBs bookOnlineInfo : bookOnlineInfoList) {
			String name = bookOnlineInfo.getLabel();
			Matcher matcher = tagPattern.matcher(name);
			while(matcher.find()) {
				String tagName = matcher.group(1);
				String tagUsageNum = matcher.group(2);
				TagInfoExample tagInfoExample = new TagInfoExample();
				TagInfoExample.Criteria tagInfoCriteria = tagInfoExample.createCriteria();
				tagInfoCriteria.andNAMEEqualTo(tagName);
				List<TagInfo> tagInfoList = tagInfoMapper.selectByExample(tagInfoExample);
				int tagId = 0;
				if(tagInfoList.size() > 0) {
					TagInfo tagInfo = tagInfoList.get(0);
					tagId = tagInfo.getTAG_ID();
					tagInfo.setCOUNT(tagInfo.getCOUNT()+1);
					tagInfoMapper.updateByPrimaryKey(tagInfo);
				} else {
					TagInfo tagInfo = new TagInfo();
					tagInfo.setCOUNT(1);
					tagInfo.setLEVEL(0);
					tagInfo.setNAME(tagName);
					tagInfoMapper.insert(tagInfo);
					tagInfoList = tagInfoMapper.selectByExample(tagInfoExample);
					if(tagInfoList.size() > 0) {
						tagInfo = tagInfoList.get(0);
						tagId = tagInfo.getTAG_ID();
					}
				}
				BookTagRelation bookTagRelation = new BookTagRelation();
				bookTagRelation.setBOOK_ID(bookOnlineInfo.getBook_id());
				bookTagRelation.setTAG_ID(tagId);
				bookTagRelation.setUSEAGE_NUM(Integer.parseInt(tagUsageNum));
				bookTagRelationMapper.insert(bookTagRelation);
			}
		}
		sqlSession.commit();
		sqlSession.close();
		return fromId+interval;
	}
	
	private int washAuthorData(int fromId, int interval) {
		logger.info("washAuthor Id = " + fromId);
		sqlSession = sqlSessionFactory.openSession();
		BookAuthorInfoMapper bookAuthorInfoMapper = sqlSession.getMapper(BookAuthorInfoMapper.class);
		NationalityInfoMapper nationalityInfoMapper = sqlSession.getMapper(NationalityInfoMapper.class);
		AuthorInfoMapper authorInfoMapper = sqlSession.getMapper(AuthorInfoMapper.class);
		BookAuthorRelationMapper bookAuthorRelationMapper =
				sqlSession.getMapper(BookAuthorRelationMapper.class);
		BookAuthorInfoExample bookAuthorInfoExample = new BookAuthorInfoExample();
		BookAuthorInfoExample.Criteria bookAuthorInfoCriteria = bookAuthorInfoExample.createCriteria();
		bookAuthorInfoCriteria.andBook_idBetween(fromId, fromId+interval-1);
		List<BookAuthorInfo> bookAuthorInfoList =
				bookAuthorInfoMapper.selectByExampleWithBLOBs(bookAuthorInfoExample);
		//logger.info("bookAuthorInfoList = " + bookAuthorInfoList.size());
		for(BookAuthorInfo bookAuthorInfo : bookAuthorInfoList) {
			String name = bookAuthorInfo.getAuthor_name();
			name = name.replaceAll("编著", "/");
			name = name.replaceAll("译者", "/");
			name = name.replaceAll("\\sand\\s", "/");
			name = name.replaceAll("\\s撰", "/");
			name = name.replaceAll("主编", "/");
			name = name.replaceAll("\\s等", "/");
			name = name.replaceAll("\\s注解", "/");
			name = name.replaceAll("，", "/");
			name = name.replaceAll(",", "/");
			name = name.replaceAll("\\(\\s*", ";");
			name = name.replaceAll("\\s*\\)", ";");
			name = name.replaceAll("（\\s*", ";");
			name = name.replaceAll("\\s*）", ";");
			name = name.replaceAll("\\[\\s*", ";");
			name = name.replaceAll("\\]\\s*", ";");
			name = name.replaceAll("\\s", "");
			for(String eachName : name.split("/")) {
				Matcher matcher = authorPattern.matcher(eachName);
				int nameIndex = 0;
				//logger.info("name = " + eachName);
				if(matcher.find()) {
					String nationalityName = matcher.group(2);
					String authorName = matcher.group(3);
					//logger.info("matcher.group(0) = " + matcher.group());
					//logger.info("matcher.group(2) = " + matcher.group(2));
					//logger.info("matcher.group(3) = " + matcher.group(3));
					AuthorInfoExample authorInfoExample = new AuthorInfoExample();
					AuthorInfoExample.Criteria authorInfoCriteria = authorInfoExample.createCriteria();
					authorInfoCriteria.andAUTHOR_NAMEEqualTo(authorName);
					List<AuthorInfo> authorInfoList = authorInfoMapper.selectByExample(authorInfoExample);
					int nationalityId = 0;
					if(nationalityName != null) {
					NationalityInfoExample nationalityInfoExample = new NationalityInfoExample();
					NationalityInfoExample.Criteria nationalityCriteria = nationalityInfoExample.createCriteria();
					nationalityCriteria.andNATIONALITY_NAMEEqualTo(nationalityName);
					List<NationalityInfo> nationalityInfoList = nationalityInfoMapper.selectByExample(nationalityInfoExample);
					if(nationalityInfoList.size() > 0) {
						NationalityInfo nationalityInfo = nationalityInfoList.get(0);
						nationalityId = nationalityInfo.getNATIONALITY_ID();
					} else {
						NationalityInfo nationalityInfo = new NationalityInfo();
						nationalityInfo.setNATIONALITY_NAME(nationalityName);
						nationalityInfoMapper.insert(nationalityInfo);
						nationalityInfoList = nationalityInfoMapper.selectByExample(nationalityInfoExample);
						if(nationalityInfoList.size() > 0) {
							nationalityInfo = nationalityInfoList.get(0);
							nationalityId = nationalityInfo.getNATIONALITY_ID();
						}
					}
					//logger.info("nationalityId = " + nationalityId);
					}
					int authorId = 0;
					if(authorInfoList.size() > 0) {
						AuthorInfo authorInfo = authorInfoList.get(0);
						authorId = authorInfo.getAUTHOR_ID();
					} else {
						AuthorInfo authorInfo = new AuthorInfo();
						authorInfo.setAUTHOR_NAME(authorName);
						authorInfo.setNATIONALITY_ID(nationalityId);
						String intro = bookAuthorInfo.getIntroduction();
						if(nameIndex == 0 && intro != null && intro.length() != 0)
							authorInfo.setINTRODUCTION(bookAuthorInfo.getIntroduction());
						authorInfoMapper.insert(authorInfo);
						authorInfoList = authorInfoMapper.selectByExample(authorInfoExample);
						if(authorInfoList.size() > 0) {
							authorInfo = authorInfoList.get(0);
							authorId = authorInfo.getAUTHOR_ID();
						}
					}
					BookAuthorRelation bookAuthorRelation = new BookAuthorRelation();
					bookAuthorRelation.setAUTHOR_ID(authorId);
					bookAuthorRelation.setBOOK_ID(bookAuthorInfo.getBook_id());
					bookAuthorRelationMapper.insert(bookAuthorRelation);
					nameIndex++;
				}
			}
		}
		sqlSession.commit();
		sqlSession.close();
		return fromId+interval;
	}
	
	private int washPublisherData(int fromId, int interval) {
		logger.info("washPublisher Id = " + fromId);
		sqlSession = sqlSessionFactory.openSession();
		BookPublishingInfoMapper bookPublishingInfoMapper =
				sqlSession.getMapper(BookPublishingInfoMapper.class);
		PublisherInfoMapper publisherInfoMapper =
				sqlSession.getMapper(PublisherInfoMapper.class);
		BindingTypeMapper bindingTypeMapper = sqlSession.getMapper(BindingTypeMapper.class);
		BookPublishingInfoExample bookPublishingInfoExample = new BookPublishingInfoExample();
		BookPublishingInfoExample.Criteria bookPublishingCriteria = bookPublishingInfoExample.createCriteria();
		bookPublishingCriteria.andBook_idBetween(fromId, fromId+interval-1);
		List<BookPublishingInfo> bookPublishingInfoList =
				bookPublishingInfoMapper.selectByExample(bookPublishingInfoExample);
		for(BookPublishingInfo bookPublishingInfo : bookPublishingInfoList) {
			String binding = bookPublishingInfo.getBinding();
			String publisher = bookPublishingInfo.getPublisher();
			if(publisher != null && publisher.length() != 0) {
				PublisherInfoExample publisherInfoExample = new PublisherInfoExample();
				PublisherInfoExample.Criteria publisherInfoCriteria = publisherInfoExample.createCriteria();
				publisherInfoCriteria.andPUBLISHER_NAMEEqualTo(publisher);
				List<PublisherInfo> publisherInfoList = publisherInfoMapper.selectByExample(publisherInfoExample);
				if(publisherInfoList.size() < 1)
				{
					PublisherInfo publisherInfo = new PublisherInfo();
					publisherInfo.setPUBLISHER_NAME(publisher);
					publisherInfoMapper.insert(publisherInfo);
				}
			}
			if(binding != null && binding.length() != 0) {
				BindingTypeExample bindingTypeExample = new BindingTypeExample();
				BindingTypeExample.Criteria bindingTypeCriteria = bindingTypeExample.createCriteria();
				bindingTypeCriteria.andBINDING_NAMEEqualTo(binding);
				List<BindingType> bindingTypeList = bindingTypeMapper.selectByExample(bindingTypeExample);
				if(bindingTypeList.size() < 1) {
					BindingType bindingType = new BindingType();
					bindingType.setBINDING_NAME(binding);
					bindingTypeMapper.insert(bindingType);
				}
			}
		}
		sqlSession.commit();
		sqlSession.close();
		return fromId+interval;
	}
	
	private int washTogetherBoughtData(int fromId, int interval) {
		logger.info("washTogetherBought Id = " + fromId);
		sqlSession = sqlSessionFactory.openSession();
		BookOnlineInfoMapper bookOnlineInfoMapper =
				sqlSession.getMapper(BookOnlineInfoMapper.class);
		BookRelationMapper bookRelationMapper =
				sqlSession.getMapper(BookRelationMapper.class);
		BookPublishingInfoMapper bookPublishingInfoMapper =
				sqlSession.getMapper(BookPublishingInfoMapper.class);
		BookOnlineInfoExample bookOnlineInfoExample = new BookOnlineInfoExample();
		BookOnlineInfoExample.Criteria bookOnlineInfoCriteria = bookOnlineInfoExample.createCriteria();
		bookOnlineInfoCriteria.andBook_idBetween(fromId, fromId+interval-1);
		List<BookOnlineInfoWithBLOBs> bookOnlineInfoList =
				bookOnlineInfoMapper.selectByExampleWithBLOBs(bookOnlineInfoExample);
		for(BookOnlineInfoWithBLOBs bookOnlineInfo : bookOnlineInfoList) {
			String relations = bookOnlineInfo.getTogether_bought();
			if(relations != null && relations.length() != 0) {
				for(String relation : relations.split(";")) {
					BookRelation bookRelation = new BookRelation();
					bookRelation.setBOOK_ID(bookOnlineInfo.getBook_id());
					bookRelation.setRELATION_BOOK_NAME(relation);
					BookPublishingInfoExample bookPublishingInfoExample = new BookPublishingInfoExample();
					BookPublishingInfoExample.Criteria bookPublishingInfoCriteria = bookPublishingInfoExample.createCriteria();
					//bookPublishingInfoCriteria.andti;
					List<BookPublishingInfo> bookPublishingInfoList = bookPublishingInfoMapper.selectByExample(bookPublishingInfoExample);
					if(bookPublishingInfoList.size() > 0)
						bookRelation.setRELATION_BOOK_ID(bookPublishingInfoList.get(0).getBook_id());
					bookRelationMapper.insert(bookRelation);
				}
			}
		}
		sqlSession.commit();
		sqlSession.close();
		return fromId+interval;
	}
	
	private int washBookInfoData(int fromId, int interval) {
		logger.info("washBookInfo Id = " + fromId);
		sqlSession = sqlSessionFactory.openSession();
		BookOnlineInfoMapper bookOnlineInfoMapper =
				sqlSession.getMapper(BookOnlineInfoMapper.class);
		BookPublishingInfoMapper bookPublishingInfoMapper =
				sqlSession.getMapper(BookPublishingInfoMapper.class);
		BookInfoMapper bookInfoMapper = sqlSession.getMapper(BookInfoMapper.class);
		BindingTypeMapper bindingTypeMapper = sqlSession.getMapper(BindingTypeMapper.class);
		PublisherInfoMapper publisherInfoMapper = sqlSession.getMapper(PublisherInfoMapper.class);
		BookPublishingInfoExample bookPublishingInfoExample = new BookPublishingInfoExample();
		bookPublishingInfoExample.or().andBook_idBetween(fromId, fromId + interval - 1);
		List<BookPublishingInfoWithBLOBs> bookPublishingInfoList =
				bookPublishingInfoMapper.selectByExampleWithBLOBs(bookPublishingInfoExample);
		for(BookPublishingInfoWithBLOBs bookPublishingInfo : bookPublishingInfoList) {
			BookOnlineInfoExample bookOnlineInfoExample = new BookOnlineInfoExample();
			bookOnlineInfoExample.or().andBook_idEqualTo(bookPublishingInfo.getBook_id());
			List<BookOnlineInfo> bookOnlineInfoList =
					bookOnlineInfoMapper.selectByExample(bookOnlineInfoExample);
			BookInfoWithBLOBs bookInfo = new BookInfoWithBLOBs();
			if(bookOnlineInfoList.size()>0)
			{
				BookOnlineInfo bookOnlineInfo = bookOnlineInfoList.get(0);
				if(bookOnlineInfo.getNumber_readed() != null)
					bookInfo.setNUMBER_READED(bookOnlineInfo.getNumber_readed());
				if(bookOnlineInfo.getNumber_reading() != null)
					bookInfo.setNUMBER_READING(bookOnlineInfo.getNumber_reading());
				if(bookOnlineInfo.getNumber_review() != null)
					bookInfo.setNUMBER_REVIEW(bookOnlineInfo.getNumber_review());
				if(bookOnlineInfo.getNumber_star1() != null)
					bookInfo.setNUMBER_STAR1(bookOnlineInfo.getNumber_star1());
				if(bookOnlineInfo.getNumber_star2() != null)
					bookInfo.setNUMBER_STAR2(bookOnlineInfo.getNumber_star2());
				if(bookOnlineInfo.getNumber_star3() != null)
					bookInfo.setNUMBER_STAR3(bookOnlineInfo.getNumber_star3());
				if(bookOnlineInfo.getNumber_star4() != null)
					bookInfo.setNUMBER_STAR4(bookOnlineInfo.getNumber_star4());
				if(bookOnlineInfo.getNumber_star5() != null)
					bookInfo.setNUMBER_STAR5(bookOnlineInfo.getNumber_star5());
				if(bookOnlineInfo.getNumber_toread() != null)
					bookInfo.setNUMBER_TOREAD(bookOnlineInfo.getNumber_toread());
			}
			bookInfo.setBOOK_ID(bookPublishingInfo.getBook_id());
			if(bookPublishingInfo.getIsbn() != null)
				bookInfo.setISBN(bookPublishingInfo.getIsbn());
			if(bookPublishingInfo.getTitle() != null)
				bookInfo.setTITLE(bookPublishingInfo.getTitle());
			if(bookPublishingInfo.getDescription() != null)
				bookInfo.setDESCRIPTION(bookPublishingInfo.getDescription());
			if(bookPublishingInfo.getDirectory() != null)
				bookInfo.setDIRECTORY(bookPublishingInfo.getDirectory());
			if(bookPublishingInfo.getBinding() != null) {
				BindingTypeExample bindingTypeExample = new BindingTypeExample();
				bindingTypeExample.or().andBINDING_NAMEEqualTo(bookPublishingInfo.getBinding());
				List<BindingType> bindingTypeList = bindingTypeMapper.selectByExample(bindingTypeExample);
				if(bindingTypeList.size() > 0)
					bookInfo.setBINDING_ID(bindingTypeList.get(0).getBINDING_ID());
			}
			if(bookPublishingInfo.getList_price() != null) {
				try {
					Double price = Double.parseDouble(bookPublishingInfo.getList_price());
					int priceTimes = (int) (price * 100);
					bookInfo.setLIST_PRICE(priceTimes);
				} catch(NumberFormatException e){
				}
			}
			if(bookPublishingInfo.getPage() != null) {
				try {
					int page = Integer.parseInt(bookPublishingInfo.getPage());
					bookInfo.setPAGE(page);
				} catch(NumberFormatException e){
				}
			}
			if(bookPublishingInfo.getPublisher() != null) {
				PublisherInfoExample publisherInfoExample = new PublisherInfoExample();
				publisherInfoExample.or().andPUBLISHER_NAMEEqualTo(bookPublishingInfo.getPublisher());
				List<PublisherInfo> publisherInfoList = publisherInfoMapper.selectByExample(publisherInfoExample);
				if(publisherInfoList.size() > 0)
					bookInfo.setPUBLISHER_ID(publisherInfoList.get(0).getPUBLISHER_ID());
			}
			if(bookPublishingInfo.getPublication_date() != null) {
				try {
					String dateStr = bookPublishingInfo.getPublication_date().trim();
					String format = null;
					if(dateStr.matches("^\\d+-\\d+-\\d+$"))
						format = "yyyy-MM-dd";
					else if(dateStr.matches("^\\d+-\\d+$"))
						format = "yyyy-MM";
					else if(dateStr.matches("^\\d{4}$"))
						format = "yyyy";
					else if(dateStr.matches("^\\d+年\\d+月\\d+日$"))
						format = "yyyy-MM-dd";
					else if(dateStr.matches("^\\d+年\\d+月$"))
						format = "yyyy-MM";
					else if(dateStr.matches("^\\d+年$"))
						format = "yyyy";
					if(format != null)
					{
						DateFormat df = new SimpleDateFormat(format);
						Date date = df.parse(dateStr);
						bookInfo.setPUBLICATION_DATE(date);
					}
				} catch(ParseException e){
				}
			}
			if(bookPublishingInfo.getTitle_page_images() != null)
				bookInfo.setTITLE_PAGE_IMAGES(bookPublishingInfo.getTitle_page_images());
			bookInfoMapper.insert(bookInfo);
		}
		sqlSession.commit();
		sqlSession.close();
		return fromId+interval;
	}
	
	
}
