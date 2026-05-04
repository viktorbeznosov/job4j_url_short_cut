package ru.job4j.urlshortcut.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.job4j.urlshortcut.controller.dto.RegistrationRequest;
import ru.job4j.urlshortcut.controller.dto.RegistrationResponse;
import ru.job4j.urlshortcut.service.SiteService;

@RestController
@RequestMapping("/api/registration")
@AllArgsConstructor
public class RegistrationController {

    private final SiteService siteService;

    @PostMapping
    public ResponseEntity<RegistrationResponse> register(@RequestBody RegistrationRequest request) {
        var site = siteService.register(request.getSite());
        boolean isNew = site.getPasswordHash().length() != 60;
        String password = isNew ? site.getPasswordHash() : null;
        String login = site.getLogin();

        return ResponseEntity.ok(new RegistrationResponse(isNew, login, password));
    }
}
