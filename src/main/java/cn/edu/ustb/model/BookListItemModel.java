package cn.edu.ustb.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BookListItemModel {
	private Integer id;
	private String imageSrc;
	private String title;
	private String author;
	private String publisher;
	private String date;
	private String price;
	private int star;
	
	public Integer getId() {
		return id;
	}
	public String getImageSrc() {
		return imageSrc;
	}
	public String getTitle() {
		return title;
	}
	public String getAuthor() {
		return author;
	}
	public String getPublisher() {
		return publisher;
	}
	public String getDate() {
		return date;
	}
	public String getPrice() {
		return price;
	}
	public int getStar() {
		return star;
	}
	public void setId(Integer id) {
		this.id = id;
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
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public void setDate(Date date) {
		DateFormat df = new SimpleDateFormat("yyyy-MM");
		this.date = df.format(date);
	}
	public void setPrice(Integer price) {
		if(price == null)
			price = 0;
		this.price = String.format("%.2f", price/100.0);
	}
	public void setStar(int star) {
		this.star = star;
	}
	public void setStar(Integer star1, Integer star2, Integer star3, Integer star4, Integer star5) {
		if(star1 == null)
			star1 = 0;
		if(star2 == null)
			star2 = 0;
		if(star3 == null)
			star3 = 0;
		if(star4 == null)
			star4 = 0;
		if(star5 == null)
			star5 = 0;
		int total = star1+star2+star3+star4+star5;
		if(total != 0)
			this.star = (star1 + 2*star2 + 3*star3 + 4*star4 + 5*star5) / (star1+star2+star3+star4+star5);
		else
			this.star = 0;
	}
}
