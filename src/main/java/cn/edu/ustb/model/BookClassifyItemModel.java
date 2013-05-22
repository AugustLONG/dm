package cn.edu.ustb.model;

import java.util.List;

public class BookClassifyItemModel {
	private String imageSrc;
	private String tagName;
	private List<TitleList> titleList;
	
	public String getImageSrc() {
		return imageSrc;
	}
	public String getTagName() {
		return tagName;
	}
	public List<TitleList> getTitleList() {
		return titleList;
	}
	public void setImageSrc(String imageSrc) {
		this.imageSrc = imageSrc;
	}
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	public void setTitleList(List<TitleList> titleList) {
		this.titleList = titleList;
	}
	public TitleList createTitleList() {
		return new TitleList();
	}
	
	public class TitleList {
		private Integer id;
		private String title;
		
		public void setId(Integer id) {
			this.id = id;
		}
		public Integer getId() {
			return id;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getTitle() {
			return title;
		}
	}
}
