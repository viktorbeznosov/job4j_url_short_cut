package ru.job4j.urlshortcut.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ConvertRequest {

    @NotBlank
    private String url;
}
