package it.uniroma3.siw.siw_books.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.siw_books.model.Book;
import it.uniroma3.siw.siw_books.repository.BookRepository;

@Service
public class BookService {

    @Autowired 
    BookRepository bookRepository;

    public Book getBookById(Long id) {
        return this.bookRepository.findById(id).get();
    }

    public Iterable<Book> getAllBooks() {
        return this.bookRepository.findAll();
    }

    public Book saveBook(Book book) {
        return this.bookRepository.save(book);
    }

    public void removeBook(Book book) {
        this.bookRepository.delete(book);
    }
    
}
