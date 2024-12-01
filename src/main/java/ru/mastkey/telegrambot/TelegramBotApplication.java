package ru.mastkey.telegrambot;

import ru.mastkey.telegrambot.configuration.properties.ServiceProperties;
import ru.mastkey.telegrambot.configuration.properties.TokenProperties;
import ru.mastkey.telegrambot.configuration.properties.UrlProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties({TokenProperties.class, UrlProperties.class, ServiceProperties.class})
@SpringBootApplication
public class TelegramBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(TelegramBotApplication.class, args);
    }

}
