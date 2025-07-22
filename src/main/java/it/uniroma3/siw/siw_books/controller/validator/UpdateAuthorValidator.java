package it.uniroma3.siw.siw_books.controller.validator;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.siw_books.dto.UpdateAuthor;
import it.uniroma3.siw.siw_books.model.Author;
import it.uniroma3.siw.siw_books.service.AuthorService;

@Component
public class UpdateAuthorValidator implements Validator {

    @Autowired
    private AuthorService authorService;

    @Override
    public boolean supports(Class<?> clazz) {
        return UpdateAuthor.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UpdateAuthor updateAuthor = (UpdateAuthor)target;

        List<Author> authorByNameSurnameDateOfBirth = this.authorService.findAllByNameAndSurnameAndDateOfBirth(updateAuthor.getName(), updateAuthor.getSurname(), updateAuthor.getDateOfBirth());

        for (Author author : authorByNameSurnameDateOfBirth) {
            if(author.getId() != updateAuthor.getId()) {
                errors.reject("author.duplicato");
                break;
            }  
        }

        LocalDate today = LocalDate.now();

        if (updateAuthor.getDateOfBirth() != null && updateAuthor.getDateOfBirth().isAfter(today)) {
            errors.rejectValue("dateOfBirth", "dateOfBirth.future");
        }

        if (updateAuthor.getDateOfDeath() != null) {
            if (updateAuthor.getDateOfDeath().isAfter(today)) {
                errors.rejectValue("dateOfDeath", "dateOfDeath.future");
            }

            if (updateAuthor.getDateOfBirth() != null &&
                updateAuthor.getDateOfDeath().isBefore(updateAuthor.getDateOfBirth())) {
                errors.rejectValue("dateOfDeath", "date.invalidOrder");
            }
    
        }
    
    }
}
