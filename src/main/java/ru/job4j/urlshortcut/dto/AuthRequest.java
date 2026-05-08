package ru.job4j.urlshortcut.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthRequest {

    @NotBlank
    private String login;

    @NotBlank
    private String password;
}
