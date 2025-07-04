package it.uniroma3.siw.siw_books.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.siw_books.model.Book;
import it.uniroma3.siw.siw_books.model.Review;
import it.uniroma3.siw.siw_books.model.User;
import it.uniroma3.siw.siw_books.repository.ReviewRepository;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public Review findReviewById(Long id) {
        return this.reviewRepository.findById(id).get();
    }
    
    public Review saveReview(Review review) {
        return this.reviewRepository.save(review);
    }

    public boolean userHasReviewedBook(User user, Book book) {
        return reviewRepository.existsByAuthorAndBook(user, book);
    }

    public void removeReview(Review review) {
        this.reviewRepository.delete(review);
    }

    public int countReview(Long idBook) {
        return this.reviewRepository.countReview(idBook);
    }
}
