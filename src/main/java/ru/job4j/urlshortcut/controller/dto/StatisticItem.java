package ru.job4j.urlshortcut.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatisticItem {

    private String url;
    private Long total;
}
