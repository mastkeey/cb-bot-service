package org.example.telegrambot.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "api")
@Data
public class UrlProperties {
    public String cloudServiceUrl;
}
