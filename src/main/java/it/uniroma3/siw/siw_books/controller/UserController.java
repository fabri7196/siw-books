package it.uniroma3.siw.siw_books.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import it.uniroma3.siw.siw_books.controller.validator.ChangePasswordFormValidator;
import it.uniroma3.siw.siw_books.dto.ChangePasswordForm;
import it.uniroma3.siw.siw_books.model.Credentials;
import it.uniroma3.siw.siw_books.model.User;
import it.uniroma3.siw.siw_books.service.CredentialsService;
import it.uniroma3.siw.siw_books.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private GlobalController globalController;

    @Autowired
    private CredentialsService credentialsService;

    @Autowired
    private ChangePasswordFormValidator changePasswordFormValidator;

    @GetMapping("/AllUsers")
    public String getUsers(Model model) {
        UserDetails userDetails = this.globalController.getUser();
        Credentials credentials = this.credentialsService.getCredentials(userDetails.getUsername());
        List<User> users = this.userService.getAllUsersExceptCurrent(credentials);

        List<Integer> numberReviews = new ArrayList<>();
        for (User user : users) {
            numberReviews.add(user.getReviews().size());
        }

        model.addAttribute("user", credentials);
        model.addAttribute("numberReviews", numberReviews);
        model.addAttribute("users", users);

        return "showUsers.html";
    }

    @PostMapping("/users/delete/{userId}")
    public String postDeleteUser(@PathVariable("userId") Long id) {
        this.userService.deleteUser(id);
        return "redirect:/AllUsers";
    }

    @PostMapping("delete/currentUser")
    public String postDeleteCurrentUser(HttpServletRequest request) {
        UserDetails userDetails = this.globalController.getUser();
        Credentials credentials = this.credentialsService.getCredentials(userDetails.getUsername());

        this.credentialsService.deleteUser(credentials.getUsername());

        request.getSession().invalidate();
        SecurityContextHolder.clearContext();

        return "redirect:/";
    }

    @GetMapping("/userProfile")
    public String getUserProfile(Model model) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
        model.addAttribute("user", credentials);
        model.addAttribute("requestURI", "/userProfile");
        return "userProfile.html";
    }

    @GetMapping("/userProfile/changePass")
    public String getFormNewPass(Model model) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
        model.addAttribute("ChangePasswordForm", new ChangePasswordForm());
        model.addAttribute("user", credentials);
        return "changePass.html";
    }

    @PostMapping("/userProfile/changePass/success")
    public String postFormNewPass(@Valid @ModelAttribute("ChangePasswordForm") ChangePasswordForm form,
            BindingResult bindingResult, Model model) {

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
        model.addAttribute("user", credentials);

        this.changePasswordFormValidator.validate(form, bindingResult);

        if (bindingResult.hasErrors()) {
            return "changePass.html";
        }

        credentials.setPassword(form.getNewPassword());
        if(credentials.getRole().equals(Credentials.DEFAULT_ROLE))
            this.credentialsService.saveCredentials(credentials, Credentials.DEFAULT_ROLE);
        else
            this.credentialsService.saveCredentials(credentials, Credentials.ADMIN_ROLE);

        return "confirmChangePass.html";
    }

}
