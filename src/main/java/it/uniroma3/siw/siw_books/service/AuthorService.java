package it.uniroma3.siw.siw_books.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.siw_books.model.Author;
import it.uniroma3.siw.siw_books.repository.AuthorRepository;

@Service
public class AuthorService {

    @Autowired 
    AuthorRepository authorRepository;
    
    public Author getAuthorById(Long id) {
        return this.authorRepository.findById(id).get();
    }

    public Iterable<Author> getAllAuthors() { 
        return this.authorRepository.findAll();
    }

    public Author saveAuthor(Author author) {
        return this.authorRepository.save(author);
    }

    public void removeAuthor(Author author) {
        this.authorRepository.delete(author);
    }
}
