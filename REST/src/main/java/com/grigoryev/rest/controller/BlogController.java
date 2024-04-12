package com.grigoryev.rest.controller;

import com.grigoryev.blog.Blog;
import com.grigoryev.rest.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/blog")
@RequiredArgsConstructor
public class BlogController {

    private final BlogService blogService;

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Blog> findById(@PathVariable String id,
                               @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token) {
        return blogService.findById(id, token);
    }

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Blog> findAll(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token) {
        return blogService.findAll(token);
    }

}
