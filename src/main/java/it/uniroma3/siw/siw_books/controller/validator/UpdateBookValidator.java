package it.uniroma3.siw.siw_books.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.siw_books.dto.UpdateBook;
import it.uniroma3.siw.siw_books.model.Book;
import it.uniroma3.siw.siw_books.service.BookService;

@Component
public class UpdateBookValidator implements Validator {

    @Autowired
    private BookService bookService;

    @Override
    public boolean supports(Class<?> clazz) {
        return UpdateBook.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UpdateBook updateBook = (UpdateBook) target;

        Book bookByCode = this.bookService.getBookByCode(updateBook.getCode());

        if (bookByCode != null && !bookByCode.getCode().equals(updateBook.getCode())) {
            errors.rejectValue("code", "updateBook.code");
        }

        Book bookByTitle = this.bookService.getBookByTitle(updateBook.getTitle());
        
        if (bookByTitle != null && !bookByTitle.getId().equals(updateBook.getId())) {
            errors.rejectValue("title", "updateBook.title");
        }
    }

}
