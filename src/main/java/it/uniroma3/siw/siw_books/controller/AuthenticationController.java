package it.uniroma3.siw.siw_books.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.siw_books.model.Credentials;
import it.uniroma3.siw.siw_books.model.User;
import it.uniroma3.siw.siw_books.service.BookService;
import it.uniroma3.siw.siw_books.service.CredentialsService;
import it.uniroma3.siw.siw_books.service.UserService;
import it.uniroma3.siw.siw_books.validator.CredentialsValidator;
import jakarta.validation.Valid;


@Controller
public class AuthenticationController {
	
	@Autowired
	private CredentialsService credentialsService;

    @Autowired
	private UserService userService;

    @Autowired
    private BookService bookService;

    @Autowired
    private CredentialsValidator credentialsValidator;

    @GetMapping("/")
    public String getWelcome(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            model.addAttribute("user", null);
            return "index.html";
        }
        UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Credentials credentials = this.credentialsService.getCredentials(userDetails.getUsername());
        model.addAttribute("user", credentials);
        return "index.html";
    }
    

    @GetMapping(value = "/home") 
	public String getHome(@RequestParam(name = "notFound", required = false) Boolean notFound, Model model) {
        model.addAttribute("requestURI", "/home");
        model.addAttribute("books", this.bookService.getAllBooks());

        if(Boolean.TRUE.equals(notFound)) {
            model.addAttribute("showNotFound", true);
        }

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
	        return "home.html";
		}
		else {		
			UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
            model.addAttribute("user", credentials);
            return "home.html"; 
		}
	}
	
	@GetMapping(value = "/register") 
	public String getRegisterForm (Model model) {
		model.addAttribute("user", new User());
		model.addAttribute("credentials", new Credentials());
		return "formRegisterUser.html";
	}
	
	@GetMapping(value = "/login") 
	public String getLoginForm () {
		return "formLogin.html";
	}
		
    // @GetMapping(value = "/success")
    // public String defaultAfterLogin(Model model) {
        
    // 	UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    // 	Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
    //     model.addAttribute("user", credentials);
    // 	// if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
    //     //     return "admin/indexAdmin.html";
    //     // }
    //     return "home.html";
    // }

	@PostMapping(value = { "/register" })
    public String postRegisterUser(@Valid @ModelAttribute("user") User user,
        BindingResult userBindingResult, @Valid
        @ModelAttribute("credentials") Credentials credentials,
        BindingResult credentialsBindingResult,
        Model model) {
        
        this.credentialsValidator.validate(credentials, credentialsBindingResult);
        
		// se user e credential hanno entrambi contenuti validi, memorizza User e the Credentials nel DB
        if(!userBindingResult.hasErrors() && !credentialsBindingResult.hasErrors()) {
            this.userService.saveUser(user);
            credentials.setUser(user);
            this.credentialsService.saveCredentials(credentials);
            model.addAttribute("user", credentials);
            return "registrationSuccess.html";
        }
        else {
            return "formRegisterUser.html";
        }
    }
    
    @GetMapping("/userProfile")
    public String getUserProfile(Model model) {
        UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
        model.addAttribute("user", credentials);
        model.addAttribute("requestURI", "/userProfile");
        return "userProfile.html";
    }

    @GetMapping("/userProfile/changePass")
    public String getFormNewPass(Model model) {
        UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
        model.addAttribute("user", credentials);
        return "changePass.html";
    }
    
    @PostMapping("/userProfile/changePass/success")
    public String postFormNewPass(@RequestParam("oldPassword") String oldPass, 
        @RequestParam("newPassword") String newPass, 
        @RequestParam("confirmNewPassword") String confirmNewPass, Model model) {
        
            UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
        model.addAttribute("user", credentials);
        
        if (!newPass.equals(confirmNewPass)) {
            //Validazione dati, la password non coincide
            return "redirect:/home";            //MOMENTANEO
        }

        if (oldPass.equals(newPass)) {
            //Validazione dati, vecchia pass uguale alla nuova
            return "redirect:/home";            //MOMENTANEO
        }

        credentials.setPassword(confirmNewPass);
        this.credentialsService.saveCredentials(credentials);
        
        return "confirmChangePass.html";
    }
    
}