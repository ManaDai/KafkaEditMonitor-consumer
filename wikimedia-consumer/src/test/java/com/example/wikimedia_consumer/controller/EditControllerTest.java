package com.example.wikimedia_consumer.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

import java.nio.charset.StandardCharsets;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.example.wikimedia_consumer.consumer.WikimediaConsumer;
import com.example.wikimedia_consumer.domain.Edit;

import reactor.core.publisher.Flux;

@WebFluxTest(EditController.class)
public class EditControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private WikimediaConsumer wikimediaConsumer;

    @BeforeEach
    void setUp() {
        // テスト用の `Edit` オブジェクトを準備
        Edit edit = new Edit();
        edit.setEditId("edit1");
        edit.setKeywordId("123");

        // モック設定
        when(wikimediaConsumer.getEditStream())
                .thenReturn(Flux.just(edit));
    }

    @Test
    void testStreamEdits() {

    webTestClient.get()
            .uri("/api/recent-edits?keywordId=123")
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .consumeWith(response -> {
                String body = new String(response.getResponseBody(), StandardCharsets.UTF_8);
                System.out.println("Response body: " + body);

                assertTrue(body.contains("event:edit"), "レスポンスに 'event: edit' が含まれていません");
                
                int dataIndex = body.indexOf("data:");
                assertTrue(dataIndex != -1, "レスポンスに 'data:' が含まれていません");

                String dataPart = body.substring(dataIndex + 5).trim(); // 'data:' の後の部分を取得

                assertTrue(dataPart.startsWith("{"), "データ部分が正しく開始していません");
                assertTrue(dataPart.endsWith("}"), "データ部分が正しく終了していません");

                try {
                    JSONObject jsonData = new JSONObject(dataPart);

                    assertTrue(jsonData.has("editId"), "データ部分に 'editId' フィールドがありません");
                    assertTrue(jsonData.has("keywordId"), "データ部分に 'keywordId' フィールドがありません");

                    assertEquals("edit1", jsonData.getString("editId"), "データ部分の 'editId' が期待と異なります");
                    assertEquals("123", jsonData.getString("keywordId"), "データ部分の 'keywordId' が期待と異なります");

                } catch (JSONException e) {
                    fail("データ部分の JSON パースに失敗しました: " + e.getMessage());
                }
            });

    }


}