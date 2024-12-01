package org.example.telegrambot.service;

import org.springframework.http.HttpStatus;

import java.util.UUID;

public interface UserService {
    HttpStatus createUser(Long userId, Long chatId, String username);
    HttpStatus selectWorkspace(Long userId, UUID workspaceName);
}
