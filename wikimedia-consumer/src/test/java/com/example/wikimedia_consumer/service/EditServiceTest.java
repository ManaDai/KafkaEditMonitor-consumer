package com.example.wikimedia_consumer.service;

import static org.mockito.Mockito.when;

import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.wikimedia_consumer.domain.Edit;
import com.example.wikimedia_consumer.repository.EditRepository;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class EditServiceTest {

    @Mock
    private EditRepository editRepository;

    @InjectMocks
    private EditService editService;

    @Test
    void testGetEditsByKeywordId() {
        Edit edit1 = new Edit("editId1", "keywordId", "pageTitle1", "user1", "comment1", "timestamp1", "titleUrl1");
        Edit edit2 = new Edit("editId2", "keywordId", "pageTitle2", "user2", "comment2", "timestamp2", "titleUrl2");

        when(editRepository.findByKeywordId("keywordId")).thenReturn(Flux.just(edit1, edit2));

        Flux<Edit> result = editService.getEditsByKeywordId("keywordId");

        // StepVerifierを使って結果を検証
        StepVerifier.create(result)
                .expectNext(edit1)
                .expectNext(edit2)
                .verifyComplete();
    }

    @Test
    void testGetRecentEdits() {
        Edit edit1 = new Edit("editId1", "keywordId", "pageTitle1", "user1", "comment1", "timestamp1", "titleUrl1");
        Edit edit2 = new Edit("editId2", "keywordId", "pageTitle2", "user2", "comment2", "timestamp2", "titleUrl2");

        when(editRepository.findAll()).thenReturn(Flux.just(edit1, edit2));

        Flux<Edit> result = editService.getRecentEdits();

        StepVerifier.create(result)
                .expectNext(edit1)
                .expectNoEvent(Duration.ofSeconds(1)) // 1秒の遅延を検証
                .expectNext(edit2)
                .verifyComplete();
    }
}
