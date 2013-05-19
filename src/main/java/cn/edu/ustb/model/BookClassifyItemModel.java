package cn.edu.ustb.model;

import java.util.List;

public class BookClassifyItemModel {
	private String imageSrc;
	private String tagName;
	private List<String> titleList;
	
	public String getImageSrc() {
		return imageSrc;
	}
	public String getTagName() {
		return tagName;
	}
	public List<String> getTitleList() {
		return titleList;
	}
	public void setImageSrc(String imageSrc) {
		this.imageSrc = imageSrc;
	}
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	public void setTitleList(List<String> titleList) {
		this.titleList = titleList;
	}
	
}
