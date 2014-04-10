package com.als.redis.book;

import java.io.Serializable;

public class Book implements Serializable {

	private static final long serialVersionUID = -5845270569794162653L;

	public static final String NAMESPACE="$books";
	
	private String key;
	private String category;
	private String title;
	private String author;
	private String year;

	public Book() {}

	public Book populateFromParts(String[] parts) {
		setCategory(sanitize(parts[0]));
		setKey(sanitize(parts[1]));
		setTitle(sanitize(parts[2]));
		setAuthor(sanitize(parts[3]));
		setYear(sanitize(parts[4]));
		
		return this;
	}
	
	private String sanitize(String part) {
		return part.replace("\"", "");
	}
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}
	
	public String toString() {
		return new StringBuilder(getTitle()).append(" by ").append(getAuthor()).toString();
	}
	
}
