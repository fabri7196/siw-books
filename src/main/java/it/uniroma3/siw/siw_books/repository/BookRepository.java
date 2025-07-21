package it.uniroma3.siw.siw_books.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.uniroma3.siw.siw_books.model.Book;

@Repository
public interface BookRepository extends CrudRepository<Book, Long> {

    @Query("SELECT b FROM Book b WHERE LOWER(b.title) = LOWER(:title)")
    public List<Book> findAllByTitleIgnoreCase(@Param("title") String title);

    public Book findByCode(Long code);

    public Book findByTitle(String title);
}