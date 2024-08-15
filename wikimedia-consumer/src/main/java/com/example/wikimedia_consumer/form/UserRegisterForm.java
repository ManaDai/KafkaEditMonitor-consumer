package com.example.wikimedia_consumer.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserRegisterForm {

    @NotEmpty(message = "Please enter username.")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters.")
    private String username;

    @NotEmpty(message = "Please enter password.")
    @Size(min = 8, message = "Password must be at least 8 characters long.")
    private String password;

    @NotEmpty(message = "Please enter your email.")
    @Email(message = "Please enter a valid email address.")
    private String email;

}
