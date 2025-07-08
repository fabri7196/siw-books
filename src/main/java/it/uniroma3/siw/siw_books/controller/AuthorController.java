package it.uniroma3.siw.siw_books.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;

import it.uniroma3.siw.siw_books.model.AssetImage;
import it.uniroma3.siw.siw_books.model.Author;
import it.uniroma3.siw.siw_books.model.Book;
import it.uniroma3.siw.siw_books.model.Credentials;
import it.uniroma3.siw.siw_books.service.AssetImageService;
import it.uniroma3.siw.siw_books.service.AuthorService;
import it.uniroma3.siw.siw_books.service.BookService;
import it.uniroma3.siw.siw_books.service.CredentialsService;
import it.uniroma3.siw.siw_books.storage.StorageProperties;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class AuthorController {

    private final BookService bookService;

    @Autowired
    private AuthorService authorService;

    @Autowired
    private CredentialsService credentialsService;

    @Autowired
    private AssetImageService assetImageService;

    @Autowired
    private GlobalController globalController;

    @Autowired
    private StorageProperties storageProperties;

    AuthorController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/authors")
    public String getAuthors(@RequestParam(name = "notFound", required = false) Boolean notFound, Model model) {
        model.addAttribute("requestURI", "/authors");

        if (Boolean.TRUE.equals(notFound)) {
            model.addAttribute("showNotFound", true);
        }

        List<Author> authors = (ArrayList<Author>) this.authorService.getAllAuthors();
        model.addAttribute("authors", authors);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            return "authors.html";
        } else {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                    .getPrincipal();
            Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
            model.addAttribute("user", credentials);
            return "authors.html";
        }
    }

    @GetMapping("/formNewAuthor")
    public String getFormNewAuthor(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            return "login.html";
        } else {
            UserDetails userDetails = globalController.getUser();
            Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
            model.addAttribute("user", credentials);
            model.addAttribute("author", new Author());
            return "formNewAuthor.html";
        }
    }

    @PostMapping("/addAuthor")
    public String addAuthor(@ModelAttribute("author") Author author, @RequestParam("file") MultipartFile file,
            Model model) throws IOException {

        UserDetails userDetails = globalController.getUser();
        if (userDetails != null) {
            Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
            model.addAttribute("user", credentials);
        }

        this.authorService.saveAuthor(author);

        if (file != null) {
            String filename = StringUtils.cleanPath(file.getOriginalFilename());
            Path authorDir = Paths.get(storageProperties.getLocation() + "/authors", String.valueOf(author.getId()));
            Files.createDirectories(authorDir);
            Path filePath = authorDir.resolve(filename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            AssetImage assetImage = new AssetImage();
            String publicPath = "/upload/authors/" + author.getId() + "/" + filename;
            assetImage.setPath(publicPath);
            AssetImage assetImageAuthor = this.assetImageService.saveImage(assetImage);

            author.setPhoto(assetImageAuthor);
        }

        this.authorService.saveAuthor(author);

        return "redirect:/authors";
        // return "redirect:/authors/" + author.getId();
    }

    @GetMapping("/author/{id}")
    public String getAuthor(@PathVariable("id") Long id, Model model) {
        Author author = this.authorService.getAuthorById(id);
        UserDetails userDetails = globalController.getUser();
        if (userDetails != null) {
            Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
            model.addAttribute("user", credentials);
        }
        model.addAttribute("author", author);
        
        return "author.html";
    }

    @PostMapping("/author/{id}/remove")
    public String postRemoveAuthor(@PathVariable("id") Long id) {
        Path folderPath = Paths.get("siw-books/upload/authors", String.valueOf(id));
        try {
            FileSystemUtils.deleteRecursively(folderPath);
        } catch (IOException e) {}

        this.authorService.removeAuthor(this.authorService.getAuthorById(id));
        
        return "redirect:/authors";
    }
    
    @GetMapping("/{id}/addAuthorsToBook")
    public String getAddAuthorToBook(@RequestParam(name = "notFound", required = false) Boolean notFound, @PathVariable("id") Long bookId, Model model) {
        model.addAttribute("requestURI", "/" + bookId + "/addAuthorsToBook");

        if (Boolean.TRUE.equals(notFound)) {
            model.addAttribute("showNotFound", true);
        }

        List<Author> authors = (ArrayList<Author>) this.authorService.getAllAuthors();
        model.addAttribute("authors", authors);

        UserDetails userDetails = globalController.getUser();
        if (userDetails != null) {
            Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
            model.addAttribute("user", credentials);
        }

        model.addAttribute("book", this.bookService.getBookById(bookId));

        return "authors.html";
    }

    @PostMapping("/{id}/{aid}/addedAuthorToBook")
    public String postAddedAuthorToBook(@PathVariable("id") Long bookId, @PathVariable("aid") Long authorId) {
        Book book = this.bookService.getBookById(bookId);
        if(book.getAuthors() == null) {
            List<Author> authors = new ArrayList<>();
            book.setAuthors(authors);
        }
        
        this.bookService.addAuthorToBook(book, this.authorService.getAuthorById(authorId));
        return "redirect:/" + bookId + "/addAuthorsToBook";
    }
    
    @PostMapping("/{id}/{aid}/removedAuthorFromBook")
    public String postRemovedAuthorFromBook(@PathVariable("id") Long bookId, @PathVariable("aid") Long authorId) {
        Book book = this.bookService.getBookById(bookId);

        this.bookService.removedAuthorFromBook(book, this.authorService.getAuthorById(authorId));
        return "redirect:/" + bookId + "/addAuthorsToBook";
    }
    

}
