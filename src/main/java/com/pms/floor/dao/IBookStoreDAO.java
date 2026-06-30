package com.pms.floor.dao;

import java.util.List;

import com.pms.floor.entity.Book;

public interface IBookStoreDAO {
	
	public List<Book> getBooks();
	public Book getBook(int bookId);
	public Book createBook(Book book);
	public Book updateBook(int BookId,Book book);
	public boolean deleteBook(int BookId);

}
