package it.uniroma3.siw.siw_books.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.siw_books.model.Review;

public interface ReviewRepository extends CrudRepository<Review,Long> {

}
