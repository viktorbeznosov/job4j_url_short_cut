package ru.job4j.urlshortcut.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.job4j.urlshortcut.model.Url;

import java.util.List;
import java.util.Optional;

@Repository
public interface UrlRepository extends JpaRepository<Url, Long> {

    Optional<Url> findByCode(String code);

    List<Url> findBySiteId(Long siteId);

    @Modifying
    @Query("UPDATE Url u SET u.totalVisits = u.totalVisits + 1 WHERE u.code = :code")
    int incrementVisits(String code);
}
