package it.uniroma3.siw.siw_books.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.siw_books.dto.ChangePasswordForm;
import it.uniroma3.siw.siw_books.model.Credentials;
import it.uniroma3.siw.siw_books.service.CredentialsService;

@Component
public class ChangePasswordFormValidator implements Validator{

    @Autowired
    private CredentialsService credentialsService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public boolean supports(Class<?> clazz) {
        return ChangePasswordForm.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ChangePasswordForm form = (ChangePasswordForm)target;

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Credentials credentials = this.credentialsService.getCredentials(userDetails.getUsername());

        
        // Verifica che la vecchia password sia corretta
        if (!this.passwordEncoder.matches(form.getOldPassword(), credentials.getPassword())) {
            errors.rejectValue("oldPassword", "password.old.invalid");
        }

        // Verifica che la nuova password sia diversa dalla vecchia
        if (form.getOldPassword().equals(form.getNewPassword())) {
            errors.reject("password.same");
        }

        // Verifica che le due nuove password coincidano
        if (!form.getNewPassword().equals(form.getConfirmNewPassword())) {
            errors.rejectValue("newPassword", "password.confirmation.mismatch");
        }
    }
    
}
