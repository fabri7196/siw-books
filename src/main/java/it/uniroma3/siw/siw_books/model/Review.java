package it.uniroma3.siw.siw_books.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Entity
@SequenceGenerator(allocationSize = 1, initialValue = 1, name = "seq_idReview")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_idReview")
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(nullable = false)
    @Min(1)
    @Max(5)
    private int vote;
    
    @Column(nullable = false, length = 1000)
    private String text;

    @OneToOne
    private Book book;
    
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
    
    public int getVote() {
        return vote;
    }
    public void setVote(int vote) {
        this.vote = vote;
    }
    
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    
    public Book getBook() {
        return book;
    }
    public void setBook(Book book) {
        this.book = book;
    }

}
