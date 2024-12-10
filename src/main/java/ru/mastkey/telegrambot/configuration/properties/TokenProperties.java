package ru.mastkey.telegrambot.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "telegram")
@Data
public class TokenProperties {
    private String token;
}
