package it.uniroma3.siw.siw_books.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.siw_books.model.AssetImage;
import it.uniroma3.siw.siw_books.repository.AssetImageRepository;

@Service
public class AssetImageService {
    
    @Autowired
    private AssetImageRepository assetImageRepository;
    
    public AssetImage save(AssetImage image) {
        return this.assetImageRepository.save(image);
    }
}
