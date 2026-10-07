package com.github.inzmamkhan.ziplink_backend.controller;

import com.github.inzmamkhan.ziplink_backend.dto.request.CreateShortUrlRequest;
import com.github.inzmamkhan.ziplink_backend.dto.response.ShortUrlResponse;
import com.github.inzmamkhan.ziplink_backend.service.UrlShortenerService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/urls")
public class UrlShortenerController {

    @Autowired
    private UrlShortenerService urlShortenerService;

    @PostMapping("/shorten")
    public ResponseEntity<ShortUrlResponse> createShortUrl(@RequestBody CreateShortUrlRequest request,
                                                           HttpServletRequest httpRequest) {
        String baseUrl = httpRequest.getRequestURL().toString().replace(httpRequest.getRequestURI(), "");
        ShortUrlResponse response = urlShortenerService.createShortUrl(request, baseUrl);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}