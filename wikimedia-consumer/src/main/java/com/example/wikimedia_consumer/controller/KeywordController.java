package com.example.wikimedia_consumer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.wikimedia_consumer.domain.AppUser;
import com.example.wikimedia_consumer.domain.Keyword;
import com.example.wikimedia_consumer.service.KeywordService;
import com.example.wikimedia_consumer.service.UserKeywordService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("keyword")
public class KeywordController {

    @Autowired
    private KeywordService keywordService;

    @Autowired
    private UserKeywordService userKeywordService;

    @Autowired
    HttpSession session;

    @PostMapping("add-keyword")
    public String addKeyword(@RequestParam("keyword-input") String keywordInput, Model model,
            RedirectAttributes redirectAttributes) {

        AppUser appUser = (AppUser) session.getAttribute("appUser");
        Keyword keyword = new Keyword();

        // すでに登録のあるキーワードがチェック
        List<String> userKeywordIds = userKeywordService.findKeywordIdsByUserId(appUser.getAppUserId());
        List<String> registeredStringKeywords = keywordService.findKeywordsByKeywordIds(userKeywordIds);

        // 入力されたキーワードとユーザーの登録済みキーワードを比較
        boolean isAlreadyRegisteredKeyword = registeredStringKeywords.contains(keywordInput);
        if (isAlreadyRegisteredKeyword) {
            redirectAttributes.addFlashAttribute("error", "すでに登録済みのキーワードです。");
            return "redirect:/dashboard";
        }
        keyword.setKeyword(keywordInput);
        Keyword registeredKeyword = keywordService.saveKeyword(keyword);
        userKeywordService.createUserKeyword(appUser.getAppUserId(), registeredKeyword.getKeywordId());

        return "redirect:/dashboard";
    }

    @DeleteMapping("/delete-keyword/{id}")
    public ResponseEntity<Void> deleteKeyword(@PathVariable("id") String keywordId) {
        Keyword keyword = keywordService.findById(keywordId);
        if (keyword != null) {
            // userKeyword テーブルから削除
            userKeywordService.deleteByKeywordId(keywordId);
            // keyword テーブルから削除
            keywordService.deleteById(keywordId);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

}
