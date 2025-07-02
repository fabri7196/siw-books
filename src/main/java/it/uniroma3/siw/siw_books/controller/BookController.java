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
import it.uniroma3.siw.siw_books.model.Book;
import it.uniroma3.siw.siw_books.model.Credentials;
import it.uniroma3.siw.siw_books.service.AssetImageService;
import it.uniroma3.siw.siw_books.service.BookService;
import it.uniroma3.siw.siw_books.service.CredentialsService;
import it.uniroma3.siw.siw_books.service.ReviewService;
import it.uniroma3.siw.siw_books.storage.StorageProperties;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class BookController {

    @Autowired
    BookService bookService;

    @Autowired
    private CredentialsService credentialsService;

    @Autowired
    private GlobalController globalController;

    @Autowired
    private AssetImageService assetImageService;

    @Autowired
    private StorageProperties storageProperties;

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/formNewBook")
	public String formNewBook(Model model) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            return "login.html";
        } else {
            UserDetails userDetails = globalController.getUser();
            Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
            model.addAttribute("user", credentials);
            model.addAttribute("book", new Book());
            return "formNewBook.html";
        }
	}

    @PostMapping("/addBook")
	public String addBook(@ModelAttribute("book") Book book, @RequestParam("files") List<MultipartFile> files, Model model) throws IOException{
        
        UserDetails userDetails = globalController.getUser();
        if (userDetails != null) {
            Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
            model.addAttribute("user", credentials);
        }
        this.bookService.saveBook(book);

        List<AssetImage> listAssetImage = new ArrayList<>();

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                String filename = StringUtils.cleanPath(file.getOriginalFilename());
                Path bookDir = Paths.get(storageProperties.getLocation(), String.valueOf(book.getId()));
                Files.createDirectories(bookDir);
                Path filePath = bookDir.resolve(filename);
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                
                AssetImage assetImage = new AssetImage();
                String publicPath = "/upload/" + book.getId() + "/" + filename;
                assetImage.setPath(publicPath);
                AssetImage assetImageBook = this.assetImageService.saveImage(assetImage);
                
                listAssetImage.add(assetImageBook);
            }
        }

        book.setCovers(listAssetImage);
        this.bookService.saveBook(book);
        
        return "redirect:/book/" + book.getId();
	}

    @PostMapping("/book/{id}/remove")
    public String postRemoveBook(@PathVariable("id") Long id) {
        Path folderPath = Paths.get("siw-books/upload/", String.valueOf(id));
        try {
            FileSystemUtils.deleteRecursively(folderPath);
        } catch (IOException e) {}

        this.bookService.removeBook(this.bookService.getBookById(id));
        return "redirect:/home";
    }
    

    @GetMapping("/book/{id}")
    public String getBook(@PathVariable("id") Long id, Model model) {
        Book book = this.bookService.getBookById(id);
        UserDetails userDetails = globalController.getUser();
        if (userDetails != null) {
            Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
            model.addAttribute("user", credentials);
            boolean alreadyReviewed = reviewService.userHasReviewedBook(credentials.getUser(), book);
            model.addAttribute("alreadyReviewed", alreadyReviewed);
        }
        else {
            model.addAttribute("alreadyReviewed", false);
        }
       
        model.addAttribute("book", book);
        return "book.html";
    }

}
