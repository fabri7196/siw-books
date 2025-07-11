package it.uniroma3.siw.siw_books.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.siw_books.model.Credentials;
import it.uniroma3.siw.siw_books.service.CredentialsService;


@Component
public class CredentialsValidator implements Validator{

    @Autowired
    private CredentialsService credentialsService;

    @Override
    public boolean supports(Class<?> clazz) {
        return Credentials.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Credentials credentials = (Credentials)target;
        String username = credentials.getUsername().trim();

        //CONTROLLO USERNAME GIA' UTILIZZATO
        if(this.credentialsService.getCredentials(username) != null) {
            errors.rejectValue("username", "username.usato");
        }  
    }
    
}
