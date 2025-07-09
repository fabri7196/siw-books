package it.uniroma3.siw.siw_books.model;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;

@Entity
@SequenceGenerator(initialValue = 1, allocationSize = 1, name = "seq_idAuthor")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_idAuthor")
    private Long id;
    
    @Column(nullable = false)
    private String name;
   
    @Column(nullable = false)
    private String surname;
   
    @Column(nullable = false)
    private LocalDate dateOfBirth;
    
    private LocalDate dateOfDeath;
    
    @Column(nullable = false)
    private String nationality;
    
    @OneToOne(cascade = CascadeType.REMOVE)
    private AssetImage photo;
    
    @ManyToMany
    private List<Book> books;
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    
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
    
    public AssetImage getPhoto() {
        return photo;
    }
    public void setPhoto(AssetImage photo) {
        this.photo = photo;
    }
    
    public List<Book> getBooks() {
        return books;
    }
    public void setBooks(List<Book> books) {
        this.books = books;
    }

}
