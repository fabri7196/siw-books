package it.uniroma3.siw.siw_books.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;

@Entity
@SequenceGenerator(initialValue = 1, allocationSize = 1, name = "seq_idImage", sequenceName = "seq_idImage")
public class AssetImage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_idImage")
    private Long id;

    @Column(nullable = false)
    private String path;

    @Column(length = 200)
    private String description;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
   
}
