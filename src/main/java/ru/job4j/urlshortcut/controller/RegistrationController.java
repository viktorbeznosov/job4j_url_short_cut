package ru.job4j.urlshortcut.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.job4j.urlshortcut.dto.RegistrationRequest;
import ru.job4j.urlshortcut.dto.RegistrationResponse;
import ru.job4j.urlshortcut.service.SiteService;

@Slf4j
@RestController
@RequestMapping("/api/registration")
@AllArgsConstructor
@Tag(name = "Registration Controller", description = "API для регистрации сайтов")
public class RegistrationController {

    private final SiteService siteService;

    @PostMapping
    @Operation(
            summary = "Регистрация сайта",
            description = "Регистрирует новый сайт или возвращает данные существующего",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешная регистрация",
                            content = @Content(schema = @Schema(implementation = RegistrationResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Некорректные данные")
            }
    )
    public ResponseEntity<RegistrationResponse> register(@RequestBody RegistrationRequest request) {
        var response = siteService.register(request.getSite());

        return ResponseEntity.ok(response);
    }
}
