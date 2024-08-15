package com.example.wikimedia_consumer.service;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.wikimedia_consumer.domain.Edit;
import com.example.wikimedia_consumer.repository.EditRepository;

import reactor.core.publisher.Flux;

@Service
public class EditService {

    @Autowired
    private EditRepository editRepository;

 
    public Flux<Edit> getEditsByKeywordId(String keywordId) {
        return editRepository.findByKeywordId(keywordId);
    }

    //後で削除してもOK
    public Flux<Edit> getRecentEdits() {
        return editRepository.findAll()
                .delayElements(Duration.ofSeconds(1)); // デモ用に1秒の遅延を挿入
    }
}
