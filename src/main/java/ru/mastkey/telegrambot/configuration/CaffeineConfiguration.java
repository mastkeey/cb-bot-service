package ru.mastkey.telegrambot.configuration;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.RequiredArgsConstructor;
import ru.mastkey.telegrambot.configuration.properties.ServiceProperties;
import ru.mastkey.telegrambot.model.FileUploadInfo;
import ru.mastkey.telegrambot.util.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Configuration
@RequiredArgsConstructor
public class CaffeineConfiguration {

    private final ServiceProperties serviceProperties;

    @Bean
    public Cache<Long, Set<FileUploadInfo>> fileCache() {
        return Caffeine.newBuilder()
            .expireAfterAccess(Constants.DEFAULT_FILE_CACHE_EXPIRE_TIME, TimeUnit.MINUTES)
            .build();
    }

    @Bean
    public Cache<Long, String> tokenCache() {
        return Caffeine.newBuilder()
            .expireAfterWrite(serviceProperties.getTokenTTL())
            .build();
    }
}
