package com.example.wikimedia_consumer.controller;

import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.wikimedia_consumer.consumer.WikimediaConsumer;
import com.example.wikimedia_consumer.domain.Edit;

import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api")
public class EditController {


    private final WikimediaConsumer wikimediaConsumer;

    public EditController(WikimediaConsumer wikimediaConsumer) {
        this.wikimediaConsumer = wikimediaConsumer;
    }

    @GetMapping("/recent-edits")
    public Flux<ServerSentEvent<Edit>> streamEdits(@RequestParam String keywordId) {
        Flux<Edit> editStream = wikimediaConsumer.getEditStream()
                .filter(edit -> edit.getKeywordId().equals(keywordId));

        return editStream.map(edit -> ServerSentEvent.builder(edit)
                .id(edit.getEditId()) 
                .event("edit")
                .build());

    }

 
}
