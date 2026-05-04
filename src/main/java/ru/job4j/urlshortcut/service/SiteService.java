package ru.job4j.urlshortcut.service;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.urlshortcut.model.Site;
import ru.job4j.urlshortcut.repository.SiteRepository;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class SiteService {

    private final SiteRepository siteRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public Site register(String siteName) {
        Optional<Site> existingSite = siteRepository.findBySite(siteName);
        if (existingSite.isPresent()) {
            return existingSite.get();
        }

        String login = UUID.randomUUID().toString();
        String password = UUID.randomUUID().toString();
        String passwordHash = passwordEncoder.encode(password);

        Site site = new Site();
        site.setSite(siteName);
        site.setLogin(login);
        site.setPasswordHash(passwordHash);

        Site savedSite = siteRepository.save(site);
        savedSite.setPasswordHash(password);
        return savedSite;
    }

    public Optional<Site> findByLogin(String login) {
        return siteRepository.findByLogin(login);
    }

    public boolean validatePassword(Site site, String rawPassword) {
        return passwordEncoder.matches(rawPassword, site.getPasswordHash());
    }
}
