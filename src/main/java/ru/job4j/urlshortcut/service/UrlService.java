package ru.job4j.urlshortcut.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.urlshortcut.dto.ConvertRequest;
import ru.job4j.urlshortcut.dto.ConvertResponse;
import ru.job4j.urlshortcut.dto.StatisticItem;
import ru.job4j.urlshortcut.mapper.UrlMapper;
import ru.job4j.urlshortcut.model.Url;
import ru.job4j.urlshortcut.repository.UrlRepository;
import ru.job4j.urlshortcut.model.Site;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class UrlService {

    private final UrlRepository urlRepository;
    private final UrlMapper urlMapper;

    @Transactional
    public ConvertResponse convertUrl(String originalUrl, Long siteId) {
        String code = generateShortCode();

        Url url = urlMapper.toEntity(new ConvertRequest(originalUrl));
        url.setCode(code);
        url.setTotalVisits(0L);
        url.setSite(new Site());
        url.getSite().setId(siteId);

        url = urlRepository.save(url);

        return urlMapper.toConvertResponse(url);
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

    public List<StatisticItem> getStatistics(Long siteId) {
        return urlRepository.findBySiteId(siteId).stream()
                .map(urlMapper::toStatisticItem)
                .collect(Collectors.toList());
    }

    private String generateShortCode() {
        byte[] bytes = new byte[6];
        new java.security.SecureRandom().nextBytes(bytes);
        return java.util.Base64.getUrlEncoder().withoutPadding().encodeToString(bytes).substring(0, 6);
    }
}
