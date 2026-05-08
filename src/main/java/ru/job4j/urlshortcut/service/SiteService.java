package ru.job4j.urlshortcut.service;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;  // Изменен импорт
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
    private final PasswordEncoder passwordEncoder;  // Изменено с BCryptPasswordEncoder на PasswordEncoder

    @Transactional
    public Site register(String siteName) {
        String login = UUID.randomUUID().toString();
        String password = UUID.randomUUID().toString();
        String passwordHash = passwordEncoder.encode(password);

        Site site = new Site();
        site.setSite(siteName);
        site.setLogin(login);
        site.setPasswordHash(passwordHash);

        Site savedSite = siteRepository.save(site);

        // Для ответа создайте новый объект или DTO, не изменяйте savedSite
        Site responseSite = new Site();
        responseSite.setId(savedSite.getId());
        responseSite.setSite(savedSite.getSite());
        responseSite.setLogin(savedSite.getLogin());
        responseSite.setPasswordHash(password);  // Открытый пароль только для ответа
        responseSite.setCreatedAt(savedSite.getCreatedAt());

        return responseSite;
    }

    public Optional<Site> findByLogin(String login) {
        return siteRepository.findByLogin(login);
    }

    public boolean validatePassword(Site site, String rawPassword) {
        return passwordEncoder.matches(rawPassword, site.getPasswordHash());
    }
}