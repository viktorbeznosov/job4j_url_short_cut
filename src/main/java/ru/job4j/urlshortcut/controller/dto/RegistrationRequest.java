package ru.job4j.urlshortcut.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegistrationRequest {

    @NotBlank
    private String site;
}
