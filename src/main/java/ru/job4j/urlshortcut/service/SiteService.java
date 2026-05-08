package ru.job4j.urlshortcut.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.urlshortcut.dto.RegistrationResponse;
import ru.job4j.urlshortcut.mapper.SiteMapper;
import ru.job4j.urlshortcut.model.Site;
import ru.job4j.urlshortcut.repository.SiteRepository;
import ru.job4j.urlshortcut.dto.RegistrationRequest;


import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class SiteService {

    private final SiteRepository siteRepository;
    private final PasswordEncoder passwordEncoder;
    private final SiteMapper siteMapper;

    @Transactional
    public RegistrationResponse register(String siteName) {
        String login = UUID.randomUUID().toString();
        String password = UUID.randomUUID().toString();
        String passwordHash = passwordEncoder.encode(password);

        Site site = siteMapper.toEntity(new RegistrationRequest(siteName));
        site.setLogin(login);
        site.setPasswordHash(passwordHash);
        site = siteRepository.save(site);

        return new RegistrationResponse(true, login, password);
    }

    public Optional<Site> findByLogin(String login) {
        return siteRepository.findByLogin(login);
    }

    public boolean validatePassword(Site site, String rawPassword) {
        return passwordEncoder.matches(rawPassword, site.getPasswordHash());
    }
}