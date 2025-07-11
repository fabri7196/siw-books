package it.uniroma3.siw.siw_books.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.siw_books.model.Author;
import it.uniroma3.siw.siw_books.model.Book;
import it.uniroma3.siw.siw_books.repository.AuthorRepository;
import it.uniroma3.siw.siw_books.repository.BookRepository;

@Service
public class BookService {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired 
    private BookRepository bookRepository;

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

    public List<Book> getBooksByTitle(String title) {
        if(title == null || title.isEmpty()) {
            return List.of();
        }
        // String searchTitleBook = title.substring(0, 1).toUpperCase().concat(title.substring(1).toLowerCase());
        // searchTitleBook = searchTitleBook.trim();
        return this.bookRepository.findAllByTitleIgnoreCase(title);
    }

    public Book addAuthorToBook(Book book, Author author) {
        book.getAuthors().add(author);
        author.getBooks().add(book);
        this.authorRepository.save(author);
        return this.saveBook(book);
    }

    public Book removedAuthorFromBook(Book book, Author author) {
        book.getAuthors().remove(author);
        author.getBooks().remove(book);

        this.authorRepository.save(author);
        return this.saveBook(book);
    }

    public Book getBookByCode(Long code) {
        return this.bookRepository.findByCode(code);
    }
    
}
