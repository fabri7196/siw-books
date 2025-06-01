package it.uniroma3.siw.siw_books.model;

import java.time.Year;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;

@Entity
@SequenceGenerator(initialValue = 1, name = "seq_idBook", allocationSize = 1, sequenceName = "seq_idBook")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_idBook")
    private Long id;
    private String title;
    private Year year_pubblication;
    private List<String> urlImages;
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    
    public Year getYear_pubblication() {
        return year_pubblication;
    }
    public void setYear_pubblication(Year year_pubblication) {
        this.year_pubblication = year_pubblication;
    }
    
    public List<String> getUrlImages() {
        return urlImages;
    }
    public void setUrlImages(List<String> urlImages) {
        this.urlImages = urlImages;
    }
    
}
