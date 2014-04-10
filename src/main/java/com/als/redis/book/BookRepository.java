package com.als.redis.book;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class BookRepository {
	
	@Autowired private RedisTemplate<String,Book> redis;
	
	public void saveBook(Book book) {
		redis.opsForHash().put(Book.NAMESPACE, Book.NAMESPACE + ":" + book.getKey(), book);
	}
	
	@SuppressWarnings("unchecked")
	public Collection<Book> findBooks(Collection<String> keys) {
		Collection<Object> keyObjs = new ArrayList<Object>();
		keyObjs.addAll(keys);
		
		return (List<Book>)(List<?>)redis.opsForHash().multiGet(Book.NAMESPACE, keyObjs);
	}

}
