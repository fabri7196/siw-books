package it.uniroma3.siw.siw_books.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.siw_books.model.Credentials;
import it.uniroma3.siw.siw_books.repository.CredentialsRepository;

@Service
public class CredentialsService {

    @Autowired
    CredentialsRepository credentialsRepository;

    public Credentials getCredentials(Long id) {
        return this.credentialsRepository.findById(id).get();
    }

    public Credentials getCredentials(String username) {
        return this.credentialsRepository.findByUsername(username).get();
    }

    public Credentials saveCredentials(Credentials credentials) {
        return this.credentialsRepository.save(credentials);
    }

}
