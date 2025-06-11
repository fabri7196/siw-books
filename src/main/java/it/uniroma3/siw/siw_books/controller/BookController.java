package it.uniroma3.siw.siw_books.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import it.uniroma3.siw.siw_books.model.Book;
import it.uniroma3.siw.siw_books.model.Credentials;
import it.uniroma3.siw.siw_books.service.BookService;
import it.uniroma3.siw.siw_books.service.CredentialsService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class BookController {

    @Autowired 
    BookService bookService;

    @Autowired
	private CredentialsService credentialsService;
    
    @GetMapping("/formNewBook")
    public String formNewBook(Model model) {
        UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
        model.addAttribute("user", credentials);
        model.addAttribute("book", new Book());
        return "formNewBook.html";
    }

    @PostMapping("/book")
    public String addBook(@ModelAttribute("book") Book book) {
        this.bookService.saveBook(book);
        return "addedBook.html";
    }
    
}
