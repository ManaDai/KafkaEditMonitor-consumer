package com.example.wikimedia_consumer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.wikimedia_consumer.domain.AppUser;
import com.example.wikimedia_consumer.domain.Keyword;
import com.example.wikimedia_consumer.domain.UserKeyword;
import com.example.wikimedia_consumer.service.UserKeywordService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private HttpSession session;

    @Autowired
    private UserKeywordService userKeywordService;


    @GetMapping("")
    public String showDashboard(Model model) {

        AppUser appUser = (AppUser) session.getAttribute("appUser");
        if (appUser == null) {
            return "redirect:/login";
        }
        // Keywordを表示する
        List<UserKeyword> userKeywordList = userKeywordService.findByAppUserId(appUser.getAppUserId());
        if (userKeywordList.isEmpty()) {
            return "dashboard";
        } else {
            List<Keyword> keywords = userKeywordService.findKeywordsByUserKeywords(userKeywordList);
            model.addAttribute("keywords", keywords);
            return "dashboard";
        }
        
    }

}
