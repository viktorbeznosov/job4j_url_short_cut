package ru.job4j.urlshortcut.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.urlshortcut.model.Url;
import ru.job4j.urlshortcut.repository.UrlRepository;

import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UrlService {

    private final UrlRepository urlRepository;

    @Transactional
    public Url convertUrl(String originalUrl, Long siteId) {
        String code = generateShortCode();

        Url url = new Url();
        url.setOriginalUrl(originalUrl);
        url.setCode(code);
        url.setTotalVisits(0L);
        url.setSite(new ru.job4j.urlshortcut.model.Site());
        url.getSite().setId(siteId);

        return urlRepository.save(url);
    }

    @Transactional
    public Optional<String> redirect(String code) {
        int updatedRows = urlRepository.incrementVisits(code);

        if (updatedRows > 0) {
            return urlRepository.findByCode(code)
                .map(Url::getOriginalUrl);
        }

        return Optional.empty();
    }

    public List<Url> getStatistics(Long siteId) {
        return urlRepository.findBySiteId(siteId);
    }

    private String generateShortCode() {
        byte[] bytes = new byte[6];
        UUID.randomUUID().toString().getBytes(java.nio.charset.StandardCharsets.UTF_8);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(
                java.security.SecureRandom.getSeed(6)
        ).substring(0, 6);
    }
}
