package com.example.wikimedia_consumer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.wikimedia_consumer.config.KafkaConsumerManager;
import com.example.wikimedia_consumer.domain.AppUser;
import com.example.wikimedia_consumer.service.AppUserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("login")
public class LoginController {

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private HttpSession session;

    @Autowired
    private KafkaConsumerManager kafkaConsumerManager;

    @GetMapping("")
    public String index() {
        return "login";
    }

    @PostMapping("/to-login")
    public String toLogin(@RequestParam("username") String username, @RequestParam("password") String password) {

        AppUser appUser = appUserService.findAppUser(username, password);
        if (appUser != null) {
            session.setAttribute("appUser", appUser);

            // ログイン成功時にKafkaコンテナを開始
            kafkaConsumerManager.startContainer("wikimedia-stream", appUser);

            return "redirect:/dashboard";

        } else {
            return "login";
        }

    }

}
