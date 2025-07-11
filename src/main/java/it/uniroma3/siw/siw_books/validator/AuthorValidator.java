package it.uniroma3.siw.siw_books.validator;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.siw_books.model.Author;
import it.uniroma3.siw.siw_books.service.AuthorService;

@Component
public class AuthorValidator implements Validator{

    @Autowired
    private AuthorService authorService;

    @Override
    public boolean supports(Class<?> clazz) {
        return Author.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Author author = (Author)target;

        String name = author.getName();
        String surname = author.getSurname();
        LocalDate birth = author.getDateOfBirth();

        LocalDate today = LocalDate.now();

        //AUTORE DUPLICATO SE STESSO NOME,COGNOME E DATA DI NASCITA
        if(!this.authorService.findAllByNameAndSurnameAndDateOfBirth(name, surname, birth).isEmpty()) {
            errors.reject("author.duplicato");
        }

        if (author.getDateOfBirth() != null && author.getDateOfBirth().isAfter(today)) {
            errors.rejectValue("dateOfBirth", "dateOfBirth.future");
        }

        if (author.getDateOfDeath() != null) {
        if (author.getDateOfDeath().isAfter(today)) {
            errors.rejectValue("dateOfDeath", "dateOfDeath.future");
        }

        if (author.getDateOfBirth() != null &&
            author.getDateOfDeath().isBefore(author.getDateOfBirth())) {
            errors.rejectValue("dateOfDeath", "date.invalidOrder");
        }
    }

    }
    
}
