package it.uniroma3.siw.siw_books.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.uniroma3.siw.siw_books.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long>{

}
