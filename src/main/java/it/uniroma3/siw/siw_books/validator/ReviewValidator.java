package it.uniroma3.siw.siw_books.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.siw_books.model.Review;

@Component
public class ReviewValidator implements Validator{

    @Override
    public boolean supports(Class<?> clazz) {
        return Review.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Review review = (Review)target;

        int vote = review.getVote();
        if(vote>5 || vote<1) {
            errors.rejectValue("vote", "vote.between");
        }
    }
    
}
