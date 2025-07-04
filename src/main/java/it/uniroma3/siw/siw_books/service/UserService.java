package it.uniroma3.siw.siw_books.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.siw_books.model.Credentials;
import it.uniroma3.siw.siw_books.model.User;
import it.uniroma3.siw.siw_books.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public User getUser(Long id) {
        return this.userRepository.findById(id).get();
    }

    @Transactional
    public User saveUser(User user) {
        return this.userRepository.save(user);
    }

    @Transactional
    public List<User> getAllUsersExceptCurrent(Credentials credentials) {
        List<User> result = new ArrayList<>();
        result = this.userRepository.getAllUsersExceptCurrent(credentials);
        
        if(result.isEmpty()) {
            return List.of();
        }

        return result;
        }

    @Transactional
    public void deleteUser(Long id){
        this.userRepository.deleteById(id);
    }
}
