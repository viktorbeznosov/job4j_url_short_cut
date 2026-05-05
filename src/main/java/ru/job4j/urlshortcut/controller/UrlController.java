package ru.job4j.urlshortcut.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.job4j.urlshortcut.controller.dto.ConvertRequest;
import ru.job4j.urlshortcut.controller.dto.ConvertResponse;
import ru.job4j.urlshortcut.controller.dto.StatisticItem;
import ru.job4j.urlshortcut.model.Url;
import ru.job4j.urlshortcut.service.UrlService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class UrlController {

    private final UrlService urlService;

    @PostMapping("/convert")
    public ResponseEntity<ConvertResponse> convert(@RequestBody ConvertRequest request) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        Long siteId = (Long) SecurityContextHolder.getContext().getAuthentication().getDetails();
        Url url = urlService.convertUrl(request.getUrl(), siteId);
        return ResponseEntity.ok(new ConvertResponse(url.getCode()));
    }

    @GetMapping("/statistic")
    public ResponseEntity<List<StatisticItem>> getStatistics() {
        Long siteId = (Long) SecurityContextHolder.getContext().getAuthentication().getDetails();
        List<Url> urls = urlService.getStatistics(siteId);
        List<StatisticItem> statistics = urls.stream()
                .map(u -> new StatisticItem(u.getOriginalUrl(), u.getTotalVisits()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(statistics);
    }

    @GetMapping("/redirect/{code}")
    public ResponseEntity<Void> redirect(@PathVariable String code) {
        log.info(" =================== CODE ================ {}", code);
        return urlService.redirect(code)
                .map(url -> ResponseEntity.status(302).header("Location", url).<Void>build())
                .orElse(ResponseEntity.notFound().build());
    }
}
