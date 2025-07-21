package it.uniroma3.siw.siw_books.dto;

import jakarta.validation.constraints.NotBlank;

public class UpdateBook {

    private Long id;

    private Long code;
    
    @NotBlank
    private String title;
    
    private Integer year_publication;

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getYear_publication() {
        return year_publication;
    }

    public void setYear_publication(Integer year_publication) {
        this.year_publication = year_publication;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
}
