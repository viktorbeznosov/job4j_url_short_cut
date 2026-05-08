package ru.job4j.urlshortcut.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.job4j.urlshortcut.dto.AuthRequest;
import ru.job4j.urlshortcut.dto.AuthResponse;
import ru.job4j.urlshortcut.service.JwtService;
import ru.job4j.urlshortcut.service.SiteService;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@Tag(name = "Auth Controller", description = "API для авторизации и получения JWT токена")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final SiteService siteService;

    @PostMapping("/login")
    @Operation(
            summary = "Получение JWT токена",
            description = "Авторизация сайта по логину и паролю",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешная авторизация",
                            content = @Content(schema = @Schema(implementation = AuthResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Неверные учетные данные")
            }
    )
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getLogin(), request.getPassword())
        );

        String token = jwtService.generateToken(request.getLogin());
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
