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
@Document(collection = "eidits")
public class Edit {

    @Id
    private String editId;

    private String keywordId; // 追加分・Keywordテーブルとの関連付け

    private String pageTitle;

    private String user;

    private String comment;

    private String timestamp;

    private String titleUrl;

}
