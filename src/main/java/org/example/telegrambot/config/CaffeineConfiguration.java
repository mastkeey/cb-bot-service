package org.example.telegrambot.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.example.telegrambot.model.FileUploadInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Configuration
public class CaffeineConfiguration {

    @Bean
    public Cache<Long, Set<FileUploadInfo>> caffeine() {
        return Caffeine.newBuilder()
            .expireAfterAccess(5, TimeUnit.MINUTES)
            .build();
    }
}
