package cn.edu.ustb.model;

import java.util.ArrayList;
import java.util.List;

public class SearchResultPaginationModel {
	private String keyword;
	private Integer from;
	private Integer count;
	private Integer totalCount;
	private Integer listCount;
	private Integer pageCount;
	private Integer currentPage;
	private Integer prevPage;
	private Integer nextPage;
	private List<PageList> pageList;
	private boolean isFirst;
	private boolean isLast;
	
	public String getKeyword() {
		return keyword;
	}
	public Integer getFrom() {
		return from;
	}
	public Integer getCount() {
		return count;
	}
	public Integer getTotalCount() {
		return totalCount;
	}
	public Integer getListCount() {
		return listCount;
	}
	public Integer getPageCount() {
		if(count != 0) {
			if(totalCount%count != 0)
				return totalCount/count + 1;
			else
				return totalCount/count;
		} else
			return 0;
	}
	public Integer getCurrentPage() {
		if(count != 0) {
			return from/count + 1;
		} else
			return 0;
	}
	public Integer getPrevPage() {
		return getFrom() - getCount();
	}
	public Integer getNextPage() {
		return getFrom() + getCount();
	}
	public List<PageList> getPageList() {
		List<PageList> list = new ArrayList<PageList>();
		int count = getPageCount();
		int curr = getCurrentPage();
		if(count < 8){
			for(int i=1; i<=count; i++) {
				list.add(new PageList(i));
			}
		} else {
			if(curr < 5) {
				for(int i=1; i<=5; i++) {
					list.add(new PageList(i));
				}
				list.add(new PageList(0));
				list.add(new PageList(count));
			} else if(curr < count-3) {
				list.add(new PageList(1));
				list.add(new PageList(0));
				list.add(new PageList(curr-1));
				list.add(new PageList(curr));
				list.add(new PageList(curr+1));
				list.add(new PageList(0));
				list.add(new PageList(count));
			} else {
				list.add(new PageList(1));
				list.add(new PageList(0));
				for(int i=count-4; i<=count; i++) {
					list.add(new PageList(i));
				}
			}
		}
		return list;
	}
	public boolean getIsFirst() {
		if(getCurrentPage() == 1) {
			return true;
		}
		return false;
	}
	public boolean getIsLast() {
		if(getCurrentPage().equals(getPageCount())) {
			return true;
		}
		return false;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public void setFrom(Integer from) {
		this.from = from;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}
	public void setListCount(Integer listCount) {
		this.listCount = listCount;
	}

	public class PageList {
		private int id;
		private int from;
		
		public PageList(int id) {
			this.id = id;
		}
		
		public int getId() {
			return id;
		}
		public int getFrom() {
			return (id-1)*count;
		}
	}
}