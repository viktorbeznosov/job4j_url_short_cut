package ru.job4j.urlshortcut.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationResponse {

    @JsonProperty("registration")
    private boolean registration;

    private String login;
    private String password;
}
