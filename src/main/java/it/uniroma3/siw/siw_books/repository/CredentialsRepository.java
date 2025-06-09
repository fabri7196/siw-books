package it.uniroma3.siw.siw_books.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.uniroma3.siw.siw_books.model.Credentials;
import java.util.Optional;

@Repository
public interface CredentialsRepository extends CrudRepository<Credentials, Long> {

    Optional<Credentials> findByUsername(String username);
}
