package it.uniroma3.siw.siw_books.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;

public class UpdateAuthor {

    private Long id;

    @NotBlank
    private String name;
   
    @NotBlank
    private String surname;
   
    private LocalDate dateOfBirth;
    
    private LocalDate dateOfDeath;
    
    @NotBlank
    private String nationality;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public LocalDate getDateOfDeath() {
        return dateOfDeath;
    }

    public void setDateOfDeath(LocalDate dateOfDeath) {
        this.dateOfDeath = dateOfDeath;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
 
}
