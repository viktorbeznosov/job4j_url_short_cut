package ru.job4j.urlshortcut.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.job4j.urlshortcut.dto.ConvertRequest;
import ru.job4j.urlshortcut.dto.ConvertResponse;
import ru.job4j.urlshortcut.dto.StatisticItem;
import ru.job4j.urlshortcut.model.Url;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UrlMapper {

    @Mapping(source = "url", target = "originalUrl")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", ignore = true)
    @Mapping(target = "totalVisits", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "site", ignore = true)
    Url toEntity(ConvertRequest request);

    ConvertResponse toConvertResponse(Url url);

    @Mapping(source = "originalUrl", target = "url")
    @Mapping(source = "totalVisits", target = "total")
    StatisticItem toStatisticItem(Url url);

    List<StatisticItem> toStatisticItemList(List<Url> urls);
}