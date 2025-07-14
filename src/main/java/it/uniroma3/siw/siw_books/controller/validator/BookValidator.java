package it.uniroma3.siw.siw_books.controller.validator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.siw_books.model.Book;
import it.uniroma3.siw.siw_books.service.BookService;

@Component
public class BookValidator implements Validator {

    @Autowired
    private BookService bookService;

    @Override
    public boolean supports(Class<?> clazz) {
        return Book.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Book book = (Book) target;

        //A parità di titolo, per essere uguale deve avere lo stesso anno
        if ((!this.bookService.getBooksByTitle(book.getTitle()).isEmpty())) {
            List<Book> list = this.bookService.getBooksByTitle(book.getTitle());
            for (Book li : list) {
                if ((li.getYear_publication() == book.getYear_publication())) {
                    errors.reject("book.duplicato");
                    break;
                }
            }
        }

        //Codice già utilizzato in un altro libro
        if (this.bookService.getBookByCode(book.getCode()) != null) {
            errors.rejectValue("code", "code.duplicato");
        }

    }

}
