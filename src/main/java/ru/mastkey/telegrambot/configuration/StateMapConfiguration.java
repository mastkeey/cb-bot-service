package ru.mastkey.telegrambot.configuration;

import ru.mastkey.telegrambot.enums.InputState;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Configuration
public class StateMapConfiguration {

    @Bean
    public Map<Long, InputState> stateMap() {
        return new HashMap<>();
    }

    @Bean
    public Map<Long, UUID> currentWorkspaceToUpdateName() {
        return new HashMap<>();
    }

    @Bean
    public Map<Long, Integer> latestMsg() {
        return new HashMap<>();
    }

    @Bean
    public Map<Long, UUID> currentWorkspace() {
        return new HashMap<>();
    }
}
