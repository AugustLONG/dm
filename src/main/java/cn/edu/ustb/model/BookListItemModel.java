package cn.edu.ustb.model;

public class BookListItemModel {
	private String imageSrc;
	private String title;
	private String author;
	private int star;
	
	public String getImageSrc() {
		return imageSrc;
	}
	public String getTitle() {
		return title;
	}
	public String getAuthor() {
		return author;
	}
	public int getStar() {
		return star;
	}
	public void setImageSrc(String imageSrc) {
		this.imageSrc = imageSrc;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public void setStar(int star) {
		this.star = star;
	}
	public void setStar(int star1, int star2, int star3, int star4, int star5) {
		this.star = (star1 + 2*star2 + 3*star3 + 4*star4 + 5*star5) / (star1+star2+star3+star4+star5);
	}
}
