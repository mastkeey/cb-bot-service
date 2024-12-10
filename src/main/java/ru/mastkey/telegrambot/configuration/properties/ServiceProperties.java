package ru.mastkey.telegrambot.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "cloud")
@Data
public class ServiceProperties {
    private Duration tokenTTL;
}
