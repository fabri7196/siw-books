package it.uniroma3.siw.siw_books.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import it.uniroma3.siw.siw_books.service.AssetImageService;

@Controller
public class AssetImageController {

    @Autowired
    private AssetImageService assetImageService;

}
