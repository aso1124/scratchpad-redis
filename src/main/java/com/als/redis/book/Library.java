package com.als.redis.book;

import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Library {

	@Autowired BookRepository bookRepo;
	@Autowired IndexRepository indexRepo;
	
	public void initialize() {
		Collection<Book> books = loadBooks();
		
		for (Book book : books) {
			bookRepo.saveBook(book);
			indexRepo.indexBook(book);
		}
	}
	
	private Collection<Book> loadBooks() {
		Collection<Book> books = new ArrayList<Book>();
		try {
			Path resource = FileSystems.getDefault().getPath("docs", "books.csv");
			
			List<String> lines = Files.readAllLines(resource, Charset.defaultCharset());
			
			for (String line : lines) {
				Book book = new Book().populateFromParts(line.split("[,]"));
				books.add(book);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return books;
	}

	public Collection<Book> autocomplete(String allTerms) {
		Set<String> terms = parseTerms(allTerms.toLowerCase(), " ");
		Collection<String> bookKeys = indexRepo.findIntersection(terms);
		
		Collection<Book> books = bookRepo.findBooks(bookKeys);
		
		return books;
	}
	
	private Set<String> parseTerms(String unparsed, String token) {
		Set<String> parsed = new HashSet<String>();
		
		parsed.addAll(Arrays.asList(unparsed.split("["+token+"]")));
		
		return parsed;
	}
	
	public Collection<Book> search(String allTerms) {
		Collection<String> bookKeys = null;
		if (allTerms.contains("|")) {
			Set<String> terms = parseTerms(allTerms, "|");
			bookKeys = searchOr(terms);
		} else { //default to intersection
			Set<String> terms = parseTerms(allTerms, "+");
			bookKeys = searchAnd(terms);
		}
		Collection<Book> books = bookRepo.findBooks(bookKeys);
		
		return books;
	}
	
	private Collection<String> searchAnd(Collection<String> terms) {
		return indexRepo.findIntersection(terms);
	}
	
	private Collection<String> searchOr(Collection<String> terms) {
		return indexRepo.findUnion(terms);
	}
	
	public long getCountForIndex(String index) {
		return indexRepo.getCountForIndex(index);
	}
}
