package it.uniroma3.siw.siw_books.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.siw_books.model.User;
import it.uniroma3.siw.siw_books.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User getUser(Long id) {
        return this.userRepository.findById(id).get();
    }

    public User saveUser(User user) {
        return this.userRepository.save(user);
    }

}
