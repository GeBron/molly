package com.demo.molly.util;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Thymeleaf 模板工具，供页面通过 {@code @templateHelper.xxx()} 调用
 */
@Component("templateHelper")
public class TemplateHelper {

    private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "-";
        }
        return DEFAULT_FORMATTER.format(dateTime);
    }
}
