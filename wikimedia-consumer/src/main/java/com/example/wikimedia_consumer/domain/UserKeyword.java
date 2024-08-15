package com.example.wikimedia_consumer.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user_keywords")
public class UserKeyword {

    @Id
    private String userKeywordId;

    private String appUserId;

    private String keywordId;

}
