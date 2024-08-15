package com.example.wikimedia_consumer.consumer;

import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Service;

import com.example.wikimedia_consumer.domain.AppUser;
import com.example.wikimedia_consumer.domain.Edit;
import com.example.wikimedia_consumer.domain.Keyword;
import com.example.wikimedia_consumer.domain.UserKeyword;
import com.example.wikimedia_consumer.repository.EditRepository;
import com.example.wikimedia_consumer.service.UserKeywordService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Service
@Slf4j
public class WikimediaConsumer implements MessageListener<String, String> {

    private final EditRepository editRepository;
    private final Sinks.Many<Edit> editSink = Sinks.many().multicast().onBackpressureBuffer();
    private final UserKeywordService userKeywordService;
    private final ObjectMapper objectMapper;
    private AppUser appUser; 

    public WikimediaConsumer(
            EditRepository editRepository,
            UserKeywordService userKeywordService,
            ObjectMapper objectMapper
            ) {
        this.editRepository = editRepository;
        this.userKeywordService = userKeywordService;
        this.objectMapper = objectMapper;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    @Override
    public void onMessage(ConsumerRecord<String, String> record) {
        String msg = record.value();
        log.info(String.format("Consuming the message from wikimedia-stream Topic:: %s", msg));
        try {
            JsonNode jsonNode = objectMapper.readTree(msg);
            String title = jsonNode.get("title").asText();
            String comment = jsonNode.get("comment").asText();
            String user = jsonNode.get("user").asText();
            String timestamp = jsonNode.get("timestamp").asText();
            String titleUrl = jsonNode.get("title_url").asText();

            List<UserKeyword> userKeywordList = userKeywordService.findByAppUserId(appUser.getAppUserId());
            List<Keyword> keywords = userKeywordService.findKeywordsByUserKeywords(userKeywordList);

            Keyword matchedKeyword = keywords.stream()
                    .filter(keyword -> title.contains(keyword.getKeyword()) || comment.contains(keyword.getKeyword()))
                    .findFirst()
                    .orElse(null);

            if (matchedKeyword != null) {
                Edit edit = new Edit();
                edit.setPageTitle(title);
                edit.setComment(comment);
                edit.setUser(user);
                edit.setTimestamp(timestamp);
                edit.setTitleUrl(titleUrl);
                edit.setKeywordId(matchedKeyword.getKeywordId());

                editRepository.save(edit)
                        .doOnSuccess(savedEdit -> editSink.tryEmitNext(savedEdit))
                        .subscribe();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Flux<Edit> getEditStream() {
        return editSink.asFlux();
    }
}
