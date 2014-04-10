package com.als.redis.book;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"applicationContext.xml"})
public class LibraryTest {

	@Autowired Library lib;
	
	@Before public void init() {
		lib.initialize();
	}
	
	@Test public void testAutocomplete() {
		Collection<Book> books = lib.search("ca opp har");
		assertEquals(1, books.size());
		
		System.out.println("[Results for ca opp har]");
		for (Book book : books) {
			System.out.println("-> found book " + book);
		}
	}
	
	@Test public void testSearchAnd() {
		Collection<Book> books = lib.search("computer+opport");
		assertEquals(2, books.size());
		
		System.out.println("[Results for computer+opport]");
		for (Book book : books) {
			System.out.println("-> found book " + book);
		}
		
	}
	
	@Test public void testSearchOr() {
		Collection<Book> books = lib.search("opport|computer");
		assertEquals(14, books.size());
		
		System.out.println("[Results for computer|opport]");
		for (Book book : books) {
			System.out.println("-> found book " + book);
		}
		
	}
}
