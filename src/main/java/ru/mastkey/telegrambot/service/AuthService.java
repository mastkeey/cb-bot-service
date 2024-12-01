package ru.mastkey.telegrambot.service;

import java.util.Map;

public interface AuthService {
    Map<String, String> getAuthorizationHeader(Long userId);
}
