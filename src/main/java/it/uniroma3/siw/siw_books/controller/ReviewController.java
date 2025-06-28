package it.uniroma3.siw.siw_books.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.siw_books.model.Credentials;
import it.uniroma3.siw.siw_books.model.Review;
import it.uniroma3.siw.siw_books.service.BookService;
import it.uniroma3.siw.siw_books.service.CredentialsService;
import it.uniroma3.siw.siw_books.service.ReviewService;

@Controller
public class ReviewController {

    @Autowired
    private GlobalController globalController;

    @Autowired
    private CredentialsService credentialsService;

    @Autowired 
    private BookService bookService;

    @Autowired
    private ReviewService reviewService;
    
    @GetMapping("/book/{id}/review")
    public String getFormReview(@PathVariable("id") Long id, Model model) {
        UserDetails userDetails = globalController.getUser();
        if (userDetails != null) {
            Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
            model.addAttribute("user", credentials);
        }
        model.addAttribute("review", new Review());
        model.addAttribute("book", this.bookService.getBookById(id));
        return "formReview.html";
    }

    @PostMapping("/book/{id}/review/addReview")
    public String getAddedReview(@PathVariable("id") Long id, @ModelAttribute("review") Review review) {
        UserDetails userDetails = globalController.getUser();
        Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
        review.setAuthor(credentials.getUser());

        review.setBook(this.bookService.getBookById(id));
        review.setId(null);
        this.reviewService.saveReview(review);
        return "redirect:/book/" + id;
    }

    @PostMapping("/book/{id}/{rId}/removeReview")
    public String postRemoveReview(@PathVariable("id") Long id, @PathVariable("rId") Long rId) {
        UserDetails userDetails = globalController.getUser();
        if (userDetails != null) {
            Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
            if(credentials.getRole().equals("ADMIN")) {
                this.reviewService.removeReview(this.reviewService.findReviewById(rId));
            }
        }
        
        return "redirect:/book/" + id;
    }
    
}
