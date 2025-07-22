package it.uniroma3.siw.siw_books.service;

import java.time.LocalDate;
import java.util.List;

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

    public List<Author> getAuthorsBySurname(String surname) {
        if(surname == null || surname.isEmpty()) {
            return List.of();
        }
        return this.authorRepository.findAllBySurnameIgnoreCase(surname);
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

    public List<Author> findAllByNameAndSurnameAndDateOfBirth(String name, String surname, LocalDate birth) {
        List<Author> list = this.authorRepository.findAllByNameAndSurnameAndDateOfBirth(name.trim(), surname.trim(), birth);
        if (list.isEmpty() || list == null) {
            return List.of();
        }
        
        return list;
    }

    public Author updateAuthor(Author author, Long id) {
        author.setId(id);
        return this.authorRepository.save(author);
    }
}
