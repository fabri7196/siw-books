package it.uniroma3.siw.siw_books.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.uniroma3.siw.siw_books.model.Book;
import it.uniroma3.siw.siw_books.model.Review;
import it.uniroma3.siw.siw_books.model.User;
import java.util.List;


@Repository
public interface ReviewRepository extends CrudRepository<Review,Long> {

    boolean existsByAuthorAndBook(User author, Book book);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.book.id =  :idBook")
    int countReview(@Param("idBook") Long idBook);

    List<Review> findByAuthor(User author);
}
