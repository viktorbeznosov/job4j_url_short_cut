package ru.job4j.urlshortcut.mapper;

import org.mapstruct.Mapper;
import ru.job4j.urlshortcut.dto.RegistrationRequest;
import ru.job4j.urlshortcut.dto.RegistrationResponse;
import ru.job4j.urlshortcut.model.Site;

@Mapper(componentModel = "spring")
public interface SiteMapper {

    Site toEntity(RegistrationRequest request);

    RegistrationResponse toRegistrationResponse(Site site, boolean registration);
}
