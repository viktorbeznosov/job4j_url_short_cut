package ru.job4j.urlshortcut.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.job4j.urlshortcut.dto.ConvertRequest;
import ru.job4j.urlshortcut.dto.ConvertResponse;
import ru.job4j.urlshortcut.dto.StatisticItem;
import ru.job4j.urlshortcut.service.UrlService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@AllArgsConstructor
@Tag(name = "URL Controller", description = "API для работы с короткими ссылками")
public class UrlController {

    private final UrlService urlService;

    @PostMapping("/convert")
    @Operation(
            summary = "Конвертация URL",
            description = "Преобразует длинный URL в короткий код",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "URL успешно сокращен",
                            content = @Content(schema = @Schema(implementation = ConvertResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Требуется авторизация")
            }
    )
    public ResponseEntity<ConvertResponse> convert(@RequestBody ConvertRequest request) {
        Long siteId = (Long) SecurityContextHolder.getContext().getAuthentication().getDetails();
        ConvertResponse response = urlService.convertUrl(request.getUrl(), siteId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/statistic")
    @Operation(
            summary = "Статистика переходов",
            description = "Возвращает статистику переходов по всем URL сайта",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Статистика получена"),
                    @ApiResponse(responseCode = "401", description = "Требуется авторизация")
            }
    )
    public ResponseEntity<List<StatisticItem>> getStatistics() {
        Long siteId = (Long) SecurityContextHolder.getContext().getAuthentication().getDetails();
        List<StatisticItem> statistics = urlService.getStatistics(siteId);
        return ResponseEntity.ok(statistics);
    }

    @GetMapping("/redirect/{code}")
    @Operation(
        summary = "Переадресация",
        description = "Выполняет редирект на оригинальный URL по короткому коду (без авторизации)",
        responses = {
            @ApiResponse(responseCode = "302", description = "Редирект на оригинальный URL"),
            @ApiResponse(responseCode = "404", description = "Код не найден")
        }
    )
    public ResponseEntity<Void> redirect(@PathVariable String code) {
        return urlService.redirect(code)
            .map(url -> ResponseEntity.status(302).header("Location", url).<Void>build())
            .orElse(ResponseEntity.notFound().build());
    }
}
