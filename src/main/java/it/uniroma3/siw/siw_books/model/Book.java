package it.uniroma3.siw.siw_books.model;

import java.time.Year;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;

@Entity
@SequenceGenerator(initialValue = 1, name = "seq_idBook", allocationSize = 1)
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_idBook")
    private Long id;

    @Column(unique = true)
    private Long code;
    
    @Column(nullable = false)
    private String title;
    
    @Column(nullable = false)
    private Year year_publication;
    
    @ManyToMany(mappedBy = "books")
    private List<Author> authors;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<Review> reviews;

    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "book_id")
    private List<AssetImage> covers;

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
    
    public Year getYear_publication() {
        return year_publication;
    }
    public void setYear_publication(Year year_publication) {
        this.year_publication = year_publication;
    }
    
    public List<Author> getAuthors() {
        return authors;
    }
    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }
    
    public List<Review> getReviews() {
        return reviews;
    } 
    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
    
    public List<AssetImage> getCovers() {
        return covers;
    }
    public void setCovers(List<AssetImage> covers) {
        this.covers = covers;
    }
    
    public Long getCode() {
        return code;
    }
    public void setCode(Long code) {
        this.code = code;
    }
   
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((code == null) ? 0 : code.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Book other = (Book) obj;
        if (code == null) {
            if (other.code != null)
                return false;
        } else if (!code.equals(other.code))
            return false;
        return true;
    }

}
