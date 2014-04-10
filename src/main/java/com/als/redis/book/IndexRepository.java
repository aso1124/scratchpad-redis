package com.als.redis.book;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class IndexRepository {
	@Autowired private RedisTemplate<String,String> redis;
	
	public void indexBook(Book book) {
		index(book.getKey(), parseWords(sanitize(book.getAuthor())));
		index(book.getKey(), parseWords(sanitize(book.getTitle())));
		index(book.getKey(), parseWords(sanitize(book.getCategory())));
	}
	
	private String[] parseWords(String unparsed) {
		return unparsed.toLowerCase().split("[ ]");
	}
	
	private String sanitize(String original) {
		return original.replaceAll("[,.!():;'*&\"]", "");
	}
	
	private void index(String key, String... words) {
		for (String word : words) {
			char[] letters = word.toCharArray();
			
			String prefix = "";
			for (char letter : letters) {
				prefix += letter;
				saveIndex(prefix, Book.NAMESPACE  + ":" + key, 1000);
			}
		}
	}
	
	private void saveIndex(String key, String value, double score) {
//		System.out.println("indexing [key:" + key + "] [value:"+value+"]");
		redis.opsForSet().add(key, value);
	}
	
	public long getCountForIndex(String index) {
		return redis.opsForSet().size(index);
	}
	
	public Collection<String> findIntersection(Collection<String> terms) {
		Collection<String> foundKeys = redis.opsForSet().intersect(terms.iterator().next(),terms);
		return foundKeys;
	}
	
	public Collection<String> findUnion(Collection<String> terms) {
		Collection<String> foundKeys = redis.opsForSet().union(terms.iterator().next(),terms);
		return foundKeys;
	}
}
