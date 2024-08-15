package com.example.wikimedia_consumer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.wikimedia_consumer.domain.AppUser;
import com.example.wikimedia_consumer.form.UserRegisterForm;
import com.example.wikimedia_consumer.service.AppUserService;

@Controller
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private AppUserService appUserService;

    @GetMapping("/to-register")
    public String toRegister(UserRegisterForm userRegisterForm) {
        return "user-registration";
    }

    @PostMapping("/add-user")
    public String addUser(@Validated UserRegisterForm userRegisterForm, BindingResult bindingResult, Model model) {

        // validationチェック
        if (bindingResult.hasErrors()) {
            return "user-registration";
        }

        Boolean isAlreadyRegistered = appUserService.isAlreadyRegisteredByEmail(userRegisterForm.getEmail());
        // メールアドレスの重複チェック
        if (isAlreadyRegistered) {
            model.addAttribute("error", "すでに登録済みのメールアドレスです。");
            return "user-registration";

        }

        AppUser newAppUser = new AppUser();
        newAppUser.setUsername(userRegisterForm.getUsername());
        newAppUser.setEmail(userRegisterForm.getEmail());
        newAppUser.setPassword(userRegisterForm.getPassword());
        appUserService.saveAppUser(newAppUser);

        return "redirect:/login";
    }

}
