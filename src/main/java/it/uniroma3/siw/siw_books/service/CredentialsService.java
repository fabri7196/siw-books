package it.uniroma3.siw.siw_books.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.siw_books.model.Credentials;
import it.uniroma3.siw.siw_books.repository.CredentialsRepository;

@Service
public class CredentialsService {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    CredentialsRepository credentialsRepository;

    CredentialsService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Credentials getCredentials(Long id) {
        return this.credentialsRepository.findById(id).get();
    }

    @Transactional
    public Credentials getCredentials(String username) {
        return this.credentialsRepository.findByUsername(username).get();
    }

    @Transactional
    public Credentials saveCredentials(Credentials credentials) {
        credentials.setRole(Credentials.DEFAULT_ROLE);
        credentials.setPassword(this.passwordEncoder.encode(credentials.getPassword()));
        return this.credentialsRepository.save(credentials);
    }

    @Transactional
    public void deleteUser(String username){
        Credentials credentials = this.credentialsRepository.findByUsername(username).get();
        this.credentialsRepository.delete(credentials);
    }
}
