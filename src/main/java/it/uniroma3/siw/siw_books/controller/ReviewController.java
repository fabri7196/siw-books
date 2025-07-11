package it.uniroma3.siw.siw_books.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.siw_books.model.Book;
import it.uniroma3.siw.siw_books.model.Credentials;
import it.uniroma3.siw.siw_books.model.Review;
import it.uniroma3.siw.siw_books.service.BookService;
import it.uniroma3.siw.siw_books.service.CredentialsService;
import it.uniroma3.siw.siw_books.service.ReviewService;
import it.uniroma3.siw.siw_books.service.UserService;
import it.uniroma3.siw.siw_books.validator.ReviewValidator;
import jakarta.validation.Valid;

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

    @Autowired
    private UserService userService;

    @Autowired
    private ReviewValidator reviewValidator;

    @GetMapping("/book/{id}/review")
    public String getFormReview(@PathVariable("id") Long id, Model model) {
        UserDetails userDetails = globalController.getUser();
        if (userDetails != null) {
            Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
            model.addAttribute("user", credentials);
            if (this.reviewService.userHasReviewedBook(credentials.getUser(), this.bookService.getBookById(id))) {
                return "redirect:/book/" + id;
            }
        }
        model.addAttribute("review", new Review());
        model.addAttribute("book", this.bookService.getBookById(id));
        return "formReview.html";
    }

    @PostMapping("/book/{id}/review/addReview")
    public String getAddedReview(@PathVariable("id") Long id, @Valid @ModelAttribute("review") Review review,
            BindingResult bindingResult, Model model) {
        UserDetails userDetails = globalController.getUser();
        Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
        Book book = this.bookService.getBookById(id);

        this.reviewValidator.validate(review, bindingResult);

        if (!bindingResult.hasErrors()) {
            review.setAuthor(credentials.getUser());

            review.setBook(book);
            review.setId(null);
            this.reviewService.saveReview(review);
            return "redirect:/book/" + id;
        }

        model.addAttribute("book",book);
        return "formReview.html";

    }

    @PostMapping("/book/{id}/{rId}/removeReview")
    public String postRemoveReview(@PathVariable("id") Long id, @PathVariable("rId") Long rId) {
        UserDetails userDetails = globalController.getUser();
        if (userDetails != null) {
            Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
            if (credentials.getRole().equals("ADMIN")) {
                this.reviewService.removeReview(this.reviewService.findReviewById(rId));
            }
        }

        return "redirect:/book/" + id;
    }

    @GetMapping("/user/reviews")
    public String getUserReviews(Model model) {
        UserDetails userDetails = globalController.getUser();
        List<Review> reviews = new ArrayList<>();
        if (userDetails != null) {
            Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
            model.addAttribute("user", credentials);
            reviews = this.reviewService.findByAuthor(credentials.getUser());
        }

        model.addAttribute("nameUser", null);
        model.addAttribute("reviews", reviews);
        return "myReviews.html";
    }

    @GetMapping("/{id}/reviews")
    public String getUserReviewsById(@PathVariable("id") Long id, Model model) {
        UserDetails userDetails = globalController.getUser();
        if (userDetails != null) {
            Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
            model.addAttribute("user", credentials);
        }
        model.addAttribute("reviews", this.reviewService.findByAuthor(this.userService.getUser(id)));
        model.addAttribute("nameUser", this.userService.getUser(id).getCredentials().getUsername());
        return "myReviews.html";
    }

}
