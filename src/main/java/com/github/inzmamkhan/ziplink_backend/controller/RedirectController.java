package com.github.inzmamkhan.ziplink_backend.controller;

import com.github.inzmamkhan.ziplink_backend.service.UrlShortenerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedirectController {

    @Autowired
    private UrlShortenerService urlShortenerService;

    @GetMapping("/{shortKey:[a-zA-Z0-9]+}")
    public ResponseEntity<Void> redirectToOriginalUrl(@PathVariable String shortKey) {
        String originalUrl = urlShortenerService.getOriginalUrl(shortKey);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, originalUrl);
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }
}