package it.uniroma3.siw.siw_books.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;

@Entity
@SequenceGenerator(allocationSize = 1, initialValue = 1, name = "seq_idReview", sequenceName = "seq_idReview")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_idReview")
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private int vote;
    @Column(nullable = false)
    private String text;
    
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

}
