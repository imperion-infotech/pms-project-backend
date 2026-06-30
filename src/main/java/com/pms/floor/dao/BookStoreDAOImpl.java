package com.pms.floor.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.pms.floor.entity.Book;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Transactional
@Repository
public class BookStoreDAOImpl implements IBookStoreDAO {

	@PersistenceContext
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public List<Book> getBooks() {
		String hql = "FROM Book as atcl ORDER BY atcl.id";
		return (List<Book>) entityManager.createQuery(hql).getResultList();
	}

	public Book getBook(int bookId) {
		return entityManager.find(Book.class, bookId);
	}

	public Book createBook(Book book) {
		entityManager.persist(book);
		Book b = getLastInsertedBook();
		return b;
	}

	public Book updateBook(int bookId, Book book) {
		// First We are taking Book detail from database by given book id and
		// then updating detail with provided book object
		Book bookFromDB = getBook(bookId);
		bookFromDB.setName(book.getName());
		bookFromDB.setAuthor(book.getAuthor());
		bookFromDB.setCategory(book.getCategory());
		bookFromDB.setPublication(book.getPublication());
		bookFromDB.setPages(book.getPages());
		bookFromDB.setPrice(book.getPrice());

		entityManager.flush();

		// again i am taking updated result of book and returning the book object
		Book updatedBook = getBook(bookId);

		return updatedBook;
	}

	public boolean deleteBook(int bookId) {
		Book book = getBook(bookId);
		entityManager.remove(book);

		// we are checking here that whether entityManager contains earlier deleted book
		// or not
		// if contains then book is not deleted from DB that's why returning false;
		boolean status = entityManager.contains(book);
		if (status) {
			return false;
		}
		return true;
	}

	/**
	 * This method will get the latest inserted record from the database and return
	 * the object of Book class
	 * 
	 * @return book
	 */
	private Book getLastInsertedBook() {
		String hql = "from Book order by id DESC";
		Query query = entityManager.createQuery(hql);
		query.setMaxResults(1);
		Book book = (Book) query.getSingleResult();
		return book;
	}

//	@Override
//	public Book CreateBook(Book book) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//
//	@Override
//	public Book book(int BookId, Book book) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//	

}
