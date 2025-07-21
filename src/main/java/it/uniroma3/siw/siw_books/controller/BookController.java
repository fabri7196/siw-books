package it.uniroma3.siw.siw_books.controller;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.springframework.validation.BindingResult;

import it.uniroma3.siw.siw_books.model.AssetImage;
import it.uniroma3.siw.siw_books.model.Book;
import it.uniroma3.siw.siw_books.model.Credentials;
import it.uniroma3.siw.siw_books.service.AssetImageService;
import it.uniroma3.siw.siw_books.service.BookService;
import it.uniroma3.siw.siw_books.service.CredentialsService;
import it.uniroma3.siw.siw_books.service.ReviewService;
import it.uniroma3.siw.siw_books.storage.StorageProperties;
import it.uniroma3.siw.siw_books.controller.validator.BookValidator;
import it.uniroma3.siw.siw_books.controller.validator.UpdateBookValidator;
import it.uniroma3.siw.siw_books.dto.UpdateBook;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.RequestBody;


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

    @Autowired
    private BookValidator bookValidator;

    @Autowired
    private UpdateBookValidator updateBookValidator;

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
	public String addBook(@Valid @ModelAttribute("book") Book book, BindingResult bindingResult, @RequestParam("files") List<MultipartFile> files, Model model) throws IOException{
        
        UserDetails userDetails = globalController.getUser();
        if (userDetails != null) {
            Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
            model.addAttribute("user", credentials);
        }

        this.bookValidator.validate(book, bindingResult);

        if(!bindingResult.hasErrors()) {
       
            this.bookService.saveBook(book);

            Path bookDir = Paths.get(storageProperties.getLocation(), String.valueOf(book.getId()));
            Files.createDirectories(bookDir);

            List<AssetImage> listAssetImage = new ArrayList<>();

            List<String> allowedTypes = Arrays.asList("image/jpeg", "image/png", "image/gif", "image/webp");
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    String contentType = file.getContentType();
                        if (!allowedTypes.contains(contentType)) {
                            bindingResult.reject("files.invalidType", "Solo file immagine sono ammessi");
                            return "formNewBook.html";
                        }
                    String filename = StringUtils.cleanPath(file.getOriginalFilename());
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
        return "formNewBook.html";
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
            // model.addAttribute("user", null);
        }
        model.addAttribute("numReviews",this.reviewService.countReview(id));
        model.addAttribute("book", book);
        model.addAttribute("authors", book.getAuthors());
        return "book.html";
    }

    @GetMapping("/home/searchBooks")
    public String getSearchBooks(@RequestParam(name = "title", required = false) String title, RedirectAttributes redirectAttributes, Model model) {
        List<Book> books = this.bookService.getBooksByTitle(title);
        if(!books.isEmpty()) {
            model.addAttribute("books", books);
            return "home.html";
        }

        redirectAttributes.addAttribute("notFound", true);
        return "redirect:/home";
    }

    @GetMapping("/{id}/updateBook")
    public String getUpdateBook(@PathVariable("id") Long bookId, Model model) {
        UserDetails userDetails = globalController.getUser();
        if (userDetails != null) {
            Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
            model.addAttribute("user", credentials);
        }

        model.addAttribute("updateBookForm", new UpdateBook());
        model.addAttribute("book", this.bookService.getBookById(bookId));

        return "updateFormBook.html";
    }

    @PostMapping("/{id}/updateBookSuccessful")
    public String postUpdateBook(@PathVariable("id") Long bookId, @Valid @ModelAttribute("updateBookForm") UpdateBook updateBook, BindingResult bindingResult, Model model) {
        UserDetails userDetails = globalController.getUser();
        if (userDetails != null) {
            Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
            model.addAttribute("user", credentials);
        }

        Book book = this.bookService.getBookById(bookId);
        updateBook.setId(bookId);

        this.updateBookValidator.validate(updateBook, bindingResult);

        if(!bindingResult.hasErrors()) {
            book.setCode(updateBook.getCode());
            book.setTitle(updateBook.getTitle());
            book.setYear_publication(updateBook.getYear_publication());
            this.bookService.updateBook(book, bookId);
            return "redirect:/book/" + bookId;
        }
        
        model.addAttribute("book", book);
        return "updateFormBook.html";
    }
    
    
    

}
