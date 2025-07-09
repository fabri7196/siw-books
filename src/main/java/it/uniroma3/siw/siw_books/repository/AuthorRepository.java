package it.uniroma3.siw.siw_books.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.uniroma3.siw.siw_books.model.Author;

import java.util.List;
;

@Repository
public interface AuthorRepository extends CrudRepository<Author, Long> {

    public List<Author> findBySurname(String surname);

    @Query("SELECT a FROM Author a WHERE LOWER(a.surname) = LOWER(:surname)")
    List<Author> findAllBySurnameIgnoreCase(@Param("surname") String surname);

}
