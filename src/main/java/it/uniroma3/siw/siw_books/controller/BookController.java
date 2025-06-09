package it.uniroma3.siw.siw_books.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import it.uniroma3.siw.siw_books.model.Book;
import it.uniroma3.siw.siw_books.service.BookService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class BookController {

    @Autowired BookService bookService;
    
    @GetMapping("/about")
    public String getAbout() {
        return "about.html";
    }

    @GetMapping("/formNewBook")
    public String formNewBook(Model model) {
        model.addAttribute("book", new Book());
        return "formNewBook.html";
    }

    @PostMapping("/book")
    public String addBook(@ModelAttribute("book") Book book) {
        this.bookService.saveBook(book);
        return "redirect:/";
    }
    
}
